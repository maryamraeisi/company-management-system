package com.example.demo.service.impl;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.api.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Override
    public Role findRoleById(long id) {
        Role role = roleRepository.findById(id);
        return role;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    @Override
    public Role findRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        return role;
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
