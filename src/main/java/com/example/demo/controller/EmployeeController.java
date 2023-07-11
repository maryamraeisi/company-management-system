package com.example.demo.controller;

import com.example.demo.constants.employee.EmployeeURLs;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.constants.employee.EmployeeErrors;
import com.example.demo.constants.TemplateNames;
import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.service.api.EmployeeService;
import com.example.demo.service.api.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @GetMapping(EmployeeURLs.EMPLOYEE_SIGN_UP_RELATIVE_URL)
    public String getSignUp(Model model) {
        setModelAttributes(model, new EmployeeDTO(), roleService.findAll(), employeeService.findManagers(), null);
        return TemplateNames.EMPLOYEE_SIGN_UP;
    }

    @PostMapping(EmployeeURLs.EMPLOYEE_SIGN_UP_RELATIVE_URL)
    public String postSignUp(@Valid EmployeeDTO employeeDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setModelAttributes(model, employeeDTO, roleService.findAll(), employeeService.findManagers(), null);
            return TemplateNames.EMPLOYEE_SIGN_UP;
        }
        try {
            if (Objects.nonNull(employeeService.findEmployeeByUsername(employeeDTO.getUsername()))) {
                throw new Exception(String.format(EmployeeErrors.USERNAME_ALREADY_EXISTS, employeeDTO.getUsername()));
            } else {
                if (Objects.nonNull(employeeService.findEmployeeByEmail(employeeDTO.getEmail()))) {
                    throw new Exception(String.format(EmployeeErrors.EMAIL_IS_TAKEN, employeeDTO.getEmail()));
                } else {
                    Employee employee = employeeService.fillEmployee(employeeDTO);
                    employeeService.saveEmployee(employee);
                    setModelAttributes(model, null, null, null, "Hi " + employeeDTO.getFirstName());
                    return "redirect:" + EmployeeURLs.EMPLOYEE_PROFILE_FULL_URL + employeeDTO.getUsername();
                }
            }
        } catch (Exception e) {
            setModelAttributes(model, employeeDTO, roleService.findAll(), employeeService.findManagers(), String.format(EmployeeErrors.ERROR_IN_SIGNING_UP, e.getMessage()));
            return TemplateNames.EMPLOYEE_SIGN_UP;
        }
    }

    @GetMapping(EmployeeURLs.EMPLOYEE_SIGN_IN_RELATIVE_URL)
    public String getSignIn(Model model) {
        setModelAttributes(model, new EmployeeDTO(), null, null, null);
        return TemplateNames.EMPLOYEE_SIGN_IN;
    }

    @PostMapping(EmployeeURLs.EMPLOYEE_SIGN_IN_RELATIVE_URL)
    public String postSignIn(@Valid EmployeeDTO employeeDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setModelAttributes(model, employeeDTO, null, null, null);
            return TemplateNames.EMPLOYEE_SIGN_IN;
        }
        try {
            if (Objects.nonNull(employeeService.findEmployeeByUsername(employeeDTO.getUsername()))) {
                Employee employee = employeeService.findEmployeeByUsername(employeeDTO.getUsername());
                if (passwordEncoder.matches(employeeDTO.getPassword(), employee.getPassword())) {
                    setModelAttributes(model, null, null, null, "Hi " + employeeDTO.getFirstName());
                    return "redirect:" + EmployeeURLs.EMPLOYEE_PROFILE_FULL_URL + employeeDTO.getUsername();
                } else {
                    throw new Exception(EmployeeErrors.WRONG_PASSWORD);
                }
            } else {
                throw new Exception(String.format(EmployeeErrors.USERNAME_DOES_NOT_EXIST, employeeDTO.getUsername()));
            }
        } catch (Exception e) {
            setModelAttributes(model, employeeDTO, null, null, String.format(EmployeeErrors.ERROR_IN_SIGNING_IN, e.getMessage()));
            return TemplateNames.EMPLOYEE_SIGN_IN;
        }
    }

    @GetMapping(EmployeeURLs.EMPLOYEE_PROFILE_RELATIVE_URL + "{username}")
    public String getProfile(@PathVariable String username, Model model) {
        EmployeeDTO employeeDTO = employeeService.createEmployeeDTO(username);
        setModelAttributes(model, employeeDTO, null, null, "Hi " + employeeDTO.getFirstName());
        return TemplateNames.EMPLOYEE_PROFILE;
    }

    private void setModelAttributes(Model model, EmployeeDTO employeeDTO, List<Role> roles, List<Employee> managers, String message) {
        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("employeeRoles", roles);
        model.addAttribute("managers", managers);
        model.addAttribute("message", message);
    }

}
