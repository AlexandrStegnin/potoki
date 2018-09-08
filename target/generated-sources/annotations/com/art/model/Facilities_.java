package com.art.model;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Facilities.class)
public abstract class Facilities_ {

	public static volatile SingularAttribute<Facilities, String> address;
	public static volatile SingularAttribute<Facilities, Users> manager;
	public static volatile SingularAttribute<Facilities, String> city;
	public static volatile SetAttribute<Facilities, UnderFacilities> underFacilities;
	public static volatile SingularAttribute<Facilities, BigInteger> id;
	public static volatile SetAttribute<Facilities, Users> investors;
	public static volatile SingularAttribute<Facilities, String> facility;

}

