package com.art.service;

import com.art.model.Roles;
import com.art.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Repository
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public List<Roles> findAll() {
        return roleRepository.findAll();
    }

    public Roles findById(Integer id) {
        return roleRepository.findById(id);
    }

    public Roles findByRole(String role) {
        return roleRepository.findByRole(role);
    }
}
