package com.art.model;

import java.math.BigInteger;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PasswordResetToken.class)
public abstract class PasswordResetToken_ {

	public static volatile SingularAttribute<PasswordResetToken, Date> expiryDate;
	public static volatile SingularAttribute<PasswordResetToken, BigInteger> id;
	public static volatile SingularAttribute<PasswordResetToken, Users> users;
	public static volatile SingularAttribute<PasswordResetToken, String> token;

}

