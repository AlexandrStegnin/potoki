package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AllowanceIp.class)
public abstract class AllowanceIp_ {

	public static volatile SingularAttribute<AllowanceIp, Users> investor;
	public static volatile SingularAttribute<AllowanceIp, UnderFacilities> underFacility;
	public static volatile SingularAttribute<AllowanceIp, Date> dateStart;
	public static volatile SingularAttribute<AllowanceIp, Float> allowance;
	public static volatile SingularAttribute<AllowanceIp, BigInteger> id;
	public static volatile SingularAttribute<AllowanceIp, Facilities> facility;

}

