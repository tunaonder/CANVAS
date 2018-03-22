/*
 * Created by Sait Tuna Onder on 2018.03.21  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficsimulator.sessionbeans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import vt.trafficsimulator.entityclasses.Project;

/**
 *
 * @author Onder
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {

    @PersistenceContext(unitName = "TrafficSimulatorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }

    public List<Project> findProjectsByUserID(Integer userID) {

        List<Project> projects = em.createNamedQuery("Project.findProjectsByUserId")
                .setParameter("userId", userID)
                .getResultList();

        return projects;
    }
    
    public List<Project> findByProjectName(String projectName) {
        
        List<Project> projects = em.createNamedQuery("Project.findByProjectname")
                .setParameter("projectname", projectName)
                .getResultList();

        return projects;
    }

}
