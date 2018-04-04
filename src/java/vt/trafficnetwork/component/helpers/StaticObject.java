/*
 * Created by Sait Tuna Onder on 2017.04.06  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component.helpers;

/**
 *
 * @author Onder
 */
public abstract class StaticObject extends SimulationObject{
    
    private DynamicObject incomingCar;
    private DynamicObject leavingCar;
    private String occupierId;

    public StaticObject(String id, double x, double y) {
        super(id, x, y);
        this.incomingCar = null;
        this.occupierId = "";
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
