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
public abstract class DynamicObject extends SimulationObject {

    private double speed;
    private double tempSpeed;
    private double length;

    private StaticObject currentSpot;
    private StaticObject targetSpot;

    private DynamicObject prevCar;
    private DynamicObject nextCar;

//    public DynamicObject(String id) {
//
//    }
    public DynamicObject(String id, double x, double y, double speed, StaticObject current, StaticObject target, double length) {

        super(id, x, y);
        this.speed = speed;
        this.tempSpeed = speed;
        this.currentSpot = current;
        this.targetSpot = target;
        this.length = length;
        this.prevCar = null;
        this.nextCar = null;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public StaticObject getCurrentSpot() {
        return currentSpot;
    }

    public void setCurrentSpot(StaticObject currentSpot) {
        this.currentSpot = currentSpot;
    }

    public StaticObject getTargetSpot() {
        return targetSpot;
    }

    public void setTargetSpot(StaticObject targetSpot) {
        this.targetSpot = targetSpot;
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
