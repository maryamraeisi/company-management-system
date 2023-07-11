package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.enums.EmployeeStatus;
import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.repository.api.EmployeeRepository;
import com.example.demo.service.api.EmployeeService;
import com.example.demo.service.api.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.saveEmployee(employee);
    }

    @Override
    public Employee fillEmployee(EmployeeDTO employeeDTO) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        Role role = roleService.findRoleById(employeeDTO.getRoleId());
        Employee manager = employeeRepository.findEmployeeById(employeeDTO.getManagerId());
        String password = passwordEncoder.encode(employeeDTO.getPassword());
        List<Role> roles = new LinkedList<>(Arrays.asList(role));
        if (role.getName().equals("ROLE_MANAGER")) {
            Role employeeRole = roleService.findRoleByName("ROLE_EMPLOYEE");
            roles.add(employeeRole);
        }
        employee.setEnabled(true);
        employee.setPassword(password);
        employee.setRoles(roles);
        employee.setManager(manager);
        return employee;
    }

    @Override
    public Employee findEmployeeByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findEmployeeByUsername(username);
    }

    @Override
    public Employee findEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findEmployeeByEmail(email);
        return employee;
    }

    @Override
    public List<Employee> findManagers() {
        List<Employee> managers = employeeRepository.findManagers();
        return managers;
    }

    @Override
    public EmployeeDTO createEmployeeDTO(String username) {
        Employee employee = findEmployeeByUsername(username);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPassword(employee.getPassword());
        employeeDTO.setUsername(employee.getUsername());
        employeeDTO.setEmail(employee.getEmail());
        if (Objects.nonNull(employee.getManager())) {
            employeeDTO.setManagerId(employee.getManager().getId());
        }
        List<Role> roles = (List<Role>) employee.getRoles();
        Optional<Role> managerRole = roles.stream().filter(role -> role.getName().equals("ROLE_MANAGER")).findAny();
        if (managerRole.isPresent()) {
            employeeDTO.setIsManager(true);
        }
        return employeeDTO;
    }

    @Override
    public String loadEmployeeEmailByUsername(String username) {
        return employeeRepository.loadEmployeeEmailByUsername(username);
    }

    @Override
    public Long loadEmployeeIdByUsername(String username) {
        return employeeRepository.loadEmployeeIdByUsername(username);
    }

    @Override
    public void updateEmployeeStatus(Employee employee, EmployeeStatus employeeStatus) {
        employeeRepository.updateEmployeeStatus(employee, employeeStatus);
    }
}
