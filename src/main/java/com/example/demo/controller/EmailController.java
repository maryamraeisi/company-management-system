package com.example.demo.controller;

import com.example.demo.constants.email.EmailErrors;
import com.example.demo.constants.email.EmailURLs;
import com.example.demo.dto.EmailDTO;
import com.example.demo.constants.TemplateNames;
import com.example.demo.service.api.EmailService;
import com.example.demo.service.api.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(EmailController.class);
    private final EmployeeService employeeService;
    private final EmailService emailService;

    @GetMapping(EmailURLs.SEND_EMAIL_RELATIVE_URL + "{username}")
    public String getSendEmail(@PathVariable String username, Model model) {
        EmailDTO emailDTO = new EmailDTO();
        try {
            String employeeEmail = emailService.loadEmployeeEmailsByUsername(username);
//        List<EmailDTO> emailDTOList = emailService.loadEmployeeSentEmails(employeeId);
            List<EmailDTO> emailDTOList = emailService.loadEmployeeInbox(employeeEmail);
            emailDTO.setFrom(employeeEmail);
            model.addAttribute("emailDTO", emailDTO);
            model.addAttribute("username", username);
            model.addAttribute("emails", emailDTOList);
        } catch (MessagingException | IOException e) {
            logger.info(EmailErrors.COULD_NOT_CONNECT_TO_THE_EMAIL_SERVER);
            model.addAttribute("emailDTO", emailDTO);
            model.addAttribute("username", username);
            model.addAttribute("errorMessage", EmailErrors.COULD_NOT_CONNECT_TO_THE_EMAIL_SERVER);
        }
        return TemplateNames.EMPLOYEE_EMAILS;
    }

    @PostMapping(EmailURLs.SEND_EMAIL_RELATIVE_URL + "{username}")
    public String postSendEmail(@PathVariable String username, EmailDTO emailDTO, BindingResult bindingResult,
                                @RequestParam(value = "attachedFile") MultipartFile attachedFile, Model model) {
        if (bindingResult.hasErrors()) {
            getSendEmail(username, model);
        }
        emailService.sendEmail(emailDTO, attachedFile);
        getSendEmail(username, model);
        return TemplateNames.EMPLOYEE_EMAILS;
    }

}
