/*
 * Created by Sait Tuna Onder on 2017.03.17  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.component;

import vt.canvas.component.helpers.StaticObject;

/**
 *
 * @author Onder
 */

public class ExitPoint extends StaticObject{
    
    private StaticObject prev;
    
    public ExitPoint(String id, double x, double y) {
        super(id, x, y);
    }

    public StaticObject getPrev() {
        return prev;
    }

    public void setPrev(StaticObject prev) {
        this.prev = prev;
    }
}