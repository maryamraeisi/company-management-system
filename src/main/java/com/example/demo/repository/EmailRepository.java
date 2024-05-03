package com.example.demo.repository;

import com.example.demo.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Email save(Email email);

    @Query("select e from Email e where e.employee.id = ?1 and e.enabled = true")
    List<Email> loadEmployeeSentEmails(Long employeeId);

    @Query("select e.email from Employee e where e.username = ?1 and e.enabled = true")
    String loadEmployeeEmailByUsername(String username);

}
