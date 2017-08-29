/*
 * Created by Sait Tuna Onder on 2017.08.29  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficsimulator.sessionbeans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import vt.trafficsimulator.entityclasses.UserFile;

/**
 *
 * @author Onder
 */
@Stateless
public class UserFileFacade extends AbstractFacade<UserFile> {

    @PersistenceContext(unitName = "TrafficSimulatorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFileFacade() {
        super(UserFile.class);
    }

    public UserFile getUserFile(int id) {
        return em.find(UserFile.class, id);
    }
    
        /**
     *
     * @param userID is the Primary Key of the user entity in the database
     * @return a list of object references of userFiles that belong to the user whose DB Primary Key = userID
     */
    public List<UserFile> findUserFilesByUserID(Integer userID) {
        /*
        The following @NamedQuery definition is given in UserFile.java entity class file:
        @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")
        
        The following statement obtaines the results from the named database query.
         */
        List<UserFile> userFiles = em.createNamedQuery("UserFile.findUserFilesByUserId")
                .setParameter("userId", userID)
                .getResultList();

        return userFiles;
    }

    /**
     *
     * @param file_name
     * @return a list of object references of userFiles with the name file_name
     */
    public List<UserFile> findByFilename(String file_name) {
        /*
        The following @NamedQuery definition is given in UserFile.java entity class file:
        @NamedQuery(name = "UserFile.findByFilename", query = "SELECT u FROM UserFile u WHERE u.filename = :filename")
        
        The following statement obtaines the results from the named database query.
         */
        List<UserFile> files = em.createNamedQuery("UserFile.findByFilename")
                .setParameter("filename", file_name)
                .getResultList();

        return files;
    }
    
}
