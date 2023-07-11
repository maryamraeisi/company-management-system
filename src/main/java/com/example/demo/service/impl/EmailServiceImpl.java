package com.example.demo.service.impl;

import com.example.demo.config.POP3ServerConfig;
import com.example.demo.config.SMTPServerConfig;
import com.example.demo.dto.EmailDTO;
import com.example.demo.model.Email;
import com.example.demo.model.Employee;
import com.example.demo.repository.api.EmailRepository;
import com.example.demo.service.api.EmailService;
import com.example.demo.service.api.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSenderImpl mailSender;
    private final EmailRepository emailRepository;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    public EmailServiceImpl(EmailRepository emailRepository, EmployeeService employeeService, ModelMapper modelMapper) {
        this.emailRepository = emailRepository;
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(SMTPServerConfig.username);
        mailSender.setPassword(SMTPServerConfig.password);
        mailSender.setPort(SMTPServerConfig.port);
        mailSender.setHost(SMTPServerConfig.host);
        mailSender.setProtocol("smtp");
    }

    @Override
    public void saveEmail(Email email) {
        emailRepository.saveEmail(email);
    }

    @Override
    public Email fillEmail(EmailDTO emailDTO) {
        Email email = modelMapper.map(emailDTO, Email.class);
        email.setEnabled(true);
        return email;
    }

    @Override
    @Async
    @Transactional
    public void sendEmail(EmailDTO emailDTO, MultipartFile attachedFile) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(emailDTO.getFrom());
            helper.setTo(emailDTO.getTo());
            helper.setSubject(emailDTO.getSubject());
            helper.setText(emailDTO.getBody(), true);
            helper.addAttachment(attachedFile.getName(), attachedFile);
            mailSender.send(mimeMessage);
            emailDTO.setSent(true);
            Employee employee = employeeService.findEmployeeByEmail(emailDTO.getFrom());
            Email email = fillEmail(emailDTO);
            email.setEmployee(employee);
            email.setAttachment(attachedFile.getBytes());
            saveEmail(email);
        } catch (MessagingException | IOException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Override
    public List<EmailDTO> loadEmployeeInbox(String email) throws MessagingException, IOException {
        List<EmailDTO> emailDTOList = new ArrayList<>();
        Properties properties = getPOP3Properties();
        Session session = Session.getInstance(properties);
        Store store = session.getStore("pop3");
        store.connect(POP3ServerConfig.host, POP3ServerConfig.port, POP3ServerConfig.username, POP3ServerConfig.password);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages();
        for (Message message : messages) {
            if (message.getRecipients(Message.RecipientType.TO)[0].toString().equals(email)) {
                EmailDTO emailDTO = new EmailDTO();
                emailDTO.setFrom(message.getFrom()[0].toString());
                emailDTO.setTo(email);
                emailDTO.setSubject(message.getSubject());
                if (message.getContent() instanceof Multipart) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int k = 0; k < multipart.getCount(); k++) {
                        emailDTO.setBody(multipart.getBodyPart(k).toString());
//                            InputStream stream = bodyPart.getInputStream();
//                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
//
//                            while (bufferedReader.ready()) {
//                                System.out.println(bufferedReader.readLine());
//                            }
//                            System.out.println();
                    }
                } else {
                    emailDTO.setBody((String) message.getContent());
                }
                emailDTOList.add(emailDTO);
            }
        }
        return emailDTOList;
    }

    @Override
    public List<EmailDTO> loadEmployeeSentEmails(Long employeeId) {
        List<Email> emails = emailRepository.loadEmployeeSentEmails(employeeId);
        List<EmailDTO> emailDTOList = new ArrayList<>();
        for (Email email : emails) {
            EmailDTO emailDTO = modelMapper.map(email, EmailDTO.class);
            emailDTOList.add(emailDTO);
        }
        return emailDTOList;
    }

    @Override
    public void receiveEmail() {
        Properties properties = getPOP3Properties();
        Session session = Session.getInstance(properties);
        try {
            Store store = session.getStore("pop3");
            store.connect(POP3ServerConfig.host, POP3ServerConfig.port, POP3ServerConfig.username, POP3ServerConfig.password);
            //Create the folder object and open it in your mailbox.
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //Retrieve the messages from the folder object.
            Message[] messages = emailFolder.getMessages();
            System.out.println("Total Message" + messages.length);

            //Iterate the messages
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
                System.out.println("---------------------------------");
                System.out.println("Details of Email Message " + (i + 1) + " :");
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);

                //Iterate recipients
                System.out.println("To: ");
                for (int j = 0; j < toAddress.length; j++) {
                    System.out.println(toAddress[j].toString());
                }

                //Iterate multiparts
                if (message.getContent() instanceof Multipart) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int k = 0; k < multipart.getCount(); k++) {
                        BodyPart bodyPart = multipart.getBodyPart(k);
                        InputStream stream = bodyPart.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                        while (bufferedReader.ready()) {
                            System.out.println(bufferedReader.readLine());
                        }
                        System.out.println();
                    }
                }
            }

            //close the folder and store objects
            emailFolder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Properties getPOP3Properties() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.store.host", POP3ServerConfig.host);
        properties.put("mail.store.port", POP3ServerConfig.port);
        properties.put("mail.store.starttls.enable", "true");
        return properties;
    }
}
