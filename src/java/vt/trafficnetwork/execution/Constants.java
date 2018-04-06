/*
 * Created by Sait Tuna Onder on 2017.04.23  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.execution;

/**
 *
 * @author Onder
 */
public class Constants {

    // In terms of minutes
    // public static final double simulationDuration = 60;
    public int vehicleDistanceLimit = 20;
    public int vehicleToSpotDistanceLimit = 20;

    public int vehicleLength1 = 16;
    public int vehicleLength2 = 26;
    public int vehicleLength3 = 30;
    public int vehicleLength4 = 40;

    public double minSpeed = 0.7;
    public double maxSpeed = 2.0;

    public Constants(int vehicleLength) {
       
        double ratio = vehicleLength / 16.0;
        vehicleLength1 = (int)vehicleLength;
        vehicleLength2 *= ratio;
        vehicleLength3 *= ratio;
        vehicleLength4 *= ratio;
        
        System.out.println(ratio);
        
        minSpeed *= ratio;
        maxSpeed *= ratio;
        
        vehicleDistanceLimit *= ratio;
        vehicleToSpotDistanceLimit *= ratio;
        
    }

}
