/*
 * Created by Sait Tuna Onder on 2017.03.16  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import vt.trafficnetwork.component.EnterPoint;
import vt.trafficnetwork.component.ExitPoint;
import vt.trafficnetwork.component.Fork;
import vt.trafficnetwork.component.Merge;
import vt.trafficnetwork.component.MoveSpot;
import vt.trafficnetwork.component.TrafficLight;
import vt.trafficnetwork.component.Vehicle;
import vt.trafficnetwork.component.factory.EventFactory;
import vt.trafficnetwork.component.helpers.DynamicObject;
import vt.trafficnetwork.component.helpers.StaticObject;
import vt.trafficnetwork.execution.events.TrafficLightStateChangeEvent;
import vt.trafficnetwork.execution.events.helpers.Event;
import vt.trafficnetwork.execution.events.VehicleCreateEvent;
import vt.trafficnetwork.execution.events.helpers.EventList;
import vt.trafficnetwork.messaging.MessageManager;

/**
 *
 * @author Onder
 */
public class SimulationRuntime {

    Map<String, StaticObject> staticObjects;

    Map<String, EnterPoint> enterPoints;
    Map<String, TrafficLight> trafficLights;

    // Map<String, DynamicObject> vehicleMap;
    private int simulationTime;

    private final EventList futureEventList;

    private Event earliestScheduledEvent;

    private int simulationTimeLimit;

    private Constants simulationConstants;

    private final EventFactory eventFactory;

    private final MessageManager messageManager;

    private final List<Vehicle> vehicles;

    private long sumOfVehicleDestroyTime;

    public SimulationRuntime(String sessionIdentifier) {

        staticObjects = new HashMap();
        enterPoints = new HashMap();
        trafficLights = new HashMap();
        // vehicleMap = new HashMap();
        vehicles = new ArrayList();

        this.simulationTime = 0;
        this.sumOfVehicleDestroyTime = 0;

        //Renderer renders 60 times in a sec. Simulation duration is defined in terms of minutes
        //simulationTimeLimit = 60 * 60 * Constants.simulationDuration;
        //simulationTimeLimit = 100;
        eventFactory = new EventFactory();

        futureEventList = new EventList();

        messageManager = new MessageManager(sessionIdentifier);

    }

