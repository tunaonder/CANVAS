/*
 * Created by Sait Tuna Onder on 2017.03.17  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.MovementObject;

/**
 *
 * @author Onder
 */


public class ExitPoint extends MovementObject{
    
    private MovementObject prev;
    
    public ExitPoint(String id, double x, double y) {
        super(id, x, y);
    }

    public MovementObject getPrev() {
        return prev;
    }

    public void setPrev(MovementObject prev) {
        this.prev = prev;
    }
    
    

    
    
}