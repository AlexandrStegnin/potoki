package com.art.model;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Emails.class)
public abstract class Emails_ {

	public static volatile SingularAttribute<Emails, BigInteger> id;
	public static volatile SingularAttribute<Emails, Users> user;
	public static volatile SingularAttribute<Emails, String> email;

}

