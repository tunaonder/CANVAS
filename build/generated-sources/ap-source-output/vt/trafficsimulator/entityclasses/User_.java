package vt.trafficsimulator.entityclasses;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import vt.trafficsimulator.entityclasses.MapImage;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-28T11:35:25")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, String> lastName;
    public static volatile CollectionAttribute<User, MapImage> mapImageCollection;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Integer> id;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> username;

}