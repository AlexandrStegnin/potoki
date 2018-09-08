package com.art.model;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RentorsDetails.class)
public abstract class RentorsDetails_ {

	public static volatile SingularAttribute<RentorsDetails, String> organization;
	public static volatile SingularAttribute<RentorsDetails, Users> rentor;
	public static volatile SingularAttribute<RentorsDetails, String> inn;
	public static volatile SingularAttribute<RentorsDetails, BigInteger> id;
	public static volatile SingularAttribute<RentorsDetails, Facilities> facility;
	public static volatile SingularAttribute<RentorsDetails, String> moreInfo;
	public static volatile SingularAttribute<RentorsDetails, String> account;

}

