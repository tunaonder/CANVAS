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

    /**
     * Timing and flow control object
     */
    protected SimulationRuntime rt;

    /**
     * Flags indicating is the simulator is running, and if simulator has
     * finished
     */
    private boolean running, complete;

    public Simulator(String sessionIdentifier) {

        this.rt = new SimulationRuntime(sessionIdentifier);

    }

    public void start(int simDuration, int vehicleLength) {
        if (this.isRunning()) {
            throw new IllegalStateException("Simulation is already in progress");
        }

        // set state flags
        this.running = true;
        this.complete = false;

        // initialize the simulation objects
        initialize(simDuration, vehicleLength);
        // run the simulation...
        doSimulation();
        // signal end of simulation
        shutdown();

        // set the state flags
        this.complete = true;
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean isComplete() {
        return this.complete;
    }

    protected void initialize(int simDuration, int vehicleLength) {
        System.out.println("initializing simulator...");
        rt.setSimulationConstants(vehicleLength);
        rt.setSimulationTimeLimit(simDuration);        
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
     * This method is called by Simulation Builder for each object taken from client
     * 
     * 
     * @param object 
     */
    public void addObject(StaticObject object){     
        rt.addObject(object);
        
    }
    
    public void requestNewEventsToVisualize(){
        rt.requestNewEventsToVisualize();
    }

}
