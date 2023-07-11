package com.example.demo.service.impl;

import com.example.demo.enums.EmployeeStatus;
import com.example.demo.model.Employee;
import com.example.demo.service.api.EmployeeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.TimerTask;

@Getter
@Service
public class SetEmployeeStatusAtWork extends TimerTask {

    private Employee employee;
    @Autowired
    private EmployeeService employeeService;

    @Async
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void run() {
        employeeService.updateEmployeeStatus(employee, EmployeeStatus.AT_WORK);
    }
}
