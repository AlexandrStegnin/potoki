package com.art.model;

import com.art.model.supporting.DebetCreditEnum;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AlphaCorrectTags.class)
public abstract class AlphaCorrectTags_ {

	public static volatile SingularAttribute<AlphaCorrectTags, String> correctTag;
	public static volatile SingularAttribute<AlphaCorrectTags, String> docNumber;
	public static volatile SingularAttribute<AlphaCorrectTags, Date> dateOper;
	public static volatile SingularAttribute<AlphaCorrectTags, DebetCreditEnum> debetOrCredit;
	public static volatile SingularAttribute<AlphaCorrectTags, String> inn;
	public static volatile SingularAttribute<AlphaCorrectTags, String> description;
	public static volatile SingularAttribute<AlphaCorrectTags, BigInteger> id;
	public static volatile SingularAttribute<AlphaCorrectTags, Facilities> facility;
	public static volatile SingularAttribute<AlphaCorrectTags, String> account;

}

