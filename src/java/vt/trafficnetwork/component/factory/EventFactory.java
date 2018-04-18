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
    
    private int numberOfVehicles;
    private long sumOfVehicleCreationTime;


    public EventFactory() {
        numberOfVehicles = 0;
        sumOfVehicleCreationTime = 0;
    }
    

    public Event scheduleVehicleCreation(EnterPoint enterPoint, int currentTime) {
        
        // A time frame is 1/60 seconds
        // Generation Times are provided in terms of seconds
        RandomVariate random = new UniformRandom(enterPoint.getMinGenerationTime()*60, 
                 enterPoint.getMaxGenerationTime()*60);
        //Get random creation time
        int time = (int)random.nextDouble() + currentTime;
               
        Vehicle vehicle = enterPoint.getNewVehicle();
       
        numberOfVehicles ++;
        // Convert the time into seconds
        sumOfVehicleCreationTime += time/60;
        
        return new VehicleCreateEvent(time, vehicle, enterPoint.getId());
    }
    
    public Event scheduleTrafficLightStateChange(TrafficLight light, int currentTime){
             
        int time;
        if(currentTime == 0 && light.getGreenStartTime() != 0){
            time = light.getGreenStartTime();
        }
        else{
            //If Vehicle is in Red State: time duration is redStateTime.
            //Otherwise the duration is greenStateTime
            time = currentTime + (light.getState() == TrafficLight.STATE.RED ? 
                    light.getRedStateTime() : light.getGreenStateTime());
          
        }     
              
        return new TrafficLightStateChangeEvent(time, light);
     
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }

    public long getSumOfVehicleCreationTime() {
        return sumOfVehicleCreationTime;
    }

}
