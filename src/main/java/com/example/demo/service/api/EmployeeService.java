package com.example.demo.service.api;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.enums.EmployeeStatus;
import com.example.demo.model.Employee;

import java.util.List;

public interface EmployeeService {

    void saveEmployee(Employee employee);

    Employee fillEmployee(EmployeeDTO employeeDTO);

    Employee findEmployeeByUsername(String username);

    Employee findEmployeeByEmail(String email);

    List<Employee> findManagers();

    EmployeeDTO createEmployeeDTO(String username);

    String loadEmployeeEmailByUsername(String username);

    Long loadEmployeeIdByUsername(String username);

    void updateEmployeeStatus(Employee employee, EmployeeStatus employeeStatus);
}
