/*
 * Created by Sait Tuna Onder on 2017.04.03  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.messaging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import vt.trafficnetwork.component.Vehicle;
import vt.trafficnetwork.execution.events.VehicleCreateEvent;
import vt.trafficnetwork.messaging.helpers.Message;
import vt.trafficnetwork.messaging.helpers.MessageList;
import vt.trafficnetwork.websocket.SimulationSessionHandler;

/**
 * This class adds messages to a message list. When the length of queue gets
 * high, it sends messages to the client
 *
 * @author Onder
 */
public class MessageManager {

    private final String sessionIdentifier;
    private int messageCount;

    private final int messageQueueStorageLimit = 500;
    private final int messageCountLimitPerRequest = 100;

    private final MessageList messageList;

    public MessageManager(String sessionIdentifier) {
        messageCount = 0;
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

        checkMessageBuffer();

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

        //    System.out.println("direction change at: " + simTime);
        Message message = new Message(directionChangeMessage, simTime);
        messageList.addMessage(message);
    }

    public void vehicleSpeedChange(Vehicle vehicle, String id, double updatedSpeed, int simTime) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject speedChangeMessage = provider.createObjectBuilder()
                .add("action", "changeSpeed")
                .add("time", simTime)
                .add("vehicleId", id)
                .add("speed", updatedSpeed)
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
        System.out.println("Messages are requested. Remaining Messages: " + messageList.getSize());

        while (messageList.getSize() == 0) {
            // Wait until simulation has built and messages are genareted
            // Message request might come eariler than first simulion results
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Sending new batch of messages. Current number: " + messageList.getSize());

        int messageCountPerRequest = 0;
        while (!messageList.isEmpty() && messageCountPerRequest < messageCountLimitPerRequest) {
            Message message = messageList.pollNextMessage();
            messageCount++;
            messageCountPerRequest++;
            SimulationSessionHandler.sendMessageToClient(sessionIdentifier, message.getJSONObject());
        }

        synchronized (this) {
            System.out.println("woke up..." + sessionIdentifier);
            this.notify();           
        }

    }
}
