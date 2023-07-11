package com.example.demo.constants.leave;

import com.example.demo.constants.ServletPath;

public interface LeaveURLs {
    String EMPLOYEE_LEAVES_FULL_URL = ServletPath.SERVLET_PATH + "employee_leaves/";
    String EMPLOYEE_LEAVES_RELATIVE_URL = "employee_leaves/";
    String EMPLOYEE_LEAVES_FOR_MANAGER_FULL_URL = ServletPath.SERVLET_PATH + "employee_leaves_for_manager/";
    String EMPLOYEE_LEAVES_FOR_MANAGER_RELATIVE_URL = "employee_leaves_for_manager/";
    String DELETE_LEAVE_RELATIVE_URL = "delete_leave/";
    String UPDATE_LEAVE_STATUS_RELATIVE_URL = "update_leave_status/";
}
