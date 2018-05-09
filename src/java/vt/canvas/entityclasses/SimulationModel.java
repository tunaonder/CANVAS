/*
 * Created by Sait Tuna Onder on 2018.03.21  * 
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
@Table(name = "SimulationModel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SimulationModel.findAll", query = "SELECT s FROM SimulationModel s")
    , @NamedQuery(name = "SimulationModel.findById", query = "SELECT s FROM SimulationModel s WHERE s.id = :id")
    , @NamedQuery(name = "SimulationModel.findByModeldata", query = "SELECT s FROM SimulationModel s WHERE s.modeldata = :modeldata")
    , @NamedQuery(name = "SimulationModel.findSimulationModelByProjectId", query = "SELECT s FROM SimulationModel s WHERE s.projectId.id = :projectId")})
public class SimulationModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10000)
    @Column(name = "modeldata")
    private String modeldata;
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne
    private Project projectId;

    public SimulationModel() {
    }

    public SimulationModel(Integer id) {
        this.id = id;
    }

    public SimulationModel(Integer id, String modeldata) {
        this.id = id;
        this.modeldata = modeldata;
    }
    
    public SimulationModel(String modeldata, Project projectId){
        this.modeldata = modeldata;
        this.projectId = projectId;       
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModeldata() {
        return modeldata;
    }

    public void setModeldata(String modeldata) {
        this.modeldata = modeldata;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
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
        if (!(object instanceof SimulationModel)) {
            return false;
        }
        SimulationModel other = (SimulationModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vt.trafficsimulator.entityclasses.SimulationModel[ id=" + id + " ]";
    }
    
}
