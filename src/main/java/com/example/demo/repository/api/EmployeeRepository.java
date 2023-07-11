package com.example.demo.repository.api;

import com.example.demo.model.Employee;
import java.util.List;

public interface EmployeeRepository {

    Employee saveEmployee(Employee employee);

    Employee findEmployeeByUsername(String username);

    Employee findEmployeeByEmail(String email);

    List<Employee> findManagers();

    Employee findEmployeeById(long id);

    String loadEmployeeEmailByUsername(String username);

    Long loadEmployeeIdByUsername(String username);
}
