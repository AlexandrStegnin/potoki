package com.art.repository;

import com.art.model.TypeExpenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TypeExpensesRepository extends JpaRepository<TypeExpenses, BigInteger>{

    TypeExpenses findByTypeExp(String typeExp);

}
