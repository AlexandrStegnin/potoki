package com.art.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersistentLogin.class)
public abstract class PersistentLogin_ {

	public static volatile SingularAttribute<PersistentLogin, String> series;
	public static volatile SingularAttribute<PersistentLogin, Date> last_used;
	public static volatile SingularAttribute<PersistentLogin, String> username;
	public static volatile SingularAttribute<PersistentLogin, String> token;

}

