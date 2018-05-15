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
 * This class adds messages to a message list. The messages are sent the client
 * when they are requested by the client
 *
 * @author Onder
 */
public class MessageManager {

    private final String sessionIdentifier;

    private final int messageQueueStorageLimit = 2000;
    private final int messageCountLimitPerRequest = 1000;
    private int totalEventCount = 0;

    private final MessageList messageList;

    // Each client has a Message Manager instance
    public MessageManager(String sessionIdentifier) {
        this.sessionIdentifier = sessionIdentifier;
        this.messageList = new MessageList();
    }

    /**
     * Add a VehicleCreateEvent to the messageList
     * @param event 
     */
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

    /**
     * Add a VehicleDirectionChange event to the messageList
     * @param vehicle
     * @param simTime 
     */
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
        
        // Check Number of Messages in the buffer
        checkMessageBuffer();
    }
    
    /**
     * Add VehicleSpeedChange event to the messageList
     * @param vehicle
     * @param simTime 
     */
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

        Message message = new Message(speedChangeMessage, simTime);
        messageList.addMessage(message);
    }
    
    /**
     * Add a vehicleRemoval event to the messageList
     * @param id
     * @param simTime 
     */
    public void vehicleDestroy(String id, int simTime) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject destroyMessage = provider.createObjectBuilder()
                .add("action", "vehicleDestroy")
                .add("time", simTime)
                .add("vehicleId", id)
                .build();

        Message message = new Message(destroyMessage, simTime);
        messageList.addMessage(message);
    }

    /**
     * Add a traffic light state change event to the messageList
     * @param id
     * @param simTime 
     */
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
    
    /**
     * Add end of the simulation message to the messageList
     * @param vehicleCount
     * @param averageTime
     * @param simTime 
     */
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
    
    /**
     * Pause the execution thread if enough number of messages are created
     * Otherwise, thread will continue running
     */
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

    /**
     * This method is called by the client when more messages are required on
     * the client
     */
    public void requestNewEventsToVisualize() {
        System.out.println("Messages are requested. Current Number Of Messages: " + messageList.getSize());

        // Wait until simulation has built and messages are genareted
        // Message request might come eariler than first simulion results
        while (messageList.getSize() == 0) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Wait for additional time in case more messages are currently being created
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        synchronized (this) {
            System.out.println("Sending messages: Current number: " + messageList.getSize());

            int messageCountPerRequest = 0;

            // Send number of messages
            // If there are less messages than required, send all of them
            while (!messageList.isEmpty() && messageCountPerRequest < messageCountLimitPerRequest) {
                Message message = messageList.pollNextMessage();
                messageCountPerRequest++;
                SimulationSessionHandler.sendMessageToClient(sessionIdentifier, message.getJSONObject());
                totalEventCount++;
            }

            System.out.println("Sent " + messageCountPerRequest + " messages.");

            // Notify the main execution thread to generate more messages
            System.out.println("woke up..." + sessionIdentifier);
            this.notify();
            System.out.println("TOTAL EVENTS GENERATED SO FAR: " + totalEventCount);
        }

    }
}
