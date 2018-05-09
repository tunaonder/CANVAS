/*
 * Created by Sait Tuna Onder on 2017.04.06  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component.helpers;

/**
 *
 * @author Onder
 */

public abstract class StaticObject extends SimulationObject{
    
    private DynamicObject incomingDynamicObj;
    private DynamicObject leavingDynamicObj;
    private String occupierId;

    public StaticObject(String id, double x, double y) {
        super(id, x, y);
        this.incomingDynamicObj = null;
        this.occupierId = "";
    }

    public DynamicObject getIncomingDynamicObj() {
        return incomingDynamicObj;
    }

    public void setIncomingDynamicObj(DynamicObject incomingDynamicObj) {
        this.incomingDynamicObj = incomingDynamicObj;
    }

    public DynamicObject getLeavingDynamicObj() {
        return leavingDynamicObj;
    }

    public void setLeavingDynamicObj(DynamicObject leavingDynamicObj) {
        this.leavingDynamicObj = leavingDynamicObj;
    }

    public String getOccupierId() {
        return occupierId;
    }

    public void setOccupierId(String occupierId) {
        this.occupierId = occupierId;
    }

}
