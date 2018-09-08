package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InvestorsFlows.class)
public abstract class InvestorsFlows_ {

	public static volatile SingularAttribute<InvestorsFlows, Float> shareForSvod;
	public static volatile SingularAttribute<InvestorsFlows, UnderFacilities> underFacilities;
	public static volatile SingularAttribute<InvestorsFlows, Float> afterTax;
	public static volatile SingularAttribute<InvestorsFlows, Rooms> room;
	public static volatile SingularAttribute<InvestorsFlows, Users> investor;
	public static volatile SingularAttribute<InvestorsFlows, Date> reportDate;
	public static volatile SingularAttribute<InvestorsFlows, Float> summa;
	public static volatile SingularAttribute<InvestorsFlows, Float> sumInUnderFacility;
	public static volatile SingularAttribute<InvestorsFlows, Integer> isReinvest;
	public static volatile SingularAttribute<InvestorsFlows, Facilities> reFacility;
	public static volatile SingularAttribute<InvestorsFlows, Float> share;
	public static volatile SingularAttribute<InvestorsFlows, String> reInvest;
	public static volatile SingularAttribute<InvestorsFlows, BigInteger> id;
	public static volatile SingularAttribute<InvestorsFlows, Float> givedCash;
	public static volatile SingularAttribute<InvestorsFlows, Float> cashing;
	public static volatile SingularAttribute<InvestorsFlows, Float> afterCashing;
	public static volatile SingularAttribute<InvestorsFlows, String> shareKind;
	public static volatile SingularAttribute<InvestorsFlows, Float> onInvestors;
	public static volatile SingularAttribute<InvestorsFlows, Float> afterDeductionEmptyFacility;
	public static volatile SingularAttribute<InvestorsFlows, Facilities> facility;
	public static volatile SingularAttribute<InvestorsFlows, Float> taxation;

}

