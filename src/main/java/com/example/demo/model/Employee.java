package com.example.demo.model;

import com.example.demo.enums.EmployeeStatus;
import lombok.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private boolean enabled;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Leave> leaveList = new ArrayList<>();
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Email> emailList = new ArrayList<>();
}

