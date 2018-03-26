package vt.trafficsimulator.entityclasses;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import vt.trafficsimulator.entityclasses.SimulationModel;
import vt.trafficsimulator.entityclasses.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-25T22:59:03")
@StaticMetamodel(Project.class)
public class Project_ { 

    public static volatile SingularAttribute<Project, String> projectname;
    public static volatile SingularAttribute<Project, Integer> id;
    public static volatile SingularAttribute<Project, String> mapname;
    public static volatile CollectionAttribute<Project, SimulationModel> simulationModelCollection;
    public static volatile SingularAttribute<Project, User> userId;

}