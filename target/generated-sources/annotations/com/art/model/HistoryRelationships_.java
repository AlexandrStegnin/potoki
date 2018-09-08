package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(HistoryRelationships.class)
public abstract class HistoryRelationships_ {

	public static volatile SingularAttribute<HistoryRelationships, Float> summ_fact;
	public static volatile SingularAttribute<HistoryRelationships, Date> period;
	public static volatile SingularAttribute<HistoryRelationships, Users> manager;
	public static volatile SingularAttribute<HistoryRelationships, Date> pay_date_fact;
	public static volatile SingularAttribute<HistoryRelationships, Users> rentor;
	public static volatile SingularAttribute<HistoryRelationships, PaymentsMethod> paymentsMethod;
	public static volatile SingularAttribute<HistoryRelationships, PaymentsType> paymentsType;
	public static volatile SingularAttribute<HistoryRelationships, BigInteger> id;
	public static volatile SingularAttribute<HistoryRelationships, Facilities> facility;

}

