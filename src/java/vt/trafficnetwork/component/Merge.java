/*
 * Created by Sait Tuna Onder on 2017.04.23  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.DynamicObject;
import vt.trafficnetwork.component.helpers.StaticObject;

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
    private DynamicObject incomingCar2;


    public Merge(String id, double x, double y) {
        super(id, x, y);
        this.incomingCar2 = null;

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

    public DynamicObject getIncomingCar2() {
        return incomingCar2;
    }

    public void setIncomingCar2(DynamicObject incomingCar2) {
        this.incomingCar2 = incomingCar2;
    }

}
