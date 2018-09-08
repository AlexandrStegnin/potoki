package com.art.model;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MailingGroups.class)
public abstract class MailingGroups_ {

	public static volatile SingularAttribute<MailingGroups, String> mailingGroup;
	public static volatile SingularAttribute<MailingGroups, BigInteger> id;
	public static volatile ListAttribute<MailingGroups, Users> users;

}

