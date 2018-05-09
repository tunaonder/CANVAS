/*
 * Created by Sait Tuna Onder on 2017.03.17  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.component;

import vt.canvas.component.helpers.StaticObject;
import vt.canvas.component.factory.VehicleFactory;

/**
 * 
 * @author Onder
 */

public class EnterPoint extends StaticObject{

   
    private StaticObject next;
    //Each Enter Point has a Vehicle Factory
    private VehicleFactory factory;
    
    private final int minGenerationTime;
    private final int maxGenerationTime;
    
    public EnterPoint(String id, double x, double y, int minTime, int maxTime) {
        super(id,x,y);
        this.factory = new VehicleFactory();
        this.next = null;
        this.minGenerationTime = minTime;
        this.maxGenerationTime = maxTime;
    }

    public StaticObject getNext() {
        return next;
    }

    public void setNext(StaticObject next) {
        this.next = next;
    }

    public VehicleFactory getFactory() {
        return factory;
    }

    public void setFactory(VehicleFactory factory) {
        this.factory = factory;
    }
    
    public Vehicle getNewVehicle(){
        return factory.getNewVehicle(this);
    }

    public int getMinGenerationTime() {
        return minGenerationTime;
    }

    public int getMaxGenerationTime() {
        return maxGenerationTime;
    }


}