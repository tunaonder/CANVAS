/*
 * Created by Sait Tuna Onder on 2017.03.16  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import vt.canvas.component.EnterPoint;
import vt.canvas.component.ExitPoint;
import vt.canvas.component.Fork;
import vt.canvas.component.Merge;
import vt.canvas.component.MoveSpot;
import vt.canvas.component.TrafficLight;
import vt.canvas.component.Vehicle;
import vt.canvas.component.factory.EventFactory;
import vt.canvas.component.helpers.DynamicObject;
import vt.canvas.component.helpers.StaticObject;
import vt.canvas.execution.events.TrafficLightStateChangeEvent;
import vt.canvas.execution.events.helpers.Event;
import vt.canvas.execution.events.VehicleCreateEvent;
import vt.canvas.execution.events.helpers.EventList;
import vt.canvas.messaging.MessageManager;

/**
 * This class performs CANVAS Conceptual Framework. The generated results are
 * sent to message manager for the visualization
 *
 * @author Onder
 */
public class SimulationRuntime {

    Map<String, StaticObject> staticObjects;

    // Holds References to EnterPoints
    Map<String, EnterPoint> enterPoints;
    // Holds References to TrafficLights
    Map<String, TrafficLight> trafficLights;

    // The time of the simulation
    private int simulationTime;
    // Simulation Duration
    private int simulationTimeLimit;

    private final EventList futureEventList;

    private Event earliestScheduledEvent;

    private Constants simulationConstants;

    private final EventFactory eventFactory;

    private final MessageManager messageManager;

    private final List<Vehicle> vehicles;

    // Data Collection Variables
    // These variables are used to sent results to the client
    private long sumOfVehicleDestroyTime;
    private long sumOfVehicleCreationTime;
    private int totalNumberOfVehicles;

    public SimulationRuntime(String sessionIdentifier) {

        staticObjects = new HashMap();
        enterPoints = new HashMap();
        trafficLights = new HashMap();
        vehicles = new ArrayList();

        this.simulationTime = 0;
        this.sumOfVehicleDestroyTime = 0;
        this.sumOfVehicleCreationTime = 0;
        this.totalNumberOfVehicles = 0;

        eventFactory = new EventFactory();

        futureEventList = new EventList();

        messageManager = new MessageManager(sessionIdentifier);

    }

    /**
     * All simulation constants are updated in the initialization phase of
     * CANVAS Conceptual Framework
     *
     * @param vehicleLength
     */
    public void setSimulationConstants(int vehicleLength) {
        simulationConstants = new Constants(vehicleLength);
        for (EnterPoint point : enterPoints.values()) {
            point.getFactory().setFactoryConstants(simulationConstants);
        }
    }

    /**
     * Build the static objects data structure for the runtime
     *
     * @param object
     */
    protected void addObject(StaticObject object) {

        staticObjects.put(object.getId(), object);
        if (object instanceof EnterPoint) {
            enterPoints.put(object.getId(), (EnterPoint) object);
        } else if (object instanceof TrafficLight) {
            trafficLights.put(object.getId(), (TrafficLight) object);
        }

    }

