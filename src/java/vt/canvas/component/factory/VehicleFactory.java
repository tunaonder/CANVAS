/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component.factory;

import vt.canvas.component.EnterPoint;
import vt.canvas.component.Vehicle;
import vt.canvas.execution.Constants;
import vt.canvas.execution.random.RandomVariate;
import vt.canvas.execution.random.UniformRandom;

/**
 * This class Creates Vehicles
 * @author Onder
 */

public class VehicleFactory {

    private final RandomVariate random;
    
    //Defines Numerical id of new car
    private int producedVehicleCount;
    
    private double minSpeed;
    private double maxSpeed;
    private int vehicleLength1;
    private int vehicleLength2;
    private int vehicleLength3;
    private int vehicleLength4;

    public VehicleFactory() {
        this.random = new UniformRandom();
        this.producedVehicleCount = 0;
    }
    
    public void setFactoryConstants(Constants simulationConstants){
        minSpeed = simulationConstants.minSpeed;
        maxSpeed = simulationConstants.maxSpeed;
        vehicleLength1 = simulationConstants.vehicleLength1;
        vehicleLength2 = simulationConstants.vehicleLength2;
        vehicleLength3 = simulationConstants.vehicleLength3;
        vehicleLength4 = simulationConstants.vehicleLength4;
        
    }

    public Vehicle getNewVehicle(EnterPoint factoryOwner) {
        
        //Set New Id
        producedVehicleCount++;
        
        //ID represents dynamic object id
        String id = "d" + producedVehicleCount + factoryOwner.getId();

        //Get random speed
        double speed = random.nextDouble(minSpeed, maxSpeed);
        
        double length;
        
        // Randomly assign a length to the vehicle
        // Vehicle length1 refers to automobiles while others refer to
        // trucks and busses
        int lengthGenerator = (int)random.nextDouble(1, 25);
        
        if(lengthGenerator < 21){
            length = vehicleLength1;
        }
        else if(lengthGenerator < 23){
            length = vehicleLength2;
        }
        else if(lengthGenerator == 23){
            length = vehicleLength3;
        }
        else{
            length = vehicleLength4;
        }
             
        Vehicle vehicle = new Vehicle(id, factoryOwner.getX(), factoryOwner.getY(), speed,
                factoryOwner, factoryOwner.getNext(), length);
        
        return vehicle;
    }

}
