package vt.canvas.entityclasses;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import vt.canvas.entityclasses.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-10T14:08:02")
@StaticMetamodel(Project.class)
public class Project_ { 

    public static volatile SingularAttribute<Project, String> projectname;
    public static volatile SingularAttribute<Project, Boolean> publicProject;
    public static volatile SingularAttribute<Project, Integer> id;
    public static volatile SingularAttribute<Project, String> mapname;
    public static volatile SingularAttribute<Project, User> userId;

}