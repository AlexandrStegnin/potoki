package com.art.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InvestorsCash.class)
public abstract class InvestorsCash_ {

	public static volatile SingularAttribute<InvestorsCash, InvestorsTypes> investorsType;
	public static volatile SingularAttribute<InvestorsCash, BigInteger> sourceId;
	public static volatile SingularAttribute<InvestorsCash, CashTypes> cashType;
	public static volatile SingularAttribute<InvestorsCash, BigInteger> facilityId;
	public static volatile SingularAttribute<InvestorsCash, UnderFacilities> underFacility;
	public static volatile SingularAttribute<InvestorsCash, NewCashDetails> newCashDetails;
	public static volatile SingularAttribute<InvestorsCash, Facilities> sourceFacility;
	public static volatile SingularAttribute<InvestorsCash, Date> dateClosingInvest;
	public static volatile SingularAttribute<InvestorsCash, BigInteger> investorId;
	public static volatile SingularAttribute<InvestorsCash, TypeClosingInvest> typeClosingInvest;
	public static volatile SingularAttribute<InvestorsCash, String> source;
	public static volatile SingularAttribute<InvestorsCash, Rooms> room;
	public static volatile SingularAttribute<InvestorsCash, Date> dateReport;
	public static volatile SingularAttribute<InvestorsCash, Date> dateGivedCash;
	public static volatile SingularAttribute<InvestorsCash, Users> investor;
	public static volatile SingularAttribute<InvestorsCash, Integer> isDivide;
	public static volatile SingularAttribute<InvestorsCash, UnderFacilities> sourceUnderFacility;
	public static volatile SingularAttribute<InvestorsCash, CashSources> cashSource;
	public static volatile SingularAttribute<InvestorsCash, Integer> isReinvest;
	public static volatile SingularAttribute<InvestorsCash, String> sourceFlowsId;
	public static volatile SingularAttribute<InvestorsCash, BigInteger> id;
	public static volatile SingularAttribute<InvestorsCash, BigDecimal> givedCash;
	public static volatile SingularAttribute<InvestorsCash, ShareKind> shareKind;
	public static volatile SingularAttribute<InvestorsCash, Facilities> facility;

}

