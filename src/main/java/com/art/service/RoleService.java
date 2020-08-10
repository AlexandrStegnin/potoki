package com.art.service;

import com.art.config.application.Constant;
import com.art.model.AppRole;
import com.art.repository.RoleRepository;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(Constant.ROLES_CACHE_KEY)
    public List<AppRole> findAll() {
        return roleRepository.findAll();
    }

    @Cacheable(Constant.ROLES_CACHE_KEY)
    public AppRole findById(Long id) {
        return roleRepository.findOne(id);
    }

}
