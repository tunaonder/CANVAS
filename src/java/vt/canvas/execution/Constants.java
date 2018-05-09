/*
 * Created by Sait Tuna Onder on 2017.04.23  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.execution;

/**
 *
 * @author Onder
 */

public class Constants {
    
    // Default Vehicle Distance Limit(Between Vehicles)
    public int vehicleDistanceLimit = 16;
    // Default Vehicle to Spot Distance Limit
    public int vehicleToSpotDistanceLimit = 20;
    
    // Default Automobile Length
    public int vehicleLength1 = 16;
    // Default Small Bus Length
    public int vehicleLength2 = 22;
    // Default Small Truck Length
    public int vehicleLength3 = 30;
    // Default Long Truck Length
    public int vehicleLength4 = 40;
    
    // Default Minimum and Maximum Speed Limits
    public double minSpeed = 0.5;
    public double maxSpeed = 1.3;

    /**
     * All Default Constants are updated according to the Vehicle Size set by the Client
     * @param vehicleLength 
     */
    public Constants(int vehicleLength) {
       
        double ratio = vehicleLength / vehicleLength1;
        vehicleLength1 = (int)vehicleLength;
        vehicleLength2 *= ratio;
        vehicleLength3 *= ratio;
        vehicleLength4 *= ratio;
        
        minSpeed *= ratio;
        maxSpeed *= ratio;
        
        vehicleDistanceLimit *= ratio;
        vehicleToSpotDistanceLimit *= ratio;
        
    }

}
