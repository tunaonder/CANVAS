/*
 * Created by Sait Tuna Onder on 2017.04.03  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.messaging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import vt.canvas.component.Vehicle;
import vt.canvas.execution.events.VehicleCreateEvent;
import vt.canvas.messaging.helpers.Message;
import vt.canvas.messaging.helpers.MessageList;
import vt.canvas.websocket.SimulationSessionHandler;

/**
 * This class adds messages to a message list. When the length of queue gets
 * high, it sends messages to the client
 *
 * @author Onder
 */
public class MessageManager {

    private final String sessionIdentifier;

    private final int messageQueueStorageLimit = 2000;
    private final int messageCountLimitPerRequest = 1000;

    private final MessageList messageList;

    public MessageManager(String sessionIdentifier) {
        this.sessionIdentifier = sessionIdentifier;

        this.messageList = new MessageList();
    }

    public void vehicleCreated(VehicleCreateEvent event) {

        double eventTime = event.getTime();
        Vehicle vehicle = event.getVehicle();

        String vehicleId = vehicle.getId();
        double speed = vehicle.getTempSpeed();
        double length = vehicle.getLength();
        double x = vehicle.getX();
        double y = vehicle.getY();
        double targetX = vehicle.getTargetSpot().getX();
        double targetY = vehicle.getTargetSpot().getY();
        double rotation = vehicle.getRotation();

        JsonProvider provider = JsonProvider.provider();
        JsonObject createMessage = provider.createObjectBuilder()
                .add("action", "createVehicle")
                .add("time", eventTime)
                .add("vehicleId", vehicleId)
                .add("speed", speed)
                .add("length", length)
                .add("rotation", rotation)
                .add("x", x)
                .add("y", y)
                .add("targetX", targetX)
                .add("targetY", targetY)
                .build();

        Message message = new Message(createMessage, eventTime);
        messageList.addMessage(message);

    }

    public void vehicleDirectionChange(Vehicle vehicle, int simTime) {

        String vehicleId = vehicle.getId();
        double rotation = vehicle.getRotation();
        double x = vehicle.getX();
        double y = vehicle.getY();
        double speed = vehicle.getTempSpeed();

        JsonProvider provider = JsonProvider.provider();
        JsonObject directionChangeMessage = provider.createObjectBuilder()
                .add("action", "changeDirection")
                .add("time", simTime)
                .add("vehicleId", vehicleId)
                .add("rotation", rotation)
                .add("speed", speed)
                .add("x", x)
                .add("y", y)
                .build();

        Message message = new Message(directionChangeMessage, simTime);
        messageList.addMessage(message);
        
        checkMessageBuffer();
    }

    public void vehicleSpeedChange(Vehicle vehicle, int simTime) {
        
        double x = vehicle.getX();
        double y = vehicle.getY();
        String id = vehicle.getId();
        double updatedSpeed = vehicle.getTempSpeed();
        
        JsonProvider provider = JsonProvider.provider();
        JsonObject speedChangeMessage = provider.createObjectBuilder()
                .add("action", "changeSpeed")
                .add("time", simTime)
                .add("vehicleId", id)
                .add("speed", updatedSpeed)
                .add("x", x)
                .add("y", y)
                .build();

        // System.out.println("speed change at: " + simTime + "for id: " + id + " from " + vehicle.getSpeed()+ " to " + updatedSpeed) ;
        Message message = new Message(speedChangeMessage, simTime);
        messageList.addMessage(message);
    }

    public void vehicleDestroy(String id, int simTime) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject destroyMessage = provider.createObjectBuilder()
                .add("action", "vehicleDestroy")
                .add("time", simTime)
                .add("vehicleId", id)
                .build();

        //System.out.println("vehicle destroy: " + simTime + "for id: " + id);
        Message message = new Message(destroyMessage, simTime);
        messageList.addMessage(message);
    }

    public void trafficLightStateChange(String id, int simTime) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject stateChangeMessage = provider.createObjectBuilder()
                .add("action", "trafficLightStateChange")
                .add("time", simTime)
                .add("lightId", id)
                .build();

        Message message = new Message(stateChangeMessage, simTime);
        messageList.addMessage(message);
    }
    
    public void endOfSimulation(int vehicleCount, int averageTime, int simTime){
        JsonProvider provider = JsonProvider.provider();
        JsonObject endOfSimMessage = provider.createObjectBuilder()
                .add("action", "endOfSimulation")
                .add("vehicleCount", vehicleCount)
                .add("averageTime", averageTime)
                .add("time", simTime)
                .build();

        Message message = new Message(endOfSimMessage, simTime);
        messageList.addMessage(message);
        
    }

    private void checkMessageBuffer() {
        synchronized (this) {
            if (messageList.getSize() > messageQueueStorageLimit) {

                try {
                    System.out.println("Sleeping..."+sessionIdentifier);
                    this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void requestNewEventsToVisualize() {
        System.out.println("Messages are requested. Current Number Of Messages: " + messageList.getSize());

        while (messageList.getSize() == 0) {
            // Wait until simulation has built and messages are genareted
            // Message request might come eariler than first simulion results
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        synchronized (this) {
            System.out.println("Sending messages: Current number: " + messageList.getSize());

            int messageCountPerRequest = 0;
            while (!messageList.isEmpty() && messageCountPerRequest < messageCountLimitPerRequest) {
                Message message = messageList.pollNextMessage();
                messageCountPerRequest++;
                SimulationSessionHandler.sendMessageToClient(sessionIdentifier, message.getJSONObject());
            }
            System.out.println("Sent " + messageCountPerRequest + " messages.");

            System.out.println("woke up..." + sessionIdentifier);
            this.notify();
        }

    }
}