    /**
     * Called When Simulator is Initialized
     */
    protected void initializeFutureList() {

        for (EnterPoint point : enterPoints.values()) {
            futureEventList.addEvent(eventFactory.scheduleVehicleCreation(point, 0));
        }

        if (trafficLights.size() > 0) {
            for (TrafficLight trafficLight : trafficLights.values()) {
                futureEventList.addEvent(eventFactory.scheduleTrafficLightStateChange(trafficLight, 0));
            }
        }

        earliestScheduledEvent = futureEventList.pollNextEvent();

    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTimeLimit(int simulationTimeLimit) {
        // A time frame in CANVAS is 1/60 sec. Simulation duration is set by the client in terms of minutes
        // Therefore, simulation duration is equal to simulationTimeLimit * 60 * 60
        this.simulationTimeLimit = simulationTimeLimit * 60 * 60;
    }

    public synchronized void simulate() {

        while (simulationTime < simulationTimeLimit) {

            // Scan Scheduled Future Events
            processEvent();

            clockUpdate();
            updateSimulation();

        }

        // Simulation time is over, continue processing until all vehicles leave the simulation
        while (vehicles.size() > 0) {

            processEvent();
            clockUpdate();
            updateSimulation();

        }

        clockUpdate();

        // End Of The Simulation
        processEndOfSimulation();

    }

    private void clockUpdate() {
        simulationTime++;
    }

    private void processEvent() {

        // Earliest scheduled event can be null especially if vehicle creation has stopped.
        while (earliestScheduledEvent != null && simulationTime == earliestScheduledEvent.getTime()) {
            processScheduledEvent();
        }

    }

    private synchronized void processScheduledEvent() {

        // If the event is Vehicle Creation Event
        if (earliestScheduledEvent instanceof VehicleCreateEvent) {

            //Safely Cast It to VehicleCreateEvent
            VehicleCreateEvent event = (VehicleCreateEvent) earliestScheduledEvent;

            //Add The Created Vehicle To Dynamic Vehicle List
            vehicles.add(event.getVehicle());

            // Save Vehicle Creation Second for data collection purposes
            sumOfVehicleCreationTime += simulationTime / 60;
            totalNumberOfVehicles++;

            // Send Vehicle Creation Message
            messageManager.vehicleCreated(event);

            // Schedule a New Vehicle Event to add to future event list
            // Stop scheduling a future event if simulation time limit is exceeded
            if (simulationTime < simulationTimeLimit) {
                scheduleVehicleCreation(event);
            }

        } else if (earliestScheduledEvent instanceof TrafficLightStateChangeEvent) {

            TrafficLightStateChangeEvent event = (TrafficLightStateChangeEvent) earliestScheduledEvent;

            // State is already set in the beginning of the simulation
            // No need to change the state at the beginning of the simulation
            if (simulationTime != 0) {
                //Change the state of the light
                event.getLight().changeState();
            }

            // Schedule the next Event
            scheduleTrafficLightStateChange(event);

            messageManager.trafficLightStateChange(event.getLight().getId(), simulationTime);

        }

        //Get The Next Earlieast Event
        earliestScheduledEvent = futureEventList.pollNextEvent();
    }

    /**
     * Schedule a new Vehicle Creation when a new vehicle is created
     *
     * @param event
     */
    private void scheduleVehicleCreation(VehicleCreateEvent event) {
        //Get the Origin Id of processedEvent
        String originId = event.getOriginId();

        //Find the EnterPoint
        EnterPoint enterPoint = enterPoints.get(originId);

        //Create new vehicle in the EnterPoint
        futureEventList.addEvent(eventFactory.scheduleVehicleCreation(enterPoint, simulationTime));
    }

    /**
     * Schedule a new Traffic Light State Change
     *
     * @param event
     */
    private void scheduleTrafficLightStateChange(TrafficLightStateChangeEvent event) {

        TrafficLight light = event.getLight();

        //Schedule A New StateChange Event
        futureEventList.addEvent(eventFactory.scheduleTrafficLightStateChange(light, simulationTime));

    }

    private synchronized void updateSimulation() {

        List<Vehicle> vehiclesToRemove = new ArrayList<>();

        //for(Vehicle vehicle: vehicles){
        for (int i = 0; i < vehicles.size(); i++) {

            Vehicle vehicle = vehicles.get(i);

            if (canMove(vehicle)) {

                double x = vehicle.getX();
                double y = vehicle.getY();

                double targetX = vehicle.getTargetSpot().getX();
                double targetY = vehicle.getTargetSpot().getY();

                double distanceX = Math.abs(x - targetX);
                double distanceY = Math.abs(y - targetY);

                //If the Vehicle is not close to target, update its coordinates
                //Max Speed of A vehicle is the biggest possible location change in a time frame.
                if (distanceX > simulationConstants.maxSpeed || distanceY > simulationConstants.maxSpeed) {

                    x += vehicle.getTempSpeed() * Math.cos(vehicle.getRotation()) * -1;
                    y += vehicle.getTempSpeed() * Math.sin(vehicle.getRotation()) * -1;

                    vehicle.setX(x);
                    vehicle.setY(y);

                    //Check If Vehicle Left The Current Spot Completely
                    //If it left, current spot is free for other vehicles to move
                    compareVehicleAndCurrentSpot(vehicle);

                } //If the vehicle has reached to the target
                else {

                    //Update the vehicle's coordinates to the exact coordinates of the target
                    vehicle.setX(targetX);
                    vehicle.setY(targetY);

                    //Previous Spot
                    StaticObject previousSpot = vehicle.getCurrentSpot();

                    //Get The Target We Almost Reached
                    StaticObject currentSpot = vehicle.getTargetSpot();

                    //Set It as Current Spot
                    vehicle.setCurrentSpot(currentSpot);

                    if (currentSpot instanceof ExitPoint) {

                        vehiclesToRemove.add(vehicle);

                        continue;

                    } //Check if it is MoveSpot
                    else if (currentSpot instanceof MoveSpot) {
                        MoveSpot spot = (MoveSpot) currentSpot;

                        vehicle.setTargetSpot(spot.getNext());

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, spot.getNext());

                        ////
                        vehicle.getCurrentSpot().setIncomingDynamicObj(vehicle.getPrevDynamicObj());

                        //In the Next Run This Vehicle will leave the spot, so its' 
                        //vehicle.getCurrentSpot().setOccupied(false);
                        spot.setLeavingDynamicObj(vehicle);

                        //Remove the connection between two cars
                        if (vehicle.getPrevDynamicObj() != null) {
                            vehicle.getPrevDynamicObj().setNextDynamicObj(null);
                        }
                        vehicle.setPrevDynamicObj(null);
                        //Change car speed to previous speed
                        vehicle.setTempSpeed(vehicle.getSpeed());
                        //

                        messageManager.vehicleDirectionChange(vehicle, simulationTime);

                    } else if (currentSpot instanceof TrafficLight) {
                        TrafficLight spot = (TrafficLight) currentSpot;

                        vehicle.setTargetSpot(spot.getNext());

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, spot.getNext());

                        ////
                        vehicle.getCurrentSpot().setIncomingDynamicObj(vehicle.getPrevDynamicObj());

                        //In the Next Run This Vehicle will leave the spot, so its' 
                        //vehicle.getCurrentSpot().setOccupied(false);
                        spot.setLeavingDynamicObj(vehicle);

                        //Remove the connection between two cars
                        if (vehicle.getPrevDynamicObj() != null) {
                            vehicle.getPrevDynamicObj().setNextDynamicObj(null);
                        }
                        vehicle.setPrevDynamicObj(null);
                        //Change car speed to previous speed
                        vehicle.setTempSpeed(vehicle.getSpeed());

                        messageManager.vehicleDirectionChange(vehicle, simulationTime);

                    } else if (currentSpot instanceof Fork) {
                        Fork spot = (Fork) currentSpot;

                        StaticObject nextSpot;

                        Random rand = new Random();
                        int randomInt = rand.nextInt(100) + 1;
                        if (randomInt <= spot.getNewPathProbability()) {
                            nextSpot = spot.getNext();
                        } else {
                            nextSpot = spot.getNextAlternative();

                        }

                        vehicle.setTargetSpot(nextSpot);

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, nextSpot);

                        vehicle.getCurrentSpot().setIncomingDynamicObj(vehicle.getPrevDynamicObj());

                        //In the Next Run This Vehicle will leave the spot, so its' 
                        //vehicle.getCurrentSpot().setOccupied(false);
                        spot.setLeavingDynamicObj(vehicle);

                        //Remove the connection between two cars
                        if (vehicle.getPrevDynamicObj() != null) {
                            vehicle.getPrevDynamicObj().setNextDynamicObj(null);
                        }
                        vehicle.setPrevDynamicObj(null);
                        //Change car speed to previous speed
                        vehicle.setTempSpeed(vehicle.getSpeed());
                        //

                        messageManager.vehicleDirectionChange(vehicle, simulationTime);

                    } else if (currentSpot instanceof Merge) {

                        Merge spot = (Merge) currentSpot;

                        vehicle.setTargetSpot(spot.getNext());

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, spot.getNext());

                        if (vehicle == vehicle.getCurrentSpot().getIncomingDynamicObj()) {
                            spot.setIncomingDynamicObj(vehicle.getPrevDynamicObj());

                        } else {
                            spot.setIncomingDynamicObject2(vehicle.getPrevDynamicObj());

                        }

                        //In the Next Run This Vehicle will leave the spot, so its' 
//                        spot.setOccupied(false);
//                        spot.setOccupier(Merge.OccupierRoute.None);
                        spot.setLeavingDynamicObj(vehicle);

                        //Remove the connection between two cars
                        if (vehicle.getPrevDynamicObj() != null) {
                            vehicle.getPrevDynamicObj().setNextDynamicObj(null);
                        }
                        vehicle.setPrevDynamicObj(null);
                        //Change car speed to previous speed
                        vehicle.setTempSpeed(vehicle.getSpeed());
                        //

                        messageManager.vehicleDirectionChange(vehicle, simulationTime);

                    }

                    //Remove If There Is Still Connection with The Previous Spot
                    //This methold is usefull when spots are places so closed to each other
                    vehicle.removePreviosSpotConnections(previousSpot);

                }

