package com.art.model;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UnderFacilities.class)
public abstract class UnderFacilities_ {

	public static volatile SetAttribute<UnderFacilities, Rooms> rooms;
	public static volatile SingularAttribute<UnderFacilities, BigInteger> facilityId;
	public static volatile SingularAttribute<UnderFacilities, String> underFacility;
	public static volatile SingularAttribute<UnderFacilities, BigInteger> id;
	public static volatile SingularAttribute<UnderFacilities, Facilities> facility;

}

