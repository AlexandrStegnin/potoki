package com.art.service;

import com.art.model.AppRole;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.AppRoleDTO;
import com.art.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

//    @Cacheable(Constant.ROLES_CACHE_KEY)
    public List<AppRole> findAll() {
        return roleRepository.findAll();
    }

//    @Cacheable(Constant.ROLES_CACHE_KEY)
    public AppRole findById(Long id) {
        return roleRepository.findOne(id);
    }

    public List<AppRole> initializeRoles() {
        AppRole role = new AppRole();
        role.setId(0L);
        role.setHumanized("Выберите роль");
        List<AppRole> roles = new ArrayList<>();
        roles.add(role);
        roles.addAll(findAll());
        return roles;
    }

    /**
     * Создать роль
     *
     * @param dto DTO роли
     * @return ответ
     */
    public ApiResponse create(AppRoleDTO dto) {
        AppRole role = new AppRole(dto);
        roleRepository.save(role);
        return new ApiResponse("Роль " + role.getHumanized() + " успешно создана");
    }
}
