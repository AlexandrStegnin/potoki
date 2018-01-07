package com.art.repository;

import com.art.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findById(Integer id);
    Roles findByRole(String role);
}
