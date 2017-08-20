/*
 * Created by Sait Tuna Onder on 2017.04.06  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component.helpers;

/**
 *
 * @author Onder
 */
public abstract class MovementObject implements StaticObject{
    
    private String id;
    private double x;
    private double y;
    private DynamicObject incomingCar;
    private DynamicObject leavingCar;
    private String occupierId;

    public MovementObject(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.incomingCar = null;
        this.occupierId = "";
    }


    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public DynamicObject getIncomingCar() {
        return incomingCar;
    }

    public void setIncomingCar(DynamicObject incomingCar) {
        this.incomingCar = incomingCar;
    }

    public DynamicObject getLeavingCar() {
        return leavingCar;
    }

    public void setLeavingCar(DynamicObject leavingCar) {
        this.leavingCar = leavingCar;
    }

    public String getOccupierId() {
        return occupierId;
    }

    public void setOccupierId(String occupierId) {
        this.occupierId = occupierId;
    }
    
    
    
    
    
    
    
    
}
