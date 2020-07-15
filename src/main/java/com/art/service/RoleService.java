package com.art.service;

import com.art.model.AppRole;
import com.art.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<AppRole> findAll() {
        return roleRepository.findAll();
    }

    public AppRole findById(Long id) {
        return roleRepository.findOne(id);
    }

}
