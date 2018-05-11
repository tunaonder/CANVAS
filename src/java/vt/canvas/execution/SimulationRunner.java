/*
 * Created by Sait Tuna Onder on 2017.03.16  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.execution;

import javax.json.JsonArray;
import javax.json.JsonObject;
import vt.canvas.builder.SimulationBuilder;

/**
 * This class builds the simulation before simulator starts If simulationBuilder
 * does not validate the input(modelData), simulationBuilder returns an error
 * message to the client
 *
 * @author Onder
 */
public class SimulationRunner {

    private final SimulationBuilder simulationBuilder;
    private final Simulator sim;

    public SimulationRunner(String sessionIdentifier) {

        //Build the simulation model using the data sent by client
        this.simulationBuilder = new SimulationBuilder(sessionIdentifier);        
        this.sim = new Simulator(sessionIdentifier);
    }

    public void execute(JsonArray modelData) {
        
        // First object of the simulation model has simulation duration information
        String simDuration = ((JsonObject) modelData.get(0)).getString("duration");
        int vehicleLength = ((JsonObject) modelData.get(0)).getInt("vehicleLength");
        
        boolean buildSuccess;
        try {            
            //Build Simulation Model for Simulation instance to be ready to start
            buildSuccess = simulationBuilder.buildModel(modelData, sim);
        
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Could not build a simulation from the supplied model");
            return;
        }
        
        // If Simulation is not built, return.
        if(!buildSuccess) return;
        
        try {            
            int duration = Integer.parseInt(simDuration);
            sim.start(duration, vehicleLength);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Simulation ended unexpectedly");
        }

    }
    
    public void requestNewEventsToVisualize(){
        sim.requestNewEventsToVisualize();      
    }
}
