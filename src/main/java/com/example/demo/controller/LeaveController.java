package com.example.demo.controller;

import com.example.demo.constants.leave.LeaveErrors;
import com.example.demo.constants.leave.LeaveURLs;
import com.example.demo.dto.LeaveDTO;
import com.example.demo.enums.LeaveRequestCheck;
import com.example.demo.enums.LeaveType;
import com.example.demo.constants.TemplateNames;
import com.example.demo.model.Employee;
import com.example.demo.model.Leave;
import com.example.demo.service.api.EmployeeService;
import com.example.demo.service.api.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    @GetMapping(LeaveURLs.EMPLOYEE_LEAVES_RELATIVE_URL + "{username}")
    public String getCreateNewLeaveRequest(@PathVariable String username, Model model) {
        List<Leave> leaves = leaveService.loadEmployeeLeavesByEmployeeUsername(username);
        setModelAttributes(model, new LeaveDTO(), username, leaves, null);
        return TemplateNames.EMPLOYEE_LEAVES;
    }

    @PostMapping(LeaveURLs.EMPLOYEE_LEAVES_RELATIVE_URL + "{username}")
    public String postCreateNewLeaveRequest(@PathVariable String username, @Valid LeaveDTO leaveDTO, BindingResult result, Model model) {
        List<Leave> leaves = leaveService.loadEmployeeLeavesByEmployeeUsername(username);
        if (result.hasErrors()) {
            setModelAttributes(model, leaveDTO, username, leaves, null);
            return TemplateNames.EMPLOYEE_LEAVES;
        }
        Employee employee = employeeService.findEmployeeByUsername(username);
        try {
            LeaveRequestCheck leaveRequestCheck = leaveService.checkValidity(leaveDTO, username, model);
            if (leaveRequestCheck == LeaveRequestCheck.WRONG_DATE) {
                setModelAttributes(model, leaveDTO, username, leaves, LeaveErrors.WRONG_DATES);
                return TemplateNames.EMPLOYEE_LEAVES;
            } else if (leaveRequestCheck == LeaveRequestCheck.OVERLAP) {
                setModelAttributes(model, leaveDTO, username, leaves, LeaveErrors.DATES_OVERLAP);
                return TemplateNames.EMPLOYEE_LEAVES;
            } else if (leaveRequestCheck == LeaveRequestCheck.LIMIT_EXCEED) {
                setModelAttributes(model, leaveDTO, username, leaves, LeaveErrors.EXCEEDING_LIMIT);
                return TemplateNames.EMPLOYEE_LEAVES;
            }
            leaveDTO.setEmployee(employee);
            leaveService.saveLeave(leaveDTO);
            List<Leave> updatedLeaves = leaveService.loadEmployeeLeavesByEmployeeUsername(username);
            setModelAttributes(model, new LeaveDTO(), username, updatedLeaves, null);
        } catch (Exception e) {
            setModelAttributes(model, leaveDTO, username, leaves, "There Was A Problem Submitting Your Request.");
            return TemplateNames.EMPLOYEE_LEAVES;
        }
        return TemplateNames.EMPLOYEE_LEAVES;
    }

    private void setModelAttributes(Model model, LeaveDTO leaveDTO, String username, List<Leave> leaves, String message) {
        model.addAttribute("leaveDTO", leaveDTO);
        model.addAttribute("leaveTypes", LeaveType.values());
        model.addAttribute("username", username);
        model.addAttribute("leaves", leaves);
        model.addAttribute("errorMessage", message);
    }

    @GetMapping(LeaveURLs.DELETE_LEAVE_RELATIVE_URL + "{username}/{leaveId}")
    public String getDeleteLeaveRequest(@PathVariable String username, @PathVariable long leaveId, Model model) {
        Leave deletedLeave = leaveService.deleteLeaveById(leaveId);
        List<Leave> leaves = leaveService.loadEmployeeLeavesByEmployeeUsername(username);
        setModelAttributes(model, new LeaveDTO(), username, leaves, null);
        return "redirect:" + LeaveURLs.EMPLOYEE_LEAVES_FULL_URL + username;
    }


    @GetMapping( LeaveURLs.EMPLOYEE_LEAVES_FOR_MANAGER_RELATIVE_URL + "{managerUsername}")
    public String getCheckLeaveRequests(@PathVariable String managerUsername, Model model) {
        List<Leave> leaves = leaveService.loadEmployeeLeaveRequestsForManager(managerUsername);
        model.addAttribute("leaves", leaves);
        model.addAttribute("username", managerUsername);
        return TemplateNames.EMPLOYEE_LEAVES_FOR_MANAGER;
    }

    @GetMapping(LeaveURLs.UPDATE_LEAVE_STATUS_RELATIVE_URL + "{managerUsername}/{leaveId}")
    public String postUpdateLeaveRequestStatus(@RequestParam(value = "isAccepted") boolean isAccepted, @PathVariable long leaveId, @PathVariable String managerUsername, Model model) {
        leaveService.updateLeaveStatus(leaveId, isAccepted);
        List<Leave> leaves = leaveService.loadEmployeeLeaveRequestsForManager(managerUsername);
        model.addAttribute("leaves", leaves);
        model.addAttribute("username", managerUsername);
        return "redirect:" + LeaveURLs.EMPLOYEE_LEAVES_FOR_MANAGER_FULL_URL + managerUsername;
    }
}
