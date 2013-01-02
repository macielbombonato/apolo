package br.apolo.data.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AuditableBaseEntity.class)
public abstract class AuditableBaseEntity_ extends br.apolo.data.model.BaseEntity_ {

	public static volatile SingularAttribute<AuditableBaseEntity, Date> creationDate;
	public static volatile SingularAttribute<AuditableBaseEntity, User> createdBy;
	public static volatile SingularAttribute<AuditableBaseEntity, User> lastUpdatedBy;
	public static volatile SingularAttribute<AuditableBaseEntity, Date> lastUpdateDate;

}

