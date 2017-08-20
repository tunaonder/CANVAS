/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.DynamicObject;
import vt.trafficnetwork.component.helpers.MovementObject;
import vt.trafficnetwork.execution.Constants;

/**
 *
 * @author Onder
 */
public class Vehicle extends DynamicObject {

    /**
     * Defines the length of the vehicle
     */
    private double rotation;

    public Vehicle(String id, double x, double y, double speed, MovementObject current, MovementObject target, double length) {

        super(id, x, y, speed, current, target, length);

        this.rotation = calculateRotation(x, y, target.getX(), target.getY());

    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    
    /**
     * This Method Calculates The rotation of the car according to current and target coordinates
     * @param x
     * @param y
     * @param targetX
     * @param targetY
     * @return 
     */
    private double calculateRotation(double x, double y, double targetX, double targetY) {

        double difX = targetX - x;
        double difY = targetY - y;

        //Define New Rotation
        double rot = Math.atan(difY / difX);

        if (difX == 0) {
            rot += Math.PI;
        } else if (difX > 0 && difY == 0) {
            rot += Math.PI;
        } else if (difY > 0 && difX > 0) {
            rot += Math.PI;
        } else if (difY < 0 && difX > 0) {
            rot += Math.PI;
        }

        return rot;

    }
    
    /**
     * Calculate and Set the Rotation
     * @param o1
     * @param o2 
     */
    public void setRotation(MovementObject o1, MovementObject o2) {

        double rot = calculateRotation(o1.getX(), o1.getY(), o2.getX(), o2.getY());

        setRotation(rot);

    }

    /**
     * 
     * @param targetSpot
     * @return False if target is occupied by another vehicle
     */
    public boolean isSpotOccupiedByAnotherVehicle(MovementObject targetSpot) {

        if (targetSpot.getOccupierId().equals("")) {
            targetSpot.setOccupierId(this.getId());
            return false;
        } else if (targetSpot.getOccupierId().equals(this.getId())) {
            return false;
        }

        return true;

    }

//    public boolean isMergeOccupiedByAnotherVehicle(Merge targetSpot) {
//
//
//        if (targetSpot.getOccupierId().equals("")) {
//            targetSpot.setOccupierId(this.getId());
//            return false;
//        } else if (targetSpot.getOccupierId().equals(this.getId())) {
//            return false;
//        }
//
//        return true;
//    }
    
    /**
     * Checks if there is a vehicle ahead of this car until the target spot
     * @return 
     */
    public boolean isThereVehicleAhead() {

        //Check if there is a vehicle ahead and it is already set
        if (this.getNextCar() != null) {
            return true;
        }

        MovementObject targetSpot = this.getTargetSpot();

        //Get The First Car Moving to this target spot
        DynamicObject vehicleAhead;

        //check If the next spot is merge and this vehicle is on the alternative route
        boolean isSpotMergeAlternative = targetSpot instanceof Merge
                && this.getCurrentSpot() == ((Merge) targetSpot).getPrevAlternative();

        //If it is get the alternative vehicle
        if (isSpotMergeAlternative) {
            vehicleAhead = ((Merge) targetSpot).getIncomingCar2();

        } else {
            vehicleAhead = targetSpot.getIncomingCar();

        }

        //If the first car is this car, return false(there is no vehicle ahead)
        if (vehicleAhead == this) {
            return false;
        }
        //If there is no car ahead, set this car as the first incoming car
        if (vehicleAhead == null) {

            if (isSpotMergeAlternative) {
                ((Merge) targetSpot).setIncomingCar2(this);
            } else {
                targetSpot.setIncomingCar(this);
            }

            return false;
        }

        //If there are vehicle moving to the target spot, get the last one
        while (vehicleAhead.getPrevCar() != null && vehicleAhead.getPrevCar() != this) {
            vehicleAhead = vehicleAhead.getPrevCar();
        }

        //Set the connection in between
        vehicleAhead.setPrevCar(this);
        this.setNextCar(vehicleAhead);

        return true;

    }

    /**
     * Calculates the distance with this vehicle and the next one
     * and checks the distance
     * @return 
     */
    public boolean canMoveWithSameSpeed() {

        double distance = calculateDistance(this.getX(), this.getY(),
                this.getNextCar().getX(), this.getNextCar().getY());

        return isDistanceEnough(distance);

    }
    
    /**
     * Calculates the distance with this vehicle and the next one
     * and checks the distance
     * @param vehicleAhead
     * @return 
     */
    public boolean canMoveWithTheSameSpeed(DynamicObject vehicleAhead) {

        double distance = calculateDistance(this.getX(), this.getY(),
                vehicleAhead.getX(), vehicleAhead.getY());

        return isDistanceEnough(vehicleAhead, distance);

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

    private boolean isDistanceEnough(double distance) {

        DynamicObject vehicleAhead = this.getNextCar();

        double length1 = this.getLength() / 2;
        double length2 = vehicleAhead.getLength() / 2;

        //If there is enough distance between two vehicles return true
        if (distance > length1 + length2 + Constants.vehicleDistanceLimit) {
            return true;
        }

        //If the vehicle ahead is slower, change speed of this vehicle
        if (vehicleAhead.getTempSpeed() < this.getTempSpeed()) {
           
            this.setTempSpeed(vehicleAhead.getTempSpeed());
            
            return false;
        }

        return true;

    }

    private boolean isDistanceEnough(DynamicObject vehicleAhead, double distance) {

        double length1 = this.getLength() / 2;
        double length2 = vehicleAhead.getLength() / 2;

        //If there is enough distance between two vehicles return true
        if (distance > length1 + length2 + Constants.vehicleDistanceLimit) {
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
     * @return 
     */
    public boolean isFarFromSpot() {

        double distance = calculateDistance(this.getX(), this.getY(),
                this.getCurrentSpot().getX(), this.getCurrentSpot().getY());

        return distance > (this.getLength() * 1.5);

    }

    /**
     * Checks the distance between vehicle and its target spot
     * @return 
     */
    public boolean isCloseToTargetSpot() {

        //Calculate the Distance Between the vehicle and its' target spot
        double distance = calculateDistance(this.getX(), this.getY(),
                this.getTargetSpot().getX(), this.getTargetSpot().getY());

        return distance < Constants.vehicleToSpotDistanceLimit + this.getLength() / 2;

    }

    /**
     * Checks the distance between two vehicle
     * @param vehicle
     * @return true if the distance between two vehicle is shorter than the vehicle distance limit
     */
       public boolean isVehicleClose(DynamicObject vehicle) {
        double distance = calculateDistance(this.getX(), this.getY(),
                vehicle.getX(), vehicle.getY());

        return distance < Constants.vehicleDistanceLimit + this.getLength() / 2 + vehicle.getLength() / 2;

    }

    
    /**
     * Compare the speed of two vehicles. 
     * @param vehicleAhead
     * @return true if the vehicle ahead is slower than this vehicle
     */
    public boolean shouldSlowDown(DynamicObject vehicleAhead) {

        //If the vehicle ahead is slower, change speed of this vehicle
        if (vehicleAhead.getTempSpeed() < this.getTempSpeed()) {

            this.setTempSpeed(vehicleAhead.getTempSpeed());
            return true;
        }

        return false;

    }

    /**
     *  Removes The all connections between the vehicle and its previous spot
     * @param previousSpot 
     */
    public void removePreviosSpotConnections(MovementObject previousSpot) {

        if (previousSpot.getLeavingCar() == this) {
            previousSpot.setLeavingCar(null);
        }
        if (previousSpot.getOccupierId().equals(this.getId())) {
            previousSpot.setOccupierId("");
        }
    }

}
