package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ToshlExtract.class)
public abstract class ToshlExtract_ {

	public static volatile SingularAttribute<ToshlExtract, Date> date;
	public static volatile SingularAttribute<ToshlExtract, ToshlCorrectTags> correctTag;
	public static volatile SingularAttribute<ToshlExtract, Float> amount;
	public static volatile SingularAttribute<ToshlExtract, String> mainCurrency;
	public static volatile SingularAttribute<ToshlExtract, Float> inMainCurrency;
	public static volatile SingularAttribute<ToshlExtract, String> description;
	public static volatile SingularAttribute<ToshlExtract, String> currency;
	public static volatile SingularAttribute<ToshlExtract, BigInteger> id;
	public static volatile SingularAttribute<ToshlExtract, String> category;
	public static volatile SingularAttribute<ToshlExtract, String> account;
	public static volatile SingularAttribute<ToshlExtract, String> tags;

}

