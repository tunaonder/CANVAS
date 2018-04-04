/*
 * Created by Sait Tuna Onder on 2017.04.22  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.StaticObject;

/**
 *
 * @author Onder
 */
public class Fork extends StaticObject{
    
    private StaticObject prev;
    private StaticObject next;
    //Fork Objects has an alternative Next Spot that is different than Move Spots
    private StaticObject nextAlternative;

    public Fork(String id, double x, double y) {
        super(id, x, y);  
    }

    public StaticObject getPrev() {
        return prev;
    }

    public void setPrev(StaticObject prev) {
        this.prev = prev;
    }

    public StaticObject getNext() {
        return next;
    }

    public void setNext(StaticObject next) {
        this.next = next;
    }

    public StaticObject getNextAlternative() {
        return nextAlternative;
    }

    public void setNextAlternative(StaticObject nextAlternative) {
        this.nextAlternative = nextAlternative;
    }
    
    
    
}
