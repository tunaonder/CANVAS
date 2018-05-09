/*
 * Created by Sait Tuna Onder on 2017.03.13  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.builder;

import java.util.HashMap;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import vt.canvas.component.EnterPoint;
import vt.canvas.component.ExitPoint;
import vt.canvas.component.Fork;
import vt.canvas.component.Merge;
import vt.canvas.component.MoveSpot;
import vt.canvas.component.TrafficLight;
import vt.canvas.component.helpers.StaticObject;
import vt.canvas.execution.Simulator;
import vt.canvas.websocket.SimulationSessionHandler;

/**
 * This Class Builds The Simulation Model with the Model Information sent by the Client
 * @author Onder
 */
public class SimulationBuilder {

    Map objects;
    private final String sessionIdentifier;

    public SimulationBuilder(String sessionIdentifier) {
        objects = new HashMap();
        this.sessionIdentifier = sessionIdentifier;
        
    }

    public boolean buildModel(JsonArray modelData, Simulator sim) throws Exception {
        
        int enterPointCount = 0;
        int exitPointCount = 0;
               
        System.out.println("==Simulation Model Build Start==");
        
        // First, Read Each Json Object and Create Static Objects According to JSON Object Type
        // First object of model data(modelData[0]) is used for other data such as simulation duration
        for (int i = 1; i < modelData.size(); i++) {
            
            JsonObject spot = modelData.getJsonObject(i);

            String id = spot.getString("objectId");

            double x = spot.getJsonNumber("x").doubleValue();
            double y = spot.getJsonNumber("y").doubleValue();

            //Create Objects of Different Types and add to the Hash Map
            StaticObject object;
                  
            switch (spot.getString("type")) {
                case "Standart":

                    object = new MoveSpot(id, x, y);
                    objects.put(object.getId(), object);

                    break;
                case "EnterPoint":
                    int minTime = Integer.parseInt(spot.getString("minTime"));
                    int maxTime = Integer.parseInt(spot.getString("maxTime"));
                    
                    object = new EnterPoint(id, x, y, minTime, maxTime);
                    objects.put(object.getId(), object);
                    enterPointCount ++;

                    break;
                case "ExitPoint":

                    object = new ExitPoint(id, x, y);
                    objects.put(object.getId(), object);
                    exitPointCount++;

                    break;

                case "Fork":
                    int newPathProbability = Integer.parseInt(spot.getString("newPathProbability"));
                    object = new Fork(id, x, y, newPathProbability);
                    objects.put(object.getId(), object);

                    break;
                case "Merge":

                    object = new Merge(id, x, y);
                    objects.put(object.getId(), object);

                    break;
                    
                case "TrafficLight":
                    int greenStartTime = Integer.parseInt(spot.getString("greenStartTime"));
                    int greenDuration = Integer.parseInt(spot.getString("greenDuration"));
                    int redDuration = Integer.parseInt(spot.getString("redDuration"));
                    
                    object = new TrafficLight(id, x, y, greenStartTime, greenDuration, redDuration);
                    objects.put(object.getId(), object);
                
                    break;
                default:

                    break;
            }

        }
        
        // Check if Model has at least one Enter point
        if(enterPointCount == 0){
            sendErrorMessage("Missing Enter Point");
            return false;            
        }
        // Check if Model has at least one Exit point
        if(exitPointCount == 0){
             sendErrorMessage("Missing Exit Point");
            return false;            
        }       

        // Set the Connection Between Objects
        for (int i = 1; i < modelData.size(); i++) {

            JsonObject spot = modelData.getJsonObject(i);

            String id = spot.getString("objectId");

            String prevId = "";
            String nextId = "";
            // Used For Fork
            String alternativeNextId;
            // Used For Merge
            String alternativePrevId;

            StaticObject prev = null;
            StaticObject next = null;
            // Only For Fork Objects
            StaticObject next2;
            // Only For Merge Objects
            StaticObject prev2;
            
            // All object types except ExitPoint must have a valid next object
            if(!spot.getString("type").equals("ExitPoint")){
                nextId = spot.getString("nextId"); 
                if(nextId.equals("none")){
                    String numberId = id.substring(1);
                    sendErrorMessage("Object " + numberId + " does not have a valid next object!");
                    return false;
                }   
                next = (StaticObject) objects.get(nextId);                            
            }
            // All object types except EnterPoint must have a valid prev object
            else if(!spot.getString("type").equals("EnterPoint")){
                prevId = spot.getString("prevId");
                if(prevId.equals("none")){
                    String numberId = id.substring(1);
                    sendErrorMessage("Object " + numberId + " does not have a valid previous object!");
                    return false;
                }  
                prev = (StaticObject) objects.get(prevId);            
            }

            switch (spot.getString("type")) {

                // Standart type has prev and next objects
                case "Standart":
                    MoveSpot moveSpot = (MoveSpot) objects.get(id);
                    moveSpot.setPrev(prev);
                    moveSpot.setNext(next);

                    sim.addObject(moveSpot);

                    break;
                case "Fork":

                    alternativeNextId = spot.getString("alternativeNextId");
                    // Check if Fork object is valid
                    if (alternativeNextId.equals("none") || alternativeNextId.equals(nextId) || nextId.equals(prevId)
                            || alternativeNextId.equals(prevId)) {
                        String numberId = id.substring(1);
                        sendErrorMessage("Objet" + numberId + " is not a valid Fork");
                        return false;
                    }

                    next2 = (StaticObject) objects.get(alternativeNextId);

                    Fork fork = (Fork) objects.get(id);
                    fork.setPrev(prev);
                    fork.setNext(next);
                    fork.setNextAlternative(next2);

                    sim.addObject(fork);

                    break;
                    
                case "Merge":

                    alternativePrevId = spot.getString("alternativePrevId");
                    // Check if Merge object is valid
                    if (alternativePrevId.equals("none") || alternativePrevId.equals(prevId) || nextId.equals(prevId)
                            || alternativePrevId.equals(nextId)) {
                        String numberId = id.substring(1);
                        sendErrorMessage("Objet" + numberId + " is not a valid Merge");
                        return false;
                    }

                    prev2 = (StaticObject) objects.get(alternativePrevId);
                    Merge merge = (Merge) objects.get(id);
                    merge.setPrev(prev);
                    merge.setNext(next);
                    merge.setPrevAlternative(prev2);

                    sim.addObject(merge);

                    break;    

                case "EnterPoint":

                    EnterPoint enterPoint = (EnterPoint) objects.get(id);
                    enterPoint.setNext(next);

                    sim.addObject(enterPoint);

                    break;
  
                case "ExitPoint":

                    ExitPoint exitPoint = (ExitPoint) objects.get(id);
                    exitPoint.setPrev(prev);

                    sim.addObject(exitPoint);

                    break;
                case "TrafficLight":
                    TrafficLight light = (TrafficLight) objects.get(id);
                    light.setPrev(prev);
                    light.setNext(next);

                    sim.addObject(light);
                    
                    break;
                default:

                    break;
            }

        }
        System.out.println("==Simulation Model Build Final--");

        // Send Success Message To Client
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "buildSuccess")
                .build();
        SimulationSessionHandler.sendMessageToClient(sessionIdentifier, message);
        return true;
    }
    
    private void sendErrorMessage(String errorDetail){
                     JsonProvider provider = JsonProvider.provider();
             JsonObject errorMessage = provider.createObjectBuilder()
                .add("action", "buildError")
                .add("errorDetail", errorDetail)
                .build();
            SimulationSessionHandler.sendMessageToClient(sessionIdentifier, errorMessage);        
    }
   
}
