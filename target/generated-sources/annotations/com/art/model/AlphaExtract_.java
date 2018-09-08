package com.art.model;

import java.math.BigInteger;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AlphaExtract.class)
public abstract class AlphaExtract_ {

	public static volatile SingularAttribute<AlphaExtract, Float> debet;
	public static volatile SingularAttribute<AlphaExtract, Date> period;
	public static volatile SingularAttribute<AlphaExtract, String> orgName;
	public static volatile SingularAttribute<AlphaExtract, String> correctOrgName;
	public static volatile SingularAttribute<AlphaExtract, Date> dateOper;
	public static volatile SingularAttribute<AlphaExtract, String> docNumber;
	public static volatile SingularAttribute<AlphaExtract, String> bik;
	public static volatile SingularAttribute<AlphaExtract, String> docType;
	public static volatile SingularAttribute<AlphaExtract, String> inn;
	public static volatile SingularAttribute<AlphaExtract, String> pId;
	public static volatile SingularAttribute<AlphaExtract, String> kpp;
	public static volatile SingularAttribute<AlphaExtract, String> bankName;
	public static volatile SingularAttribute<AlphaExtract, String> codeDebet;
	public static volatile SingularAttribute<AlphaExtract, AlphaCorrectTags> tags;
	public static volatile SingularAttribute<AlphaExtract, BigInteger> id;
	public static volatile SingularAttribute<AlphaExtract, Float> credit;
	public static volatile SingularAttribute<AlphaExtract, String> purposePayment;
	public static volatile SingularAttribute<AlphaExtract, String> account;

}

