/*
 * Created by Sait Tuna Onder on 2017.04.06  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component.helpers;

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
    private double rotation;

    private StaticObject currentSpot;
    private StaticObject targetSpot;

    private DynamicObject prevDynamicObj;
    private DynamicObject nextDynamicObj;

    public DynamicObject(String id, double x, double y, double speed, StaticObject current, StaticObject target, double length) {

        super(id, x, y);
        this.speed = speed;
        this.tempSpeed = speed;
        this.currentSpot = current;
        this.targetSpot = target;
        this.length = length;
        this.prevDynamicObj = null;
        this.nextDynamicObj = null;
        this.rotation = calculateRotation(x, y, target.getX(), target.getY());
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

    public DynamicObject getPrevDynamicObj() {
        return prevDynamicObj;
    }

    public void setPrevDynamicObj(DynamicObject prevDynamicObj) {
        this.prevDynamicObj = prevDynamicObj;
    }

    public DynamicObject getNextDynamicObj() {
        return nextDynamicObj;
    }

    public void setNextDynamicObj(DynamicObject nextDynamicObj) {
        this.nextDynamicObj = nextDynamicObj;
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
    public void setRotation(StaticObject o1, StaticObject o2) {

        double rot = calculateRotation(o1.getX(), o1.getY(), o2.getX(), o2.getY());

        setRotation(rot);

    }
    
    

}
