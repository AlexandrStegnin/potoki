package com.art.model;

import java.math.BigInteger;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InvestorsShare.class)
public abstract class InvestorsShare_ {

	public static volatile SingularAttribute<InvestorsShare, Float> tempExpenses;
	public static volatile SingularAttribute<InvestorsShare, BigInteger> facilityId;
	public static volatile SingularAttribute<InvestorsShare, Date> dateStManExp;
	public static volatile SingularAttribute<InvestorsShare, Float> taxationExpenses;
	public static volatile SingularAttribute<InvestorsShare, Float> cashingExpenses;
	public static volatile SingularAttribute<InvestorsShare, BigInteger> investorId;
	public static volatile SingularAttribute<InvestorsShare, Float> managerExpenses;
	public static volatile SingularAttribute<InvestorsShare, Date> dateStTempExp;
	public static volatile SingularAttribute<InvestorsShare, Users> investor;
	public static volatile SingularAttribute<InvestorsShare, Float> ipManagerExpenses;
	public static volatile SingularAttribute<InvestorsShare, Float> share;
	public static volatile SingularAttribute<InvestorsShare, Date> dateEndManExp;
	public static volatile SingularAttribute<InvestorsShare, BigInteger> id;
	public static volatile SingularAttribute<InvestorsShare, Facilities> facility;
	public static volatile SingularAttribute<InvestorsShare, Date> dateEndTempExp;

}

