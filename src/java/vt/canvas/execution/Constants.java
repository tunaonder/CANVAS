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

    public int vehicleDistanceLimit = 16;
    public int vehicleToSpotDistanceLimit = 20;

    public int vehicleLength1 = 16;
    public int vehicleLength2 = 22;
    public int vehicleLength3 = 30;
    public int vehicleLength4 = 40;

    public double minSpeed = 0.5;
    public double maxSpeed = 1.3;

    public Constants(int vehicleLength) {
       
        double ratio = vehicleLength / 16.0;
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
