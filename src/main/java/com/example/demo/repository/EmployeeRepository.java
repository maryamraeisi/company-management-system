package com.example.demo.repository;

import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee save(Employee employee);

    Employee findByUsername(String username);

    Employee findByEmail(String email);

    @Query("select e from Employee e join e.roles r where r.name = 'ROLE_MANAGER' and e.enabled = true")
    List<Employee> findManagers();

}
