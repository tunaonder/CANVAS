/*
 * Created by Sait Tuna Onder on 2017.03.08  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.MovementObject;

/**
 *
 * @author Onder
 */
public class MoveSpot extends MovementObject {

    private MovementObject prev;
    private MovementObject next;

    public MoveSpot(String id, double x, double y) {
        super(id, x, y);
        
    }

    public MovementObject getPrev() {
        return prev;
    }

    public void setPrev(MovementObject prev) {
        this.prev = prev;
    }

    public MovementObject getNext() {
        return next;
    }

    public void setNext(MovementObject next) {
        this.next = next;
    }


    
    

    
    

}
