/*
 * Created by Sait Tuna Onder on 2017.04.06  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component.helpers;

/**
 *
 * @author Onder
 */
/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */


/**
 *
 * @author Onder
 */
public abstract class DynamicObject implements SimulationObject {
    
    private String id;
    
    private double x;
    private double y;
    private double speed;
    private double tempSpeed;
    private double length;
    
    private MovementObject currentSpot;
    private MovementObject targetSpot;
    
    private DynamicObject prevCar;
    private DynamicObject nextCar;
    
    public DynamicObject(String id) {

    }

    public DynamicObject(String id, double x, double y, double speed, MovementObject current, MovementObject target, double length) {
        
        this.id = id;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.tempSpeed = speed;
        this.currentSpot = current;
        this.targetSpot = target;
        this.length = length;
        this.prevCar = null;
        this.nextCar = null;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public MovementObject getCurrentSpot() {
        return currentSpot;
    }

    public void setCurrentSpot(MovementObject currentSpot) {
        this.currentSpot = currentSpot;
    }

    public MovementObject getTargetSpot() {
        return targetSpot;
    }

    public void setTargetSpot(MovementObject targetSpot) {
        this.targetSpot = targetSpot;
    }
    
    @Override
    public String getId(){
        return id;
    }
    
        public DynamicObject getPrevCar() {
        return prevCar;
    }

    public void setPrevCar(DynamicObject prevCar) {
        this.prevCar = prevCar;
    }

    public DynamicObject getNextCar() {
        return nextCar;
    }

    public void setNextCar(DynamicObject nextCar) {
        this.nextCar = nextCar;
    }
        public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getTempSpeed() {
        return tempSpeed;
    }

    public void setTempSpeed(double tempSpeed) {
        this.tempSpeed = tempSpeed;
    }

    
    
    
    
    
    
}