    public void setSimulationConstants(int vehicleLength) {
        simulationConstants = new Constants(vehicleLength);
        for (EnterPoint point : enterPoints.values()) {
            point.getFactory().setFactoryConstants(simulationConstants);
        }
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTimeLimit(int simulationTimeLimit) {
        //Renderer renders 60 times in a sec. Simulation duration is defined in terms of minutes
        this.simulationTimeLimit = simulationTimeLimit * 60 * 60;
    }

    public void simulate() {

        while (simulationTime < simulationTimeLimit) {

            // Clock update
            clockUpdate();
            // Scan Scheduled Future Events
            processEvent();
            //
            updateSimulation();

        }

        // Simulation time is over, continue processing until all vehicles leave the simulation
        while (vehicles.size() > 0) {

            // Clock update
            clockUpdate();
            // Scan Scheduled Future Events
            processEvent();
            //
            updateSimulation();

        }

        System.out.println("Total Number OF Vehicles Created: " + eventFactory.getNumberOfVehicles());

        long totalTimeSpent = sumOfVehicleDestroyTime - eventFactory.getSumOfVehicleCreationTime();
        int averageTime = (int) (totalTimeSpent / eventFactory.getNumberOfVehicles());
        System.out.println("Avarage time a vehicle spends in the traffic: " + averageTime + " seconds.");

        //Send Remaining Messages In The Buffer at The end of the Simulation
        // sendRemainingMessages();
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

    private void processScheduledEvent() {

        if (earliestScheduledEvent instanceof VehicleCreateEvent) {

            //Safely Cast It to VehicleCreateEvent
            VehicleCreateEvent event = (VehicleCreateEvent) earliestScheduledEvent;

            // Schedule a New Vehicle Event to add to future event list
            // Stop scheduling future event if end of the simulation has come
            if (simulationTime < simulationTimeLimit) {
                scheduleVehicleCreation(event);
            }

            //Add The Created Vehicle To Dynamic Vehicle List
            vehicles.add(event.getVehicle());
            //   vehicleMap.put(event.getVehicle().getId(), event.getVehicle());

            //send message
            messageManager.vehicleCreated(event);

        } else if (earliestScheduledEvent instanceof TrafficLightStateChangeEvent) {

            TrafficLightStateChangeEvent event = (TrafficLightStateChangeEvent) earliestScheduledEvent;

            // State is already set in the beginning of the simulation
            // No need to change the state
            if (simulationTime != 0) {
                //Change the state of the light
                event.getLight().changeState();
            }

            //Schedule the next Event
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

        //Find the enterPoint with its id
        EnterPoint enterPoint = enterPoints.get(originId);

        //Create new vehicle in the same location
        futureEventList.addEvent(eventFactory.scheduleVehicleCreation(enterPoint, simulationTime));
    }

    private void scheduleTrafficLightStateChange(TrafficLightStateChangeEvent event) {

        TrafficLight light = event.getLight();

        //Schedule A New StateChange Event
        futureEventList.addEvent(eventFactory.scheduleTrafficLightStateChange(light, simulationTime));

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

    private void updateSimulation() {

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

                        // vehicle.getCurrentSpot().setOccupied(false);
                        vehicle.getCurrentSpot().setOccupierId("");

                        DynamicObject vehicleBehind = vehicle.getPrevDynamicObj();

                        //Set The Incoming Car The Previos One
                        vehicle.getCurrentSpot().setIncomingDynamicObj(vehicleBehind);

                        if (vehicleBehind != null) {
                            vehicleBehind.setNextDynamicObj(null);

                        }

                        vehicle.removePreviosSpotConnections(previousSpot);
                        vehicles.remove(i);
                        sumOfVehicleDestroyTime += simulationTime / 60;

                        messageManager.vehicleDestroy(vehicle.getId(), simulationTime);

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
                        //

                        messageManager.vehicleDirectionChange(vehicle, simulationTime);

                    } else if (currentSpot instanceof Fork) {
                        Fork spot = (Fork) currentSpot;

                        StaticObject nextSpot;

                        Random rand = new Random();
                        int randomInt = rand.nextInt(2);
                        if (randomInt == 0) {
                            nextSpot = spot.getNext();
                        } else {
                            nextSpot = spot.getNextAlternative();

                        }

                        vehicle.setTargetSpot(nextSpot);

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, nextSpot);

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

    }

    private boolean canMove(Vehicle vehicle) {

        if (vehicle.isCloseToTargetSpot(simulationConstants.vehicleToSpotDistanceLimit)) {

            StaticObject target = vehicle.getTargetSpot();
            if (target instanceof Merge) {

                Merge targetSpot = (Merge) target;

                //If the Vehicle is the first car coming from either of the two routes
                if (vehicle == targetSpot.getIncomingDynamicObj() || vehicle == targetSpot.getIncomingDynamicObject2()) {

                    if (vehicle.isSpotOccupiedByAnotherVehicle(targetSpot)) {

                        //This is Cannot be Null because of the test
                        String occupierId = targetSpot.getOccupierId();

                        DynamicObject leavingVehicle = target.getLeavingDynamicObj();

                        //Occupier Already Passed the Target
                        if (leavingVehicle != null && occupierId.equals(leavingVehicle.getId())) {

                            if (vehicle.isVehicleClose(leavingVehicle, simulationConstants.vehicleDistanceLimit)) {

                                if (vehicle.shouldSlowDown(leavingVehicle)) {
                                    //If vehicle cannot move with the same speed, change its speed
                                    messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
                                    return true;
                                }

                            }
                            return true;

                        } //Occupier is The vehicle from the other route
                        else {
                            //If vehicle is still moving, stop.
                            if (vehicle.getTempSpeed() != 0) {
                                vehicle.setTempSpeed(0);
                                messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
                            }
                            return false;

                        }

                    } else {
                        if (vehicle.getTempSpeed() == 0) {
                            vehicle.setTempSpeed(vehicle.getSpeed());
                            messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
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
                                messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);

                            }

                        } //If the Vehicle Ahead got Far Away
                        else {
                            if (vehicle.getTempSpeed() == 0) {
                                vehicle.setTempSpeed(occupierVehicle.getTempSpeed());
                                messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
                            }

                        }

                        return true;

                    } //If vehicle had stopped and now target is available, move
                    else {
                        if (vehicle.getTempSpeed() == 0) {
                            vehicle.setTempSpeed(vehicle.getSpeed());
                            messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
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
                            messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
                        }
                        return false;

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
                                messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);

                            }

                        } //If the Vehicle Ahead got Far Away
                        else {
                            if (vehicle.getTempSpeed() == 0) {
                                vehicle.setTempSpeed(occupierVehicle.getTempSpeed());
                                messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
                            }

                        }

                        return true;

                    } //If vehicle had stopped and now target is available, move
                    else {
                        if (vehicle.getTempSpeed() == 0) {
                            vehicle.setTempSpeed(vehicle.getSpeed());
                            messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
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
        //   System.out.println(simulationTime);
        if (vehicle.canMoveWithSameSpeed(simulationConstants.vehicleDistanceLimit)) {
            //System.out.println(vehicle.getId() + " new speed " + vehicle.getTempSpeed());
            return true;
        }
        //System.out.println(vehicle.getId() + " new speed " + vehicle.getTempSpeed());

        //If vehicle cannot move with the same speed, change its speed        
        messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);

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
        if (vehicle.getCurrentSpot().getLeavingDynamicObj() == vehicle && vehicle.isFarFromSpot()) {

            vehicle.getCurrentSpot().setLeavingDynamicObj(null);

            //No Occupation on the Spot anymore
            vehicle.getCurrentSpot().setOccupierId("");

        }

    }

    /**
     * Checks if the vehicle has open space in front of it to speed up its
     * original speed
     *
     * @param vehicle
     * @return
     */
    private boolean canVehicleSpeedUp(Vehicle vehicle) {

        if (vehicle.getNextDynamicObj() == null) {
            vehicle.setTempSpeed(vehicle.getSpeed());
            messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
            return true;
        } else {
            if (!vehicle.isVehicleClose(vehicle.getNextDynamicObj(), simulationConstants.vehicleDistanceLimit)) {
                vehicle.setTempSpeed(vehicle.getSpeed());
                messageManager.vehicleSpeedChange(vehicle, vehicle.getId(), vehicle.getTempSpeed(), simulationTime);
                return true;

            }

        }

        return false;

    }

    public void requestNewEventsToVisualize() {
        messageManager.requestNewEventsToVisualize();
    }

}
