package com.example.demo.dto;

import lombok.*;
import java.io.Serializable;

@Data
public class EmailDTO implements Serializable {
    private Long id;
    private Boolean enabled;
    private Boolean sent;
    private String from;
    private String to;
    private String subject = "no subject";
    private String body;
    private EmployeeDTO employeeDTO;
    private String encodedAttachment;
}
