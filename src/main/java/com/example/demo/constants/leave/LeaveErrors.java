package com.example.demo.constants.leave;

public interface LeaveErrors {
    String WRONG_DATES = "End Date Must Be After Start Date!";
    String DATES_OVERLAP = "Leave Dates Overlap With Your Other Leave Requests:";
    String EXCEEDING_LIMIT = "You Have 10 Unchecked Leave Requests. You Can't Add More.";
}
