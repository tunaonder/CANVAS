/*
 * Created by Sait Tuna Onder on 2017.03.19  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component.factory;


import vt.trafficnetwork.component.EnterPoint;
import vt.trafficnetwork.component.TrafficLight;
import vt.trafficnetwork.component.Vehicle;
import vt.trafficnetwork.execution.events.TrafficLightStateChangeEvent;
import vt.trafficnetwork.execution.events.helpers.Event;
import vt.trafficnetwork.execution.events.VehicleCreateEvent;
import vt.trafficnetwork.execution.random.RandomVariate;
import vt.trafficnetwork.execution.random.UniformRandom;

/**
 * This class creates future events
 * @author Onder
 */
public class EventFactory {
    
    public EventFactory() {
    }
    

    public Event scheduleVehicleCreation(EnterPoint enterPoint, int currentTime) {
        
        // A time frame is 1/60 seconds
        // Generation Times are provided in terms of seconds
        RandomVariate random = new UniformRandom(enterPoint.getMinGenerationTime()*60, 
                 enterPoint.getMaxGenerationTime()*60);
        // Get random creation time
        int time = (int)random.nextDouble() + currentTime;
               
        Vehicle vehicle = enterPoint.getNewVehicle();

        return new VehicleCreateEvent(time, vehicle, enterPoint.getId());
    }
    
    public Event scheduleTrafficLightStateChange(TrafficLight light, int currentTime){
             
        int time;
        if(currentTime == 0 && light.getGreenStartTime() != 0){
            time = light.getGreenStartTime();
        }
        else{
            // If Vehicle is in Red State: time duration is redStateTime.
            // Otherwise the duration is greenStateTime
            // Add an extra 1.5 seconds to red state (60 * 1.5)
            time = currentTime + (light.getState() == TrafficLight.STATE.RED ? 
                    light.getRedStateTime() + 90 : light.getGreenStateTime() - 90);
          
        }     
              
        return new TrafficLightStateChangeEvent(time, light);
     
    }

}
