/*
 * Created by Sait Tuna Onder on 2017.03.13  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.builder;

import java.util.HashMap;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonObject;
import vt.trafficnetwork.component.EnterPoint;
import vt.trafficnetwork.component.ExitPoint;
import vt.trafficnetwork.component.Fork;
import vt.trafficnetwork.component.Merge;
import vt.trafficnetwork.component.MoveSpot;
import vt.trafficnetwork.component.TrafficLight;
import vt.trafficnetwork.component.helpers.MovementObject;
import vt.trafficnetwork.execution.Simulator;

/**
 * This Class Builds The Simulation Model with the Model Information sent by the Client
 * @author Onder
 */
public class SimulationBuilder {

    Map objects;

    public SimulationBuilder() {
        objects = new HashMap();
    }

    public void buildModel(JsonArray modelData, Simulator sim) throws Exception {

        System.out.println("==Simulation Model Build Start--");
        
        //First Read Each Json Object and Create Static Objects According to JSON Object Type
        for (int i = 0; i < modelData.size(); i++) {

            JsonObject spot = modelData.getJsonObject(i);

            //'s' represents static objects
            String id = spot.getString("objectId");

            double x = spot.getJsonNumber("x").doubleValue();
            double y = spot.getJsonNumber("y").doubleValue();

            //Create Objects of Different Types and add to Hash Map
            MovementObject object;

            switch (spot.getString("type")) {
                case "Standart":

                    object = new MoveSpot(id, x, y);
                    objects.put(object.getId(), object);

                    break;
                case "EnterPoint":

                    object = new EnterPoint(id, x, y);
                    objects.put(object.getId(), object);

                    break;
                case "ExitPoint":

                    object = new ExitPoint(id, x, y);
                    objects.put(object.getId(), object);

                    break;

                case "Fork":

                    object = new Fork(id, x, y);
                    objects.put(object.getId(), object);

                    break;
                case "Merge":

                    object = new Merge(id, x, y);
                    objects.put(object.getId(), object);

                    break;
                    
                case "TrafficLight":
                    
                    object = new TrafficLight(id, x, y);
                    objects.put(object.getId(), object);
                
                    break;
                default:

                    break;
            }

        }

        //Second, Set the Connection Between Objects and add them to runtime
        for (int i = 0; i < modelData.size(); i++) {

            JsonObject spot = modelData.getJsonObject(i);

            String id = spot.getString("objectId");

            String prevId;
            String nextId;
            //Used For Fork
            String alternativeNextId;
             //Used For Merge
            String alternativePrevId;

            MovementObject prev;
            MovementObject next;
            //Only For Fork Objects
            MovementObject next2;
            //Only For Merge Objects
            MovementObject prev2;

            switch (spot.getString("type")) {

                //Standart type has prev and next objects
                case "Standart":
                    prevId = spot.getString("prevId");
                    nextId = spot.getString("nextId");

                    prev = (MovementObject) objects.get(prevId);
                    next = (MovementObject) objects.get(nextId);

                    MoveSpot moveSpot = (MoveSpot) objects.get(id);
                    moveSpot.setPrev(prev);
                    moveSpot.setNext(next);

                    sim.addObject(moveSpot);

                    break;
                case "Fork":
                    prevId = spot.getString("prevId");
                    nextId = spot.getString("nextId");
                    alternativeNextId = spot.getString("alternativeNextId");

                    prev = (MovementObject) objects.get(prevId);
                    next = (MovementObject) objects.get(nextId);
                    next2 = (MovementObject) objects.get(alternativeNextId);

                    Fork fork = (Fork) objects.get(id);
                    fork.setPrev(prev);
                    fork.setNext(next);
                    fork.setNextAlternative(next2);

                    sim.addObject(fork);

                    break;
                    
                case "Merge":
                    prevId = spot.getString("prevId");
                    nextId = spot.getString("nextId");
                    alternativePrevId = spot.getString("alternativePrevId");

                    prev = (MovementObject) objects.get(prevId);
                    next = (MovementObject) objects.get(nextId);
                    prev2 = (MovementObject) objects.get(alternativePrevId);

                    Merge merge = (Merge) objects.get(id);
                    merge.setPrev(prev);
                    merge.setNext(next);
                    merge.setPrevAlternative(prev2);

                    sim.addObject(merge);

                    break;    

                //EnterPoint has next object
                case "EnterPoint":

                    nextId = spot.getString("nextId");

                    next = (MovementObject) objects.get(nextId);

                    EnterPoint enterPoint = (EnterPoint) objects.get(id);
                    enterPoint.setNext(next);

                    sim.addObject(enterPoint);

                    break;

                //Exit point has prev object    
                case "ExitPoint":
                    prevId = spot.getString("prevId");

                    prev = (MovementObject) objects.get(prevId);

                    ExitPoint exitPoint = (ExitPoint) objects.get(id);
                    exitPoint.setPrev(prev);

                    sim.addObject(exitPoint);

                    break;
                case "TrafficLight":
                    
                    prevId = spot.getString("prevId");
                    nextId = spot.getString("nextId");
                    String stateInfo = spot.getString("state");

                    prev = (MovementObject) objects.get(prevId);
                    next = (MovementObject) objects.get(nextId);

                    TrafficLight light = (TrafficLight) objects.get(id);
                    
                    
                    if(stateInfo.equals("Green")){
                        light.setState(TrafficLight.STATE.GREEN);
                        
                    }else if(stateInfo.equals("Red")){
                        light.setState(TrafficLight.STATE.RED);
                       
                    }
                    else{
                       
                        throw new Exception("Traffic Light is Invalid");
                        
                    }
                    light.setPrev(prev);
                    light.setNext(next);

                    sim.addObject(light);
                    
                default:

                    break;
            }

        }
        System.out.println("==Simulation Model Build Final--");
    }
}
