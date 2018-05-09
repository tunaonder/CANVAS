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
     * This method is called by Simulation Builder for each object taken from
     * client
     *
     * @param object
     */
    public void addObject(StaticObject object) {
        rt.addObject(object);

    }

    public void requestNewEventsToVisualize() {
        rt.requestNewEventsToVisualize();
    }

}
