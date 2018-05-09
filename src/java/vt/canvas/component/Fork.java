/*
 * Created by Sait Tuna Onder on 2017.04.22  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component;

import vt.canvas.component.helpers.StaticObject;

/**
 *
 * @author Onder
 */

public class Fork extends StaticObject{
    
    private StaticObject prev;
    private StaticObject next;
    
    //Fork Objects has an alternative Next Spot
    private StaticObject nextAlternative;
    private final int newPathProbability;

    public Fork(String id, double x, double y, int newPathProbability) {
        super(id, x, y);  
        this.newPathProbability = newPathProbability;
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

    public int getNewPathProbability() {
        return newPathProbability;
    }

}
