package com.art.repository;

import com.art.model.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface ClientTypeRepository extends JpaRepository<ClientType, Long> {

    ClientType findByTitle(String title);

}
