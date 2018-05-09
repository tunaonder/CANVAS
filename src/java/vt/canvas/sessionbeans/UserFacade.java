/*
 * Created by Sait Tuna Onder on 2018.03.21  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.sessionbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import vt.canvas.entityclasses.User;

/**
 *
 * @author Onder
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "TrafficSimulatorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public User findByUsername(String username) {
        if (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                .setParameter("username", username)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (User) (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                    .setParameter("username", username)
                    .getSingleResult());
        }
    }
    
    public String findNameByUserId(int id) {
        
        User user = (User) em.createNamedQuery("findById")
                .setParameter("id", id)
                .getSingleResult();
        
        return user.getFirstName() + " " + user.getLastName();
    }


}
