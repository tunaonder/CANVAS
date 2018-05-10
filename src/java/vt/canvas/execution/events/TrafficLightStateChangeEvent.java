/*
 * Created by Sait Tuna Onder on 2017.05.03  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.execution.events;

import vt.canvas.component.TrafficLight;
import vt.canvas.execution.events.helpers.Event;

/**
 *
 * @author Onder
 */
public class TrafficLightStateChangeEvent extends Event {

    private TrafficLight light;

    public TrafficLightStateChangeEvent(int time, TrafficLight light) {
        super(time);
        this.light = light;
    }

    public TrafficLight getLight() {
        return light;
    }

    public void setLight(TrafficLight light) {
        this.light = light;
    }
}
