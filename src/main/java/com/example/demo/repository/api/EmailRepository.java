package com.example.demo.repository.api;

import com.example.demo.model.Email;

import java.util.List;

public interface EmailRepository {

    Email saveEmail(Email email);

    List<Email> loadEmployeeSentEmails(Long employeeId);
}
