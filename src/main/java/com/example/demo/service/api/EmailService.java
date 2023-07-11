package com.example.demo.service.api;

import com.example.demo.dto.EmailDTO;
import com.example.demo.model.Email;
import com.example.demo.model.Employee;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import java.io.IOException;
import java.util.List;

public interface EmailService {
    void saveEmail(Email email);

    Email fillEmail(EmailDTO emailDTO);

    void sendEmail(EmailDTO emailDTO, MultipartFile attachedFile);

    List<EmailDTO> loadEmployeeInbox(String email) throws MessagingException, IOException;

    void receiveEmail();

    List<EmailDTO> loadEmployeeSentEmails(Long employeeId);
}
