package com.art.model;

import com.art.model.supporting.BuySalesEnum;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FacilitiesBuySales.class)
public abstract class FacilitiesBuySales_ {

	public static volatile SingularAttribute<FacilitiesBuySales, UnderFacilities> underFacility;
	public static volatile SingularAttribute<FacilitiesBuySales, Float> summa;
	public static volatile SingularAttribute<FacilitiesBuySales, Date> dateGived;
	public static volatile SingularAttribute<FacilitiesBuySales, BuySalesEnum> operationType;
	public static volatile SingularAttribute<FacilitiesBuySales, BigInteger> id;
	public static volatile SingularAttribute<FacilitiesBuySales, Facilities> facility;

}

