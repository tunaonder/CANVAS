/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component.factory;

import vt.trafficnetwork.component.EnterPoint;
import vt.trafficnetwork.component.Vehicle;
import vt.trafficnetwork.execution.Constants;
import vt.trafficnetwork.execution.random.RandomVariate;
import vt.trafficnetwork.execution.random.UniformRandom;

/**
 * This class Creates Vehicles
 * @author Onder
 */
public class VehicleFactory {

    private final RandomVariate random;
    
    //Defines Numerical id of new car
    private int producedVehicleCount;

    public VehicleFactory() {
        this.random = new UniformRandom();
        this.producedVehicleCount = 0;
    }

    public Vehicle getNewVehicle(EnterPoint factoryOwner) {
        
        //Set New Id
        producedVehicleCount++;
        
        //ID represents dynamic object id
        String id = "d" + producedVehicleCount + factoryOwner.getId();

        //Get random speed
        double speed = random.nextDouble(Constants.minSpeed, Constants.maxSpeed);
        
        double length;
        
        int lengthGenerator = (int)random.nextDouble(1, 12);
        
        if(lengthGenerator < 9){
            length = Constants.vehicleLength1;
        }
        else if(lengthGenerator == 9){
            length = Constants.vehicleLength2;
        }
        else if(lengthGenerator == 10){
            length = Constants.vehicleLength3;
        }
        else{
            length = Constants.vehicleLength4;
        }
        
        
        //Set the length later
        Vehicle vehicle = new Vehicle(id, factoryOwner.getX(), factoryOwner.getY(), speed,
                factoryOwner, factoryOwner.getNext(), length);

        //System.out.println("Vehicle " + vehicle.getId() + " is created");
        
        return vehicle;
    }

}
