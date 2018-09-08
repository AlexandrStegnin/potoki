package com.art.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InvestorsFlowsSale.class)
public abstract class InvestorsFlowsSale_ {

	public static volatile SingularAttribute<InvestorsFlowsSale, BigDecimal> profitToCashingAuto;
	public static volatile SingularAttribute<InvestorsFlowsSale, UnderFacilities> underFacility;
	public static volatile SingularAttribute<InvestorsFlowsSale, BigDecimal> cashInFacility;
	public static volatile SingularAttribute<InvestorsFlowsSale, BigDecimal> profitToCashingMain;
	public static volatile SingularAttribute<InvestorsFlowsSale, Date> dateSale;
	public static volatile SingularAttribute<InvestorsFlowsSale, Users> investor;
	public static volatile SingularAttribute<InvestorsFlowsSale, BigDecimal> cashInUnderFacility;
	public static volatile SingularAttribute<InvestorsFlowsSale, BigDecimal> profitToReInvest;
	public static volatile SingularAttribute<InvestorsFlowsSale, Date> dateGived;
	public static volatile SingularAttribute<InvestorsFlowsSale, Integer> isReinvest;
	public static volatile SingularAttribute<InvestorsFlowsSale, BigInteger> id;
	public static volatile SingularAttribute<InvestorsFlowsSale, ShareKind> shareKind;
	public static volatile SingularAttribute<InvestorsFlowsSale, BigDecimal> investorShare;
	public static volatile SingularAttribute<InvestorsFlowsSale, Facilities> facility;

}

