package com.example.demo.repository.api;

import com.example.demo.model.Role;

import java.util.List;

public interface RoleRepository {

    Role findRoleByName(String name);

    Role saveRole(Role role);

    List<Role> loadAllRoles();

    Role findRoleById(long id);

}
