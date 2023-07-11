package com.example.demo.dto;

import com.example.demo.enums.LeaveType;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class LeaveDTO implements Serializable {

    private long id;
    private String startDateTime;
    private String endDateTime;
    private LeaveType leaveType;
    private Boolean isAccepted;

}
