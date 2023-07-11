package com.example.demo.dto;

import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class EmployeeDTO implements Serializable {

    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Long roleId;
    private Long managerId;
    private Boolean isManager;

}
