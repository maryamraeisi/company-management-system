package com.example.demo.service.api;

import com.example.demo.model.Role;

import java.util.List;

public interface RoleService {
    Role findRoleById(long id);

    List<Role> findAll();

    Role findRoleByName(String name);

    void saveRole(Role role);
}
