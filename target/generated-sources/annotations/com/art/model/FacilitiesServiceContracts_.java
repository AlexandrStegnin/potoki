package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FacilitiesServiceContracts.class)
public abstract class FacilitiesServiceContracts_ {

	public static volatile SingularAttribute<FacilitiesServiceContracts, Float> area;
	public static volatile SingularAttribute<FacilitiesServiceContracts, Float> summPayment;
	public static volatile SingularAttribute<FacilitiesServiceContracts, Date> dateStartContract;
	public static volatile SingularAttribute<FacilitiesServiceContracts, String> comments;
	public static volatile SingularAttribute<FacilitiesServiceContracts, Users> rentor;
	public static volatile SingularAttribute<FacilitiesServiceContracts, String> contractNumber;
	public static volatile SingularAttribute<FacilitiesServiceContracts, Float> discount;
	public static volatile SingularAttribute<FacilitiesServiceContracts, Integer> timeDiscount;
	public static volatile SingularAttribute<FacilitiesServiceContracts, BigInteger> id;
	public static volatile SingularAttribute<FacilitiesServiceContracts, PaymentsMethod> paymentsMethod;
	public static volatile SingularAttribute<FacilitiesServiceContracts, Facilities> facility;
	public static volatile SingularAttribute<FacilitiesServiceContracts, Integer> dayToPay;

}

