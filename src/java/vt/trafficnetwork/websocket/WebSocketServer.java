/*
 * Created by Sait Tuna Onder on 2017.03.08  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.websocket;

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
import vt.trafficnetwork.execution.SimulationManager;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    @Inject
    private SimulationSessionHandler sessionHandler;

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

            System.out.println(jsonArray);
            if (jsonArray != null && jsonArray.size() > 0) {
                JsonObject first = (JsonObject) jsonArray.get(0);
                // If an action is requested for existing simulation
                if (first.containsKey("action")) {
                    String requestType = first.getString("action");
                    if(requestType.equals("requestEvents")){
                        SimulationManager sim = SimulationManager.getSimulationInstance(session.getId());
                        sim.requestEventsForVisualizer();
                    }
                                      
                }
                // If there is no action key in JSON format, it is new simulation request
                // Then request Execution
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
