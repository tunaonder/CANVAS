/*
 * Created by Sait Tuna Onder on 2017.04.23  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component;

import vt.canvas.component.helpers.DynamicObject;
import vt.canvas.component.helpers.StaticObject;

/**
 *
 * @author Onder
 */
public class Merge extends StaticObject {

    private StaticObject prev;
    private StaticObject next;
    //Fork Objects has an alternative Prev Spot that is different than Move Spots
    private StaticObject prevAlternative;
    //Each Movement Object has an Incoming Car. However, Since merge has 2 previous spot, it has another 
    //INCOMING CAR
    private DynamicObject incomingDynamicObject2;


    public Merge(String id, double x, double y) {
        super(id, x, y);
        this.incomingDynamicObject2 = null;

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

    public StaticObject getPrevAlternative() {
        return prevAlternative;
    }

    public void setPrevAlternative(StaticObject prevAlternative) {
        this.prevAlternative = prevAlternative;
    }

    public DynamicObject getIncomingDynamicObject2() {
        return incomingDynamicObject2;
    }

    public void setIncomingDynamicObject2(DynamicObject incomingDynamicObject2) {
        this.incomingDynamicObject2 = incomingDynamicObject2;
    }

}
