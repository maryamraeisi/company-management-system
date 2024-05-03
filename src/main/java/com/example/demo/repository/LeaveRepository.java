package com.example.demo.repository;

import com.example.demo.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Leave save(Leave leave);

    @Query("select l from Leave l where l.employee.username = ?1 and l.enabled = true order by l.id desc")
    List<Leave> loadEmployeeLeavesByEmployeeUsername(String username);

    @Modifying
    @Query("update Leave l set l.enabled = false where l.id = ?1")
    void deleteById(long leaveId);

    @Query("select l from Leave l where l.employee.username = ?1 and l.enabled = true and (l.isAccepted = true or l.isAccepted is null)")
    List<Leave> loadNonRejectedEmployeeLeavesByEmployeeUsername(String username);

    @Query("select l from Leave l where l.employee.manager.username = :managerUsername and l.enabled = true")
    List<Leave> loadEmployeeLeaveRequestsForManager(String managerUsername);

    @Transactional
    @Modifying
    @Query("update Leave l set l.isAccepted = ?2 where l.id = ?1")
    void updateLeaveStatus(long leaveId, boolean isAccepted);

}
