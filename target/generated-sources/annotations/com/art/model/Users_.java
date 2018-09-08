package com.art.model;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Users.class)
public abstract class Users_ {

	public static volatile SingularAttribute<Users, String> lastName;
	public static volatile SetAttribute<Users, MailingGroups> mailingGroups;
	public static volatile ListAttribute<Users, Roles> roles;
	public static volatile SingularAttribute<Users, String> login;
	public static volatile SingularAttribute<Users, String> middle_name;
	public static volatile SingularAttribute<Users, BigInteger> stuffId;
	public static volatile SetAttribute<Users, Emails> emails;
	public static volatile SingularAttribute<Users, Integer> office_id;
	public static volatile SingularAttribute<Users, String> password;
	public static volatile SingularAttribute<Users, Integer> officeId;
	public static volatile SingularAttribute<Users, Stuffs> userStuff;
	public static volatile ListAttribute<Users, UsersAnnexToContracts> usersAnnexToContractsList;
	public static volatile SingularAttribute<Users, BigInteger> id;
	public static volatile SingularAttribute<Users, String> state;
	public static volatile SetAttribute<Users, Facilities> facilities;
	public static volatile SingularAttribute<Users, String> first_name;
	public static volatile SingularAttribute<Users, String> email;

}

