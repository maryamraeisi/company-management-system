package com.example.demo.config;

import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.service.api.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findEmployeeByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("No such employee: " + username);
        }
        List<GrantedAuthority> employeeRoles = new ArrayList<>();
        for (Role role : employee.getRoles()) {
            employeeRoles.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(employee.getUsername(), employee.getPassword(), employeeRoles);
    }
}
