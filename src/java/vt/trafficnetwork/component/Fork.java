/*
 * Created by Sait Tuna Onder on 2017.04.22  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.MovementObject;

/**
 *
 * @author Onder
 */
public class Fork extends MovementObject{
    
    private MovementObject prev;
    private MovementObject next;
    //Fork Objects has an alternative Next Spot that is different than Move Spots
    private MovementObject nextAlternative;

    public Fork(String id, double x, double y) {
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

    public MovementObject getNextAlternative() {
        return nextAlternative;
    }

    public void setNextAlternative(MovementObject nextAlternative) {
        this.nextAlternative = nextAlternative;
    }
    
    
    
}
