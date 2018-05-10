/*
 * Created by Sait Tuna Onder on 2017.03.19  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.execution.events;

import vt.canvas.execution.events.helpers.Event;
import vt.canvas.component.Vehicle;

/**
 *
 * @author Onder
 */
public class VehicleCreateEvent extends Event {

    private final Vehicle vehicle;
    //Factory Id
    private final String originId;

    public VehicleCreateEvent(int time, Vehicle vehicle, String id) {
        super(time);
        this.vehicle = vehicle;
        this.originId = id;
    }

    @Override
    public String toString() {
        return "Vehicle: " + vehicle.getId() + " created at time: " + this.getTime();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getOriginId() {
        return originId;
    }

}
