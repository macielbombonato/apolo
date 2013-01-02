package br.apolo.data.model;

import br.apolo.data.enums.DatabaseTransactionType;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AuditLog.class)
public abstract class AuditLog_ extends br.apolo.data.model.BaseEntity_ {

	public static volatile SingularAttribute<AuditLog, DatabaseTransactionType> transactionType;
	public static volatile SingularAttribute<AuditLog, Long> executedById;
	public static volatile SingularAttribute<AuditLog, Long> registryId;
	public static volatile SingularAttribute<AuditLog, Date> operationDate;
	public static volatile SingularAttribute<AuditLog, String> entityName;

}

