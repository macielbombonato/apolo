package br.apolo.data.model;

import br.apolo.security.UserPermission;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserGroup.class)
public abstract class UserGroup_ extends br.apolo.data.model.AuditableBaseEntity_ {

	public static volatile SetAttribute<UserGroup, User> users;
	public static volatile SingularAttribute<UserGroup, String> name;
	public static volatile SetAttribute<UserGroup, UserPermission> permissions;

}

