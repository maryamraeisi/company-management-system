package com.example.demo;

import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.service.api.EmployeeService;
import com.example.demo.service.api.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);
    private boolean alreadySetup = false;
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("setting up data.");
        if (alreadySetup) {
            return;
        }

        createRoleIfNotExist("ROLE_MANAGER");
        createRoleIfNotExist("ROLE_EMPLOYEE");

        Role managerRole = roleService.findRoleByName("ROLE_MANAGER");
        Role employeeRole = roleService.findRoleByName("ROLE_EMPLOYEE");

        createEmployeeIfNotExist("maryam", "maryam", "raeisi", "password", "maryamemail@gmail.com", Arrays.asList(managerRole, employeeRole));
        createEmployeeIfNotExist("niloofar", "niloofar", "azizi", "password", "niloofaremail@gmail.com", Arrays.asList(managerRole, employeeRole));

        alreadySetup = true;
    }

    private void createEmployeeIfNotExist(String username, String firstName, String lastName, String password, String email, List<Role> roles) {
        Employee employee = employeeService.findEmployeeByUsername(username);
        if (Objects.isNull(employee)) {
            logger.info(String.format("creating employee. username: \"%s\"", username));
            employee = new Employee();
            employee.setUsername(username);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setPassword(passwordEncoder.encode(password));
            employee.setEmail(email);
            employee.setRoles(roles);
            employee.setEnabled(true);
            employeeService.saveEmployee(employee);
            logger.info(String.format("employee \"%s\" created.", username));
        }
    }

    private Role createRoleIfNotExist(String name) {
        Role role = roleService.findRoleByName(name);
        if (Objects.isNull(role)) {
            logger.info(String.format("creating \"%s\" role.", name));
            role = new Role(name);
            roleService.saveRole(role);
            logger.info(String.format("role \"%s\" created.", name));
        }
        return role;
    }
}
