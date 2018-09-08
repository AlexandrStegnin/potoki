package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Discounts.class)
public abstract class Discounts_ {

	public static volatile SingularAttribute<Discounts, Float> discount;
	public static volatile SingularAttribute<Discounts, BigInteger> id;
	public static volatile SingularAttribute<Discounts, Date> discount_end;
	public static volatile SingularAttribute<Discounts, Date> discount_start;
	public static volatile SingularAttribute<Discounts, Users> discountRentor;

}

