/*
 * Created by Sait Tuna Onder on 2017.03.16  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
    
    // This map holds the visited vehicle ids for each static object id
    Map<String, Set<String>> visitLogs;
    // This map holds the last visited vehicle id for each static object id
    Map<String, String> lastVisitedVehicleLogs;

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
        visitLogs = new HashMap();
        lastVisitedVehicleLogs = new HashMap<>();

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
        
        // Vehicle creation event is scheduled for each enter point
        for (EnterPoint point : enterPoints.values()) {
            futureEventList.addEvent(eventFactory.scheduleVehicleCreation(point, 0));
        }
        
        // Traffic light state change event is scheduled for each traffic light
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

    public void simulate() {
        
        // Run the simulation until the end of the simulation duration
        while (simulationTime < simulationTimeLimit) {

            // Scan Scheduled Future Events
            processEvent();
            clockUpdate();
            updateSimulation();

        }

        // Simulation time is over, continue processing until all vehicles leave the simulation
        // Enter Points stop generating vehicles after simulation duration is execeded
        while (vehicles.size() > 0) {

            processEvent();
            clockUpdate();
            updateSimulation();

        }
        
        // Update the simulation time one last time before sending the end of simulation message
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

    private void processScheduledEvent() {

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

    /**
     * All vehicles are updated according to their speed and directions
     * in every time frame until the end of the simulation
     */
    private void updateSimulation() {

        List<Vehicle> vehiclesToRemove = new ArrayList<>();

        // Update each vehicle
        for (int i = 0; i < vehicles.size(); i++) {

            Vehicle vehicle = vehicles.get(i);

            // Check if vehicle can update its coordinate
            if (canMove(vehicle)) {

                double x = vehicle.getX();
                double y = vehicle.getY();

                double targetX = vehicle.getTargetSpot().getX();
                double targetY = vehicle.getTargetSpot().getY();

                double distanceX = Math.abs(x - targetX);
                double distanceY = Math.abs(y - targetY);

                //If the Vehicle is not close to target, update its coordinates
                //Max Speed of the vehicle is the biggest possible location change in a time frame.
                if (distanceX > simulationConstants.maxSpeed || distanceY > simulationConstants.maxSpeed) {

                    // Update the vehicle's coordinates
                    x += vehicle.getTempSpeed() * Math.cos(vehicle.getRotation()) * -1;
                    y += vehicle.getTempSpeed() * Math.sin(vehicle.getRotation()) * -1;
                    vehicle.setX(x);
                    vehicle.setY(y);

                    //Check If Vehicle Left The Current Spot Completely
                    //If it left, current spot is free for other vehicles to move
                    vehicle.compareVehicleAndCurrentSpot(simulationConstants.vehicleDistanceLimit, visitLogs, lastVisitedVehicleLogs);

                } //If the vehicle has reached to the target
                else {

                    // There might be very small difference between the vehicle's coordinate and the target's coordinates
                    // Update the vehicle's coordinates to the exact coordinates of the target
                    vehicle.setX(targetX);
                    vehicle.setY(targetY);                

                    // Previous Spot
                    StaticObject previousSpot = vehicle.getCurrentSpot();         
                    
                    // *************************
                    // If statements in this code block are rarely returns true
                    // They return true in case two static objects are placed very close
                    // and the vehicle arrived the second static object before removing
                    // the connections with the previous static object
                    // If it happens, this block makes sure that, all connections with
                    // the previous spot are removed
                    
                    if (previousSpot.getLeavingDynamicObj() == vehicle || previousSpot.getOccupierId().equals(vehicle.getId())) {
                        Set<String> set = visitLogs.get(previousSpot.getId());
                        if (set == null) {
                            set = new HashSet<String>();
                        }
                        set.add(vehicle.getId());
                        visitLogs.put(previousSpot.getId(), set);
                        lastVisitedVehicleLogs.put(previousSpot.getId(), vehicle.getId());
                    }
                    
                    if (previousSpot.getLeavingDynamicObj() == vehicle) {
                        previousSpot.setLeavingDynamicObj(null);
                    }
                    if (previousSpot.getOccupierId().equals(vehicle.getId())) {
                        previousSpot.setOccupierId("");

                    }
                    // **************************  
                    
                    // Get The Target We Almost Reached
                    StaticObject currentSpot = vehicle.getTargetSpot();

                    // Set It as Current Spot
                    vehicle.setCurrentSpot(currentSpot);

                    // If vehicle has reached to an ExitPoint, add it to the removal list
                    if (currentSpot instanceof ExitPoint) {

                        vehiclesToRemove.add(vehicle);

                        continue;

                    } else if (currentSpot instanceof MoveSpot) {

                        MoveSpot spot = (MoveSpot) currentSpot;

                        vehicle.setTargetSpot(spot.getNext());

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, spot.getNext());

                        // Vehicle is not the incoming dynamic object anymore.
                        // Make the previous dynamic object new IncomingDynamicObj
                        vehicle.getCurrentSpot().setIncomingDynamicObj(vehicle.getPrevDynamicObj());

                        // Vehicle is the leaving vehicle now
                        spot.setLeavingDynamicObj(vehicle);

                    } else if (currentSpot instanceof TrafficLight) {

                        TrafficLight spot = (TrafficLight) currentSpot;

                        vehicle.setTargetSpot(spot.getNext());

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, spot.getNext());

                        // Vehicle is not the incoming dynamic object anymore.
                        // Make the previous dynamic object new IncomingDynamicObj
                        vehicle.getCurrentSpot().setIncomingDynamicObj(vehicle.getPrevDynamicObj());

                        // Vehicle is the leaving vehicle now
                        spot.setLeavingDynamicObj(vehicle);

                    } else if (currentSpot instanceof Fork) {

                        Fork spot = (Fork) currentSpot;

                        StaticObject nextSpot;

                        // Find the next target spot according to selection probability
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

                        // Vehicle is the leaving vehicle now
                        spot.setLeavingDynamicObj(vehicle);

                    } else if (currentSpot instanceof Merge) {

                        Merge spot = (Merge) currentSpot;

                        vehicle.setTargetSpot(spot.getNext());

                        //Set New Rotation Of The Car According To Current and Target Spot
                        vehicle.setRotation(spot, spot.getNext());

                        // Set the new incoming object of the merge spot
                        // If the vehicle is coming from path 1, update path 1 incoming dynamic object
                        // Otherwise, update path 2 incoming dynamic object
                        if (vehicle == vehicle.getCurrentSpot().getIncomingDynamicObj()) {
                            spot.setIncomingDynamicObj(vehicle.getPrevDynamicObj());

                        } else {
                            spot.setIncomingDynamicObject2(vehicle.getPrevDynamicObj());

                        }

                        // Vehicle is the leaving vehicle now
                        spot.setLeavingDynamicObj(vehicle);

                    }

                    // Remove the connections between two vehicles
                    if (vehicle.getPrevDynamicObj() != null) {
                        vehicle.getPrevDynamicObj().setNextDynamicObj(null);
                    }
                    vehicle.setPrevDynamicObj(null);

                    //Change car speed to previous speed
                    vehicle.setTempSpeed(vehicle.getSpeed());

                    messageManager.vehicleDirectionChange(vehicle, simulationTime);

                    // Remove connections with the previous spot
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

    /**
     * This method checks if a vehicle can move. Vehicle's speed is updated if
     * required
     *
     * @param vehicle
     * @return false if vehicle speed is reduced to zero
     */
    private boolean canMove(Vehicle vehicle) {

        // Check if vehicle is close to its target
        if (vehicle.isCloseToTargetSpot(simulationConstants.vehicleToSpotDistanceLimit)) {

            StaticObject target = vehicle.getTargetSpot();
            // If the target is a Merge
            if (target instanceof Merge) {

                Merge targetSpot = (Merge) target;

                // Check if the vehicle is the first vehicle coming towards the target from one of the previous static objects
                if (vehicle == targetSpot.getIncomingDynamicObj() || vehicle == targetSpot.getIncomingDynamicObject2()) {

                    // If the merge is occuiped by another vehicle
                    if (vehicle.isSpotOccupiedByAnotherVehicle(targetSpot)) {

                        // If vehicle is moving, stop
                        if (vehicle.getTempSpeed() != 0) {
                            vehicle.setTempSpeed(0);
                            messageManager.vehicleSpeedChange(vehicle, simulationTime);
                        }
                        // If it is already stopped, return false
                        return false;

                    } else {
                        // If the merge is not occupied anymore and the vehicle is waiting, start moving with its original speed
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

                        // If this vehicle is the first one, and the target is occupied by someone else
                        // The other vehicle is the leaving vehicle
                        DynamicObject occupierVehicle = target.getLeavingDynamicObj();

                        // If there is not an occupier, move with the same speed
                        if (occupierVehicle == null) {
                            return true;
                        }

                        // Chech if the distance between vehicles is less than the limit
                        if (vehicle.isVehicleClose(occupierVehicle, simulationConstants.vehicleDistanceLimit)) {

                            // If vehicle cannot move with the same speed, change its speed
                            if (vehicle.shouldSlowDown(occupierVehicle)) {
                                messageManager.vehicleSpeedChange(vehicle, simulationTime);

                            }

                        } //If the ahead is not close
                        else {
                            // if vehicle has stopped, it can start moving with its original speed
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
                        // Vehicle movement might not be allowed even though TrafficLight state is green
                        // This is required because, we prefer to not have vehicles within traffic intersections
                        // when there is a traffic jam. If a vehicle gets into the intersection and cannot move
                        // vehicles that are on other paths might overlap with the vehicle
                        
                        // If the spot after the traffic light is occupied do not move
                        // This block makes sure that, there are no vehicles within the intersection
                        StaticObject spotAfterTrafficLight = ((TrafficLight) target).getNext();
                        
                        // If target is occupied do not move
                        if (!spotAfterTrafficLight.getOccupierId().equals("")) {
                            if (vehicle.getTempSpeed() != 0) {
                                vehicle.setTempSpeed(0);
                                messageManager.vehicleSpeedChange(vehicle, simulationTime);
                                return false;
                            }
                        }
                        
                        // If there is a fork just after the traffic light, we must check the next two objects
                        // The reaon is that forks are placed very close to traffic lights in some cases
                        // to provide right turns after the traffic lights
                        // Therefore, the points after the fork are the points on the other side of the intersection
                        if (spotAfterTrafficLight instanceof Fork) {
                            // This statement checks if fork object is places very close to the traffic light
                            if (target.calculateDistance(target.getX(), target.getY(), spotAfterTrafficLight.getX(), 
                                    spotAfterTrafficLight.getY()) < simulationConstants.vehicleLength1 * 3) {

                                Fork fork = (Fork) spotAfterTrafficLight;

                                StaticObject next1 = fork.getNext();
                                StaticObject next2 = fork.getNextAlternative();

                                if (!next1.getOccupierId().equals("") || !next2.getOccupierId().equals("")) {
                                    if (vehicle.getTempSpeed() != 0) {
                                        vehicle.setTempSpeed(0);
                                        messageManager.vehicleSpeedChange(vehicle, simulationTime);
                                        return false;

                                    }
                                }
                            }
                        }
                                                                            
                        
                        // Check the visit logs
                        // If the vehicle ahead did not arrive to the target do not move
                        // This code block makes sure that, a vehicle cannot move before the vehicle ahead arrives to the target
                        // Same Fork issue described above is also valid for this case                        
                        Set<String> visitSet = visitLogs.get(target.getId());
                        Set<String> aheadObjectVisitSet = visitLogs.get(spotAfterTrafficLight.getId());
                        
                        String lastVehicleId = lastVisitedVehicleLogs.get(target.getId());
                        if (visitSet != null && lastVehicleId != null && !lastVehicleId.equals("")) {

                            if (aheadObjectVisitSet != null && !aheadObjectVisitSet.contains(lastVehicleId)) {
                                if (vehicle.getTempSpeed() != 0) {
                                    vehicle.setTempSpeed(0);
                                    messageManager.vehicleSpeedChange(vehicle, simulationTime);
                                }
                                return false;
                            }
                                                        
                            if (spotAfterTrafficLight instanceof Fork) {
                                // This statement checks if fork object is places very close to the traffic light
                                if (target.calculateDistance(target.getX(), target.getY(), spotAfterTrafficLight.getX(),
                                        spotAfterTrafficLight.getY()) < simulationConstants.vehicleLength1 * 3) {
                                    Fork fork = (Fork) spotAfterTrafficLight;

                                    StaticObject nextOfFork1 = fork.getNext();
                                    StaticObject nextOfFork2 = fork.getNextAlternative();

                                    Set<String> set1 = visitLogs.get(nextOfFork1.getId());
                                    Set<String> set2 = visitLogs.get(nextOfFork2.getId());

                                    boolean vehicleArrived1 = false;
                                    boolean vehicleArrived2 = false;

                                    if (set1 != null && set1.contains(lastVehicleId)) {
                                        vehicleArrived1 = true;
                                    }

                                    if (set2 != null && set2.contains(lastVehicleId)) {
                                        vehicleArrived2 = true;
                                    }

                                    if (!vehicleArrived1 && !vehicleArrived2) {
                                        if (vehicle.getTempSpeed() != 0) {
                                            vehicle.setTempSpeed(0);
                                            messageManager.vehicleSpeedChange(vehicle, simulationTime);
                                        }
                                        return false;

                                    }
                                }
                            }
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
        if (vehicle.getTempSpeed() == 0 && vehicle.canVehicleSpeedUp(simulationConstants.vehicleDistanceLimit)) {
            messageManager.vehicleSpeedChange(vehicle, simulationTime);
            return true;
        }

        // Check If vehicle has a close vehicle ahead
        if (vehicle.isThereVehicleAhead()) {
            if (!vehicle.canMoveWithSameSpeed(simulationConstants.vehicleDistanceLimit)) {
                messageManager.vehicleSpeedChange(vehicle, simulationTime);
            }

        }

        // Vehicle can move with the same speed
        return true;
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
     *
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
     * Request Events from the message manager. This method is called by a new thread
     * which is totally different than SimulationRuntime(Main Execution) thread
     */
    public void requestNewEventsToVisualize() {
        messageManager.requestNewEventsToVisualize();
    }

}