                //Update the Vehicle In The List
                vehicles.set(i, vehicle);

            }

        }

        for (int i = 0; i < vehiclesToRemove.size(); i++) {
            removeVehicle(vehiclesToRemove.get(i));
        }

    }

    private boolean canMove(Vehicle vehicle) {

        if (vehicle.isCloseToTargetSpot(simulationConstants.vehicleToSpotDistanceLimit)) {

            StaticObject target = vehicle.getTargetSpot();
            if (target instanceof Merge) {

                Merge targetSpot = (Merge) target;

                //If the Vehicle is the first car coming from either of the two routes
                if (vehicle == targetSpot.getIncomingDynamicObj() || vehicle == targetSpot.getIncomingDynamicObject2()) {

                    if (vehicle.isSpotOccupiedByAnotherVehicle(targetSpot)) {

                        if (vehicle.getTempSpeed() != 0) {
                            vehicle.setTempSpeed(0);
                            messageManager.vehicleSpeedChange(vehicle, simulationTime);
                        }
                        return false;

                    } else {
                        if (vehicle.getTempSpeed() == 0) {
                            vehicle.setTempSpeed(vehicle.getSpeed());
                            messageManager.vehicleSpeedChange(vehicle, simulationTime);
                            return true;
                        }
                    }
                }

            } else if (target instanceof MoveSpot || target instanceof Fork) {

                //Check If This is the First Vehicle Getting Close To the Target
                if (vehicle == target.getIncomingDynamicObj()) {

                    //Check if Target Is Occupied By Another Vehicle
                    if (vehicle.isSpotOccupiedByAnotherVehicle(target)) {

                        //If this vehicle is the first one, and the target is occupied by someone else
                        //It is the leaving car
                        DynamicObject occupierVehicle = target.getLeavingDynamicObj();

                        if (occupierVehicle == null) {
                            return true;
                        }

                        if (vehicle.isVehicleClose(occupierVehicle, simulationConstants.vehicleDistanceLimit)) {

                            if (vehicle.shouldSlowDown(occupierVehicle)) {
                                //If vehicle cannot move with the same speed, change its speed
                                messageManager.vehicleSpeedChange(vehicle, simulationTime);

                            }

                        } //If the Vehicle Ahead got Far Away
                        else {
                            if (vehicle.getTempSpeed() == 0) {
                                vehicle.setTempSpeed(occupierVehicle.getTempSpeed());
                                messageManager.vehicleSpeedChange(vehicle, simulationTime);
                            }

                        }

                        return true;

                    } //If vehicle had stopped and now target is available, move
                    else {
                        if (vehicle.getTempSpeed() == 0) {
                            vehicle.setTempSpeed(vehicle.getSpeed());
                            messageManager.vehicleSpeedChange(vehicle, simulationTime);
                        }

                        return true;
                    }

                }

            } else if (target instanceof TrafficLight) {

                //Check If This is the First Vehicle Getting Close To the Target
                if (vehicle == target.getIncomingDynamicObj()) {

                    if (((TrafficLight) target).getState() == TrafficLight.STATE.RED) {
                        if (vehicle.getTempSpeed() != 0) {
                            vehicle.setTempSpeed(0);
                            messageManager.vehicleSpeedChange(vehicle, simulationTime);
                        }
                        return false;

                    } else {
                        // If spot after the traffic light is occupied do not move
                        StaticObject spotAfterTrafficLight = ((TrafficLight) target).getNext();
                        if (!spotAfterTrafficLight.getOccupierId().equals("")) {
                            if (vehicle.getTempSpeed() != 0) {
                                vehicle.setTempSpeed(0);
                                messageManager.vehicleSpeedChange(vehicle, simulationTime);
                            }
                            return false;
                        }

                    }

                    //Check if Target Is Occupied By Another Vehicle
                    if (vehicle.isSpotOccupiedByAnotherVehicle(target)) {

                        //If this vehicle is the first one, and the target is occupied by someone else
                        //It is the leaving car
                        DynamicObject occupierVehicle = target.getLeavingDynamicObj();

                        if (occupierVehicle == null) {
                            return true;
                        }

                        if (vehicle.isVehicleClose(occupierVehicle, simulationConstants.vehicleDistanceLimit)) {

                            if (vehicle.shouldSlowDown(occupierVehicle)) {
                                //If vehicle cannot move with the same speed, change its speed
                                messageManager.vehicleSpeedChange(vehicle, simulationTime);

                            }

                        } //If the Vehicle Ahead got Far Away
                        else {
                            if (vehicle.getTempSpeed() == 0) {
                                vehicle.setTempSpeed(occupierVehicle.getTempSpeed());
                                messageManager.vehicleSpeedChange(vehicle, simulationTime);
                            }

                        }

                        return true;

                    } //If vehicle had stopped and now target is available, move
                    else {
                        if (vehicle.getTempSpeed() == 0) {
                            vehicle.setTempSpeed(vehicle.getSpeed());
                            messageManager.vehicleSpeedChange(vehicle, simulationTime);
                        }

                        return true;
                    }

                }

            }
        }

        //If Vehicle has alreaddy Stopped, check if it has space to move again
        if (vehicle.getTempSpeed() == 0 && canVehicleSpeedUp(vehicle)) {
            return true;
        }

        if (!vehicle.isThereVehicleAhead()) {
            return true;
        }
        if (vehicle.canMoveWithSameSpeed(simulationConstants.vehicleDistanceLimit)) {
            return true;
        }

        //If vehicle cannot move with the same speed, change its speed        
        messageManager.vehicleSpeedChange(vehicle, simulationTime);

        return true;

    }

    /**
     * If the vehicle got far away from its last spot, do not occupy the spot
     * anymore
     *
     * @param vehicle
     */
    private void compareVehicleAndCurrentSpot(Vehicle vehicle) {
        //If the vehicle is the last vehicle leaving the current spot and if it is gone far enough
        if (vehicle.getCurrentSpot().getLeavingDynamicObj() == vehicle
                && vehicle.isFarFromSpot(simulationConstants.vehicleDistanceLimit)) {

            vehicle.getCurrentSpot().setLeavingDynamicObj(null);

            //No Occupation on the Spot anymore
            vehicle.getCurrentSpot().setOccupierId("");
        }

    }

    /**
     * Checks if the vehicle has an open space in front of it to speed up
     *
     * @param vehicle
     * @return
     */
    private boolean canVehicleSpeedUp(Vehicle vehicle) {
        
        // If vehicle has a vehicle ahead
        if (vehicle.getNextDynamicObj() == null) {
            vehicle.setTempSpeed(vehicle.getSpeed());
            messageManager.vehicleSpeedChange(vehicle, simulationTime);
            return true;
        } 
        else {
            // If vehicle ahead is not close, the vehicle can get back to its original speed
            if (!vehicle.isVehicleClose(vehicle.getNextDynamicObj(), simulationConstants.vehicleDistanceLimit)) {
                vehicle.setTempSpeed(vehicle.getSpeed());
                messageManager.vehicleSpeedChange(vehicle, simulationTime);
                return true;
            }
        }

        return false;

    }

    /**
     * Calculate the Data Collection Results and send them to the client
     */
    private void processEndOfSimulation() {

        System.out.println("Total Number OF Vehicles Created: " + totalNumberOfVehicles);
        long totalTimeSpent = sumOfVehicleDestroyTime - sumOfVehicleCreationTime;
        int averageTime = (int) (totalTimeSpent / totalNumberOfVehicles);
        System.out.println("Avarage time a vehicle spends in the traffic: " + averageTime + " seconds.");
        messageManager.endOfSimulation(totalNumberOfVehicles, averageTime, simulationTime);

    }
    
    /**
     * Remove vehicle from SimulationRuntime, and delete all of its connections
     * @param vehicle 
     */
    private void removeVehicle(Vehicle vehicle) {

        vehicle.getCurrentSpot().setOccupierId("");

        DynamicObject vehicleBehind = vehicle.getPrevDynamicObj();

        //Set The Incoming Car The Previos One
        vehicle.getCurrentSpot().setIncomingDynamicObj(vehicleBehind);

        if (vehicleBehind != null) {
            vehicleBehind.setNextDynamicObj(null);

        }

        vehicle.removePreviosSpotConnections(vehicle.getCurrentSpot());
        vehicles.remove(vehicle);
        // Save the time for data collection purposes
        sumOfVehicleDestroyTime += simulationTime / 60;

        messageManager.vehicleDestroy(vehicle.getId(), simulationTime);

    }
    
    /**
     * Request Events from the message manager This method is called by
     * Simulation Manager which is totally different than SimulationRuntime
     * thread
     */
    public void requestNewEventsToVisualize() {
        messageManager.requestNewEventsToVisualize();
    }

}
