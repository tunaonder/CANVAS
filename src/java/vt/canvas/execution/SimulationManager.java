/*
 * Created by Sait Tuna Onder on 2017.03.13  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.execution;

import java.util.HashMap;
import java.util.Map;
import javax.json.JsonArray;

/**
 * Each Client has a Simulation Manager instance
 * References are stored in static "managers" map
 * @author Onder
 */
public class SimulationManager {

    //Simulation Manager Map, one for each user session
    static Map<String, SimulationManager> managers;

    //Unique identifier for each session
    String sessionIdentifier;

    /**
     * Simulation Executor Instance
     */
    SimulationExecutor executor;

    /**
     * Running instance of the simulation
     */
    SimulationRunner runner;

    public SimulationManager(String sessionIdentifier) {

        this.sessionIdentifier = sessionIdentifier;
        this.runner = new SimulationRunner(sessionIdentifier);
    }

    /**
     *
     * @param sessionIdentifier unique identifier set by websocket for the
     * session
     * @return simulationManager instance
     */
    public static SimulationManager addSimulationInstance(String sessionIdentifier) {

        //If no session is created yet, initialize the hash map
        if (managers == null) {
            managers = new HashMap();
        }

        SimulationManager manager = new SimulationManager(sessionIdentifier);
        //Add This Manager Instance To The Static Hash Map
        managers.put(sessionIdentifier, manager);

        return manager;
    }
    
    public static SimulationManager getSimulationInstance(String sessionIdentifier){
        return managers.get(sessionIdentifier);      
    }

    public void requestExecution(JsonArray modelData) {

        // Start execution thread
        executor = new SimulationExecutor(modelData);
        Thread exec = new Thread(executor, sessionIdentifier);

        exec.start();
    }

    public void requestEventsForVisualizer() {
        
        SimulationMessageRequester messageRequester = new SimulationMessageRequester();
        Thread exec = new Thread(messageRequester);
        exec.start();
    }
    
    // Main Simulation Execution Thread
    private class SimulationExecutor implements Runnable {

        private final JsonArray model;

        public SimulationExecutor(JsonArray modelData) {
            this.model = modelData;
        }

        @Override
        public void run() {
            try {
                runner.execute(model);

            } catch (Exception e) {
                System.out.println("Runner Exception: " + e.getMessage());
            }
        }
    };
    
    // Event Request Theread
    private class SimulationMessageRequester implements Runnable {

        @Override
        public void run() {
            try {
                runner.requestNewEventsToVisualize();

            } catch (Exception e) {
                System.out.println("Message Requester Exception: " + e.getMessage());
            }
        }
    };

}
