package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Bonuses.class)
public abstract class Bonuses_ {

	public static volatile SingularAttribute<Bonuses, Date> dateStBonus;
	public static volatile SingularAttribute<Bonuses, BonusTypes> bonusTypes;
	public static volatile SingularAttribute<Bonuses, Users> manager;
	public static volatile SingularAttribute<Bonuses, Float> summa;
	public static volatile SingularAttribute<Bonuses, Users> rentor;
	public static volatile SingularAttribute<Bonuses, BigInteger> id;
	public static volatile SingularAttribute<Bonuses, Integer> countMonths;
	public static volatile SingularAttribute<Bonuses, Facilities> facility;

}

