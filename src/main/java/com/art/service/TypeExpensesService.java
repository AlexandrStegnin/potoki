package com.art.service;

import com.art.model.TypeExpenses;
import com.art.repository.TypeExpensesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class TypeExpensesService {

    @Resource(name = "typeExpensesRepository")
    private TypeExpensesRepository typeExpensesRepository;

    public List<TypeExpenses> findAll() {
        return typeExpensesRepository.findAll();
    }

    public TypeExpenses create(TypeExpenses typeExpenses) {
        return typeExpensesRepository.saveAndFlush(typeExpenses);
    }

    public TypeExpenses update(TypeExpenses typeExpenses) {
        return typeExpensesRepository.saveAndFlush(typeExpenses);
    }

    public void delete(BigInteger id) {
        typeExpensesRepository.delete(id);
    }

    public TypeExpenses findById(BigInteger id) {
        return typeExpensesRepository.findOne(id);
    }

    public TypeExpenses findByTypeExp(String typeExp) {
        return typeExpensesRepository.findByTypeExp(typeExp);
    }

}
