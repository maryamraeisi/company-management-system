package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private boolean enabled;
    private Boolean sent;
    private String from;
    private String to;
    private String subject;
    private String body;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] attachment;
}
