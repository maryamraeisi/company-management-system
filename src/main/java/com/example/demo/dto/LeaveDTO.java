package com.example.demo.dto;

import com.example.demo.enums.LeaveType;
import com.example.demo.model.Employee;
import com.example.demo.model.Leave;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class LeaveDTO implements Serializable {

    private long id;
    private String startDateTime;
    private String endDateTime;
    private LeaveType leaveType;
    private Boolean isAccepted;
    private Employee employee;

//    public Boolean ifOverlaps(Leave leave) {
//        if (leave.getStartDateTime().before(this.startDateTime) && this.startDateTime.before(leave.getEndDateTime())) {
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public void setStartDate(String start) throws ParseException {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        this.startDate = dateFormat.parse(start);
//    }
//
//    public void setEndDate(String end) throws ParseException {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        this.endDate = dateFormat.parse(end);
//    }

//    public Boolean ifTimeIsRight(Date startDateTime, Date endDateTime){
//        return startDateTime.before(endDateTime);
//    }

}
