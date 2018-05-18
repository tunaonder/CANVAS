/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.execution;

import vt.canvas.component.helpers.StaticObject;

/**
 *
 * @author Onder
 */
public class Simulator {

    protected SimulationRuntime rt;

    public Simulator(String sessionIdentifier) {

        this.rt = new SimulationRuntime(sessionIdentifier);

    }

    public void start(int simDuration, int vehicleLength) {

        // initialize the simulation objects
        initialize(simDuration, vehicleLength);
        // run the simulation...
        doSimulation();
        // signal end of simulation
        shutdown();
    }
    
    /**
     * Prepares the SimulationRuntime for the runtime algorithm
     * @param simDuration
     * @param vehicleLength 
     */
    protected void initialize(int simDuration, int vehicleLength) {
        System.out.println("initializing simulator...");
        // Set Simulation Constants
        rt.setSimulationConstants(vehicleLength);
        // Set the Duration
        rt.setSimulationTimeLimit(simDuration);
        // Set the Initial Future Events
        rt.initializeFutureList();
        System.out.println("Future Event List initalized");

    }

    //Perform Simulation
    protected void doSimulation() {
        System.out.println("Simulation Started");
        rt.simulate();
    }

    protected void shutdown() {
        System.out.println("shutting down simulator...");
    }

    /**
     * This method is called by Simulation Builder for each static object
     *
     * @param object
     */
    public void addObject(StaticObject object) {
        rt.addObject(object);

    }

    // Request new events from the simulation runtime
    public void requestNewEventsToVisualize() {
        rt.requestNewEventsToVisualize();
    }

}
