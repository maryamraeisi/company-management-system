package com.example.demo.model;

import com.example.demo.enums.LeaveType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    private Boolean isAccepted;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
