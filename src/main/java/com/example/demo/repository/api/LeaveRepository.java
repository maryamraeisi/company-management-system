package com.example.demo.repository.api;

import com.example.demo.model.Leave;

import java.util.List;

public interface LeaveRepository {

    Leave saveLeave(Leave leave);

    List<Leave> loadEmployeeLeavesByEmployeeUsername(String username);

    Leave deleteLeaveById(long leaveId);

    List<Leave> loadNonRejectedEmployeeLeavesByEmployeeUsername(String username);

    List<Leave> loadEmployeeLeaveRequestsForManager(String managerUsername);

    void updateLeaveStatus(long leaveId, boolean isAccepted);
}
