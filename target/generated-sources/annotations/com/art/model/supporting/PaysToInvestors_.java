package com.art.model.supporting;

import com.art.model.Users;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaysToInvestors.class)
public abstract class PaysToInvestors_ {

	public static volatile SingularAttribute<PaysToInvestors, Date> endDate;
	public static volatile SingularAttribute<PaysToInvestors, BigInteger> investorId;
	public static volatile SingularAttribute<PaysToInvestors, Float> ostatokPosleVivoda;
	public static volatile SingularAttribute<PaysToInvestors, Users> investor;
	public static volatile SingularAttribute<PaysToInvestors, Float> ostatokPoDole;
	public static volatile SingularAttribute<PaysToInvestors, Float> summ;
	public static volatile SingularAttribute<PaysToInvestors, Float> factPay;
	public static volatile SingularAttribute<PaysToInvestors, String> aCorTag;
	public static volatile SingularAttribute<PaysToInvestors, Float> ostatokPosleNalogov;
	public static volatile SingularAttribute<PaysToInvestors, Float> ostatokPosleRashodov;
	public static volatile SingularAttribute<PaysToInvestors, BigInteger> id;
	public static volatile SingularAttribute<PaysToInvestors, Float> pribilSprodazhi;
	public static volatile SingularAttribute<PaysToInvestors, String> facility;

}

