package vt.trafficsimulator.entityclasses;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import vt.trafficsimulator.entityclasses.Project;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-04-16T00:52:17")
@StaticMetamodel(SimulationModel.class)
public class SimulationModel_ { 

    public static volatile SingularAttribute<SimulationModel, Integer> id;
    public static volatile SingularAttribute<SimulationModel, String> modeldata;
    public static volatile SingularAttribute<SimulationModel, Project> projectId;

}