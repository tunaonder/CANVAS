/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component;

import vt.canvas.component.helpers.DynamicObject;
import vt.canvas.component.helpers.StaticObject;

/**
 * Vehicle class provides many functions that investigates the connections
 * between the vehicle and other objects. These functions are called from
 * SimulationRuntime.java class that runs the CANVAS Conceptual Framework
 *
 * @author Onder
 */
public class Vehicle extends DynamicObject {

    public Vehicle(String id, double x, double y, double speed, StaticObject current, StaticObject target, double length) {

        super(id, x, y, speed, current, target, length);

    }

    /**
     *
     * @param targetSpot
     * @return false if target is occupied by another vehicle
     */
    public boolean isSpotOccupiedByAnotherVehicle(StaticObject targetSpot) {

        if (targetSpot.getOccupierId().equals("")) {
            targetSpot.setOccupierId(this.getId());
            return false;
        } else if (targetSpot.getOccupierId().equals(this.getId())) {
            return false;
        }

        return true;
    }

    /**
     * Checks if there is a vehicle ahead of this car until the target spot
     * Creates new connections if the vehicle just started moving on the new
     * path
     *
     * @return
     */
    public boolean isThereVehicleAhead() {

        // There is a vehicle ahead if the dynamic object is not null
        if (this.getNextDynamicObj() != null) {
            return true;
        }

        StaticObject targetSpot = this.getTargetSpot();

        // Get The First Car Moving to this target spot
        DynamicObject vehicleAhead;

        // Check If the next spot is merge and this vehicle is on the alternative route
        boolean isSpotMergeAlternative = targetSpot instanceof Merge
                && this.getCurrentSpot() == ((Merge) targetSpot).getPrevAlternative();

        //If it is, get the alternative vehicle
        if (isSpotMergeAlternative) {
            vehicleAhead = ((Merge) targetSpot).getIncomingDynamicObject2();

        } else {
            vehicleAhead = targetSpot.getIncomingDynamicObj();
        }

        // If the first car is this car, return false(there is no vehicle ahead)
        if (vehicleAhead == this) {
            return false;
        }
        // If there is no car ahead, set this car as the first incoming car
        if (vehicleAhead == null) {

            if (isSpotMergeAlternative) {
                ((Merge) targetSpot).setIncomingDynamicObject2(this);
            } else {
                targetSpot.setIncomingDynamicObj(this);
            }

            return false;
        }

        // If there are vehicles moving to the target spot, find the last one
        while (vehicleAhead.getPrevDynamicObj() != null && vehicleAhead.getPrevDynamicObj() != this) {
            vehicleAhead = vehicleAhead.getPrevDynamicObj();
        }

        // Set the connection between the vehicle and the vehicle ahead
        vehicleAhead.setPrevDynamicObj(this);
        this.setNextDynamicObj(vehicleAhead);

        return true;

    }

    /**
     * Checks the distance between the vehicle and the vehicle ahead Updates the
     * speed of the vehicle, if vehicle ahead is close and slower
     *
     * @param vehicleDistanceLimit
     * @return
     */
    public boolean canMoveWithSameSpeed(int vehicleDistanceLimit) {

        double distance = calculateDistance(this.getX(), this.getY(),
                this.getNextDynamicObj().getX(), this.getNextDynamicObj().getY());

        return isDistanceEnough(this.getNextDynamicObj(), distance, vehicleDistanceLimit);

    }

    /**
     * Calculates the distance with this vehicle and the next one and checks the
     * distance
     *
     * @param vehicleAhead
     * @param vehicleDistanceLimit
     * @return
     */
    public boolean canMoveWithTheSameSpeed(DynamicObject vehicleAhead, int vehicleDistanceLimit) {

        double distance = calculateDistance(this.getX(), this.getY(),
                vehicleAhead.getX(), vehicleAhead.getY());

        return isDistanceEnough(vehicleAhead, distance, vehicleDistanceLimit);

    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return Distance between two coordinates
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {

        double xDistance = Math.abs(x1 - x2);
        double yDistance = Math.abs(y1 - y2);

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);

    }

