package com.art.model;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UsersAnnexToContracts.class)
public abstract class UsersAnnexToContracts_ {

	public static volatile SingularAttribute<UsersAnnexToContracts, Integer> annexRead;
	public static volatile SingularAttribute<UsersAnnexToContracts, BigInteger> id;
	public static volatile SingularAttribute<UsersAnnexToContracts, BigInteger> userId;
	public static volatile SingularAttribute<UsersAnnexToContracts, Date> dateRead;
	public static volatile SingularAttribute<UsersAnnexToContracts, AnnexToContracts> annex;

}

