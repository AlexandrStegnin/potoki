package com.art.repository;

import com.art.model.supporting.ServiceUnavailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
@Repository
public interface ServiceUnavailableRepository extends JpaRepository<ServiceUnavailable, BigInteger> {
    ServiceUnavailable findFirstByIdIsNotNull();
    ServiceUnavailable findById(BigInteger id);
}
