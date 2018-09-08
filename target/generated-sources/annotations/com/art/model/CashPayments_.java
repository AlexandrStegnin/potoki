package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CashPayments.class)
public abstract class CashPayments_ {

	public static volatile SingularAttribute<CashPayments, Date> dateTransferCash;
	public static volatile SingularAttribute<CashPayments, Users> manager;
	public static volatile SingularAttribute<CashPayments, Float> summTransferCash;
	public static volatile SingularAttribute<CashPayments, BigInteger> id;
	public static volatile SingularAttribute<CashPayments, PaymentsMethod> paymentsMethod;
	public static volatile SingularAttribute<CashPayments, Facilities> facility;

}

