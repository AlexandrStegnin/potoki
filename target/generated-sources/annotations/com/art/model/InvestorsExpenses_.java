package com.art.model;

import com.art.model.supporting.InvestorsExpEnum;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InvestorsExpenses.class)
public abstract class InvestorsExpenses_ {

	public static volatile SingularAttribute<InvestorsExpenses, InvestorsExpEnum> classExp;
	public static volatile SingularAttribute<InvestorsExpenses, TypeExpenses> typeExpenses;
	public static volatile SingularAttribute<InvestorsExpenses, Users> investor;
	public static volatile SingularAttribute<InvestorsExpenses, Float> sizeExp;
	public static volatile SingularAttribute<InvestorsExpenses, Date> dateEndExp;
	public static volatile SingularAttribute<InvestorsExpenses, BigInteger> id;
	public static volatile SingularAttribute<InvestorsExpenses, Facilities> facility;
	public static volatile SingularAttribute<InvestorsExpenses, Date> dateStExp;

}

