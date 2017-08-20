/*
 * Created by Sait Tuna Onder on 2017.03.17  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.MovementObject;
import vt.trafficnetwork.component.factory.VehicleFactory;


/**
 * 
 * @author Onder
 */

public class EnterPoint extends MovementObject{

   
    private MovementObject next;
    //Each Enter Point has a Vehicle Factory
    private VehicleFactory factory;
    
    public EnterPoint(String id, double x, double y) {
        super(id,x,y);
        this.factory = new VehicleFactory(this);
        this.next = null;
    }

    public MovementObject getNext() {
        return next;
    }

    public void setNext(MovementObject next) {
        this.next = next;
    }

    public VehicleFactory getFactory() {
        return factory;
    }

    public void setFactory(VehicleFactory factory) {
        this.factory = factory;
    }
    
    public Vehicle getNewVehicle(){
        return factory.getNewVehicle();
    }


    
    
    
    

}