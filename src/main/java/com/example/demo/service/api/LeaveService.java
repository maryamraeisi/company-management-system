package com.example.demo.service.api;

import com.example.demo.dto.LeaveDTO;
import com.example.demo.enums.LeaveRequestCheck;
import com.example.demo.model.Leave;
import org.springframework.ui.Model;
import java.util.List;

public interface LeaveService {

    List<Leave> loadEmployeeLeavesByEmployeeUsername(String username);

    void saveLeave(Leave leave);

    Leave fillLeave(LeaveDTO leaveDTO);

    LeaveRequestCheck checkValidity(LeaveDTO leaveDTO, String username, Model model);

    List<Leave> loadNonRejectedEmployeeLeavesByEmployeeUsername(String username);

    void deleteLeaveById(long leaveId);

    List<Leave> loadEmployeeLeaveRequestsForManager(String managerUsername);

    void updateLeaveStatus(long leaveId, boolean isAccepted);
}
