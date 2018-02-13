/*
 * Created by Sait Tuna Onder on 2017.03.13  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.execution;

import java.util.HashMap;
import java.util.Map;
import javax.json.JsonArray;

/**
 *
 * @author Onder
 */
public class SimulationManager {

    //Simulation Manager Map, one for each user session
    static Map managers;

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

        //If No session is created yet, initialize the hash map
        if (managers == null) {
            managers = new HashMap();
        }
                
        SimulationManager manager = new SimulationManager(sessionIdentifier);
        //Add This Manager Instance To The Static Hash Map
        managers.put(sessionIdentifier, manager);

        return manager;
    }

    public void requestExecution(JsonArray modelData) {

        // start execution thread
        executor = new SimulationExecutor(modelData);      
        Thread exec = new Thread(executor, sessionIdentifier);
        //Set priority
        //exec.setPriority(Thread.currentThread().getPriority() - 1);
        exec.start();   
    }

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

}
