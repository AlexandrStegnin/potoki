package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FacilitiesReserves.class)
public abstract class FacilitiesReserves_ {

	public static volatile SingularAttribute<FacilitiesReserves, UnderFacilities> underFacility;
	public static volatile SingularAttribute<FacilitiesReserves, Float> summReserv;
	public static volatile SingularAttribute<FacilitiesReserves, BigInteger> id;
	public static volatile SingularAttribute<FacilitiesReserves, Date> dateEndReserv;
	public static volatile SingularAttribute<FacilitiesReserves, Facilities> facility;
	public static volatile SingularAttribute<FacilitiesReserves, Float> balanceReserv;
	public static volatile SingularAttribute<FacilitiesReserves, Date> dateCreateReserv;

}

