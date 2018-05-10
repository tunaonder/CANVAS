/*
 * Created by Sait Tuna Onder on 2017.03.08  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import vt.canvas.execution.SimulationManager;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    @Inject
    private SimulationSessionHandler sessionHandler;

    /**
     * Adds a session automatically, when user is connected to the serverEndPoint
     * ServerEndPoint is "/actions". The client connection is initialized from
     * websocket.js class
     * @param session 
     */
    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
        System.out.println("connect");
        System.out.println(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
        System.out.println("delete " + session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {

        //Read the message received from the client and request simulation execution
        try (JsonReader reader = Json.createReader(new StringReader(message))) {

            JsonArray jsonArray = reader.readArray();

            // If message contains data
            if (jsonArray != null && jsonArray.size() > 0) {
                // Check the first object of the array
                JsonObject first = (JsonObject) jsonArray.get(0);

                // If there is an action key in the first object, it is not a new execution thread
                if (first.containsKey("action")) {
                    // Find the request type
                    String requestType = first.getString("action");
                    // If the request type is "requestEvents", the client is asking for new messages to visualize
                    // Find the correct simulation manager instance, and request messages from it
                    if (requestType.equals("requestEvents")) {
                        SimulationManager sim = SimulationManager.getSimulationInstance(session.getId());
                        sim.requestEventsForVisualizer();
                    }

                } // If there is no action key in JSON format, it is a new simulation request
                // Create a new Simulation Manager and request an execution
                else {
                    //Create a New Simulation Instance for this session
                    SimulationManager sim = SimulationManager.addSimulationInstance(session.getId());

                    //Request execution of the simulation
                    sim.requestExecution(jsonArray);

                }

            }

        }

    }
}
