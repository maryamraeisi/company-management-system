package com.example.demo.service.impl;

import com.example.demo.dto.LeaveDTO;
import com.example.demo.enums.LeaveRequestCheck;
import com.example.demo.model.Leave;
import com.example.demo.repository.LeaveRepository;
import com.example.demo.service.api.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;
    private final int leaveRequestLimit = 10;

    @Override
    public List<Leave> loadEmployeeLeavesByEmployeeUsername(String username) {
        return leaveRepository.loadEmployeeLeavesByEmployeeUsername(username);
    }

    @Override
    public void saveLeave(Leave leave) {
        leaveRepository.save(leave);
    }

    @Override
    public Leave fillLeave(LeaveDTO leaveDTO) {
        Leave leave = new Leave();
        LocalDateTime startDateTime = LocalDateTime.parse(leaveDTO.getStartDateTime());
        LocalDateTime endDateTime = LocalDateTime.parse(leaveDTO.getEndDateTime());
        leave.setStartDateTime(Timestamp.valueOf(startDateTime));
        leave.setEndDateTime(Timestamp.valueOf(endDateTime));
        leave.setLeaveType(leaveDTO.getLeaveType());
        leave.setEnabled(true);
        return leave;
    }

    @Override
    public LeaveRequestCheck checkValidity(LeaveDTO leaveDTO, String username, Model model) {
        LocalDateTime startDateTime = LocalDateTime.parse(leaveDTO.getStartDateTime());
        LocalDateTime endDateTime = LocalDateTime.parse(leaveDTO.getEndDateTime());
        List<Leave> leaves = loadNonRejectedEmployeeLeavesByEmployeeUsername(username);
        if (startDateTime.isAfter(endDateTime)) {
            return LeaveRequestCheck.WRONG_DATE;
        }
        if (leaves.size() > leaveRequestLimit) {
            return LeaveRequestCheck.LIMIT_EXCEED;
        }
        for (Leave leave : leaves) {
            if ((leave.getStartDateTime().before(Timestamp.valueOf(startDateTime))
                    && Timestamp.valueOf(startDateTime).before(leave.getEndDateTime()))
                    || (Timestamp.valueOf(startDateTime).before(leave.getStartDateTime())
                    && leave.getStartDateTime().before(Timestamp.valueOf(endDateTime)))) {
                model.addAttribute("overlappedStartDateTime", "start date:" + leave.getStartDateTime().toString());
                model.addAttribute("overlappedEndDateTime", "end date:" + leave.getEndDateTime().toString());
                return LeaveRequestCheck.OVERLAP;
            }
        }
        return LeaveRequestCheck.OK;
    }

    @Override
    public List<Leave> loadNonRejectedEmployeeLeavesByEmployeeUsername(String username) {
        return leaveRepository.loadNonRejectedEmployeeLeavesByEmployeeUsername(username);
    }

    @Override
    public void deleteLeaveById(long leaveId) {
        leaveRepository.deleteById(leaveId);
    }

    @Override
    public List<Leave> loadEmployeeLeaveRequestsForManager(String managerUsername) {
        return leaveRepository.loadEmployeeLeaveRequestsForManager(managerUsername);
    }

    @Override
    public void updateLeaveStatus(long leaveId, boolean isAccepted) {
        leaveRepository.updateLeaveStatus(leaveId, isAccepted);
    }

}
