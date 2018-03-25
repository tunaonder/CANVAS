package vt.trafficsimulator.entityclasses;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import vt.trafficsimulator.entityclasses.Project;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-25T18:10:01")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Integer> id;
    public static volatile CollectionAttribute<User, Project> projectCollection;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> username;

}