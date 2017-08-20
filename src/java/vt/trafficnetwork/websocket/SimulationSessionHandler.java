/*
 * Created by Sait Tuna Onder on 2017.03.08  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Onder
 */


@ApplicationScoped
public class SimulationSessionHandler {

    //All Sessions are kept in this Map
    private static final Map<String, Session> sessions = new HashMap<>();

    public void addSession(Session session) {
        //sessions.add(session);
        sessions.put(session.getId(), session);
        System.out.println("session is added");

    }

    public void removeSession(Session session) {
        //sessions.remove(session);
        sessions.remove(session.getId());
    }

//    private void sendToAllConnectedSessions(JsonObject message) {
//
//        
//        for (Session session: sessions.values()){
//            
//            sendToSession(session, message);
//     
//        }
//    }

    private static void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session.getId());
            Logger.getLogger(SimulationSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sends Message to a specific session
     * @param sessionIdentifier
     * @param message 
     */
    public static void sendMessageToClient(String sessionIdentifier, JsonObject message){
        
       
        sendToSession(sessions.get(sessionIdentifier), message);
        
    }

}