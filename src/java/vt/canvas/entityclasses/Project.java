/*
 * Created by Sait Tuna Onder on 2018.04.18  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.entityclasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Onder
 */
@Entity
@Table(name = "Project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :id")
    , @NamedQuery(name = "Project.findByProjectname", query = "SELECT p FROM Project p WHERE p.projectname = :projectname")
    , @NamedQuery(name = "Project.findProjectsByUserId", query = "SELECT p FROM Project p WHERE p.userId.id = :userId")
    , @NamedQuery(name = "Project.findByMapname", query = "SELECT p FROM Project p WHERE p.mapname = :mapname")
    , @NamedQuery(name = "Project.findByPublicProject", query = "SELECT p FROM Project p WHERE p.publicProject = :publicProject")})

public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "projectname")
    private String projectname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "mapname")
    private String mapname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "publicProject")
    private boolean publicProject;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public Project() {
    }

    public Project(String projectname, String mapname, User id) {
        this.projectname = projectname;
        this.mapname = mapname;
        this.userId = id;
        this.publicProject = false;
    }

    public Project(Integer id) {
        this.id = id;
    }

    public Project(Integer id, String projectname, String mapname, boolean publicProject) {
        this.id = id;
        this.projectname = projectname;
        this.mapname = mapname;
        this.publicProject = publicProject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getMapname() {
        return mapname;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }

    public boolean getPublicProject() {
        return publicProject;
    }

    public void setPublicProject(boolean publicProject) {
        this.publicProject = publicProject;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vt.trafficsimulator.entityclasses.Project[ id=" + id + " ]";
    }
    
}
