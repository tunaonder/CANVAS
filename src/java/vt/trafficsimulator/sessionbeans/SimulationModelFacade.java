/*
 * Created by Sait Tuna Onder on 2018.03.21  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficsimulator.sessionbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import vt.trafficsimulator.entityclasses.SimulationModel;

/**
 *
 * @author Onder
 */
@Stateless
public class SimulationModelFacade extends AbstractFacade<SimulationModel> {

    @PersistenceContext(unitName = "TrafficSimulatorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SimulationModelFacade() {
        super(SimulationModel.class);
    }
    
}