    /**
     * Updates the vehicle speed, if this vehicle is close to the vehicle ahead
     * and the vehicle ahead is slower
     *
     * @param vehicleAhead
     * @param distance
     * @param vehicleDistanceLimit
     * @return
     */
    private boolean isDistanceEnough(DynamicObject vehicleAhead, double distance, int vehicleDistanceLimit) {

        double length1 = this.getLength() / 2;
        double length2 = vehicleAhead.getLength() / 2;

        //If there is enough distance between two vehicles return true
        if (distance > length1 + length2 + vehicleDistanceLimit) {
            return true;
        }

        //If the vehicle ahead is slower, change speed of this vehicle
        if (vehicleAhead.getTempSpeed() < this.getTempSpeed()) {

            this.setTempSpeed(vehicleAhead.getTempSpeed());
            return false;
        }

        return true;

    }

    /**
     * Checks if the vehicle got far away from its current spot
     *
     * @param vehicleDistanceLimit
     * @return
     */
    public boolean isFarFromSpot(int vehicleDistanceLimit) {

        double distance = calculateDistance(this.getX(), this.getY(),
                this.getCurrentSpot().getX(), this.getCurrentSpot().getY());

        return distance > this.getLength() * 0.5 + vehicleDistanceLimit;

    }

    /**
     * Checks the distance between vehicle and its target spot
     *
     * @param vehicleToSpotDistanceLimit
     * @return
     */
    public boolean isCloseToTargetSpot(int vehicleToSpotDistanceLimit) {

        //Calculate the Distance Between the vehicle and its' target spot
        double distance = calculateDistance(this.getX(), this.getY(),
                this.getTargetSpot().getX(), this.getTargetSpot().getY());

        return distance < vehicleToSpotDistanceLimit + this.getLength() / 2;

    }

    /**
     * Checks the distance between two vehicle
     *
     * @param vehicle
     * @param vehicleDistanceLimit
     * @return true if the distance between two vehicle is shorter than the
     * vehicle distance limit
     */
    public boolean isVehicleClose(DynamicObject vehicle, int vehicleDistanceLimit) {
        double distance = calculateDistance(this.getX(), this.getY(),
                vehicle.getX(), vehicle.getY());

        return distance < vehicleDistanceLimit + this.getLength() / 2 + vehicle.getLength() / 2;

    }

    /**
     * Compare the speed of two vehicles.
     *
     * @param vehicleAhead
     * @return true if the vehicle ahead is slower than this vehicle
     */
    public boolean shouldSlowDown(DynamicObject vehicleAhead) {

        // If the vehicle ahead is slower, change speed of this vehicle
        if (vehicleAhead.getTempSpeed() < this.getTempSpeed()) {
            this.setTempSpeed(vehicleAhead.getTempSpeed());
            return true;
        }
        return false;
    }

    /**
     * Removes The all connections between the vehicle and its previous spot
     *
     * @param previousSpot
     */
    public void removePreviosSpotConnections(StaticObject previousSpot) {

        if (previousSpot.getLeavingDynamicObj() == this) {
            previousSpot.setLeavingDynamicObj(null);
        }
        if (previousSpot.getOccupierId().equals(this.getId())) {
            previousSpot.setOccupierId("");
        }
    }

    /**
     * If the vehicle got far away from its last spot, do not occupy the spot
     * anymore
     *
     * @param vehicle
     */
    public void compareVehicleAndCurrentSpot(int vehicleDistanceLimit) {
        //If the vehicle is the last vehicle leaving the current spot and if it is gone far enough
        if (this.getCurrentSpot().getLeavingDynamicObj() == this
                && this.isFarFromSpot(vehicleDistanceLimit)) {

            this.getCurrentSpot().setLeavingDynamicObj(null);

            //No Occupation on the Spot anymore
            this.getCurrentSpot().setOccupierId("");
        }
    }

    /**
     * Checks if the vehicle has an open space in front of it to speed up
     *
     * @param vehicle
     * @return
     */
    public boolean canVehicleSpeedUp(int vehicleDistanceLimit) {

        // If vehicle has a vehicle ahead
        if (this.getNextDynamicObj() == null) {
            this.setTempSpeed(this.getSpeed());
            return true;
        } else {
            // If vehicle ahead is not close, the vehicle can get back to its original speed
            if (!this.isVehicleClose(this.getNextDynamicObj(), vehicleDistanceLimit)) {
                this.setTempSpeed(this.getSpeed());
                return true;
            }
        }

        return false;
    }

}
