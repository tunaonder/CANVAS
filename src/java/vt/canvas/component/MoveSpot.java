/*
 * Created by Sait Tuna Onder on 2017.03.08  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component;

import vt.canvas.component.helpers.StaticObject;

/**
 *
 * @author Onder
 */

public class MoveSpot extends StaticObject {

    private StaticObject prev;
    private StaticObject next;

    public MoveSpot(String id, double x, double y) {
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
}
