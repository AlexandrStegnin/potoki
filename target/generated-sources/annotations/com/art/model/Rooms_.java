package com.art.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Rooms.class)
public abstract class Rooms_ {

	public static volatile SingularAttribute<Rooms, BigDecimal> coast;
	public static volatile SingularAttribute<Rooms, Boolean> sold;
	public static volatile SingularAttribute<Rooms, UnderFacilities> underFacility;
	public static volatile SingularAttribute<Rooms, LocalDate> dateOfSale;
	public static volatile SingularAttribute<Rooms, BigDecimal> roomSize;
	public static volatile SingularAttribute<Rooms, BigInteger> id;
	public static volatile SingularAttribute<Rooms, String> room;

}

