package com.art.repository;

import com.art.model.TypeClosing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */
@Repository
public interface TypeClosingRepository extends JpaRepository<TypeClosing, Long> {

  TypeClosing findByName(String name);

}
