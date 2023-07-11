package com.example.demo.repository.impl;

import com.example.demo.model.Leave;
import com.example.demo.model.Role;
import com.example.demo.repository.api.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LeaveRepositoryImpl implements LeaveRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Leave saveLeave(Leave leave) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.save(leave);
            transaction.commit();
            return leave;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Leave> loadEmployeeLeavesByEmployeeUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select l from Leave l where l.employee.username = :username and l.enabled = true order by id desc";
            Query query = session.createQuery(hqlString);
            query.setParameter("username", username);
            List<Leave> leaveList = (List<Leave>) query.getResultList();
            return leaveList;
        } finally {
            session.close();
        }
    }

    @Override
    public Leave deleteLeaveById(long leaveId) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            Leave leave = session.load(Leave.class, leaveId);
            leave.setEnabled(false);
            session.update(leave);
            transaction.commit();
            return leave;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Leave> loadNonRejectedEmployeeLeavesByEmployeeUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select l from Leave l where l.employee.username = :username and l.enabled = true and (l.isAccepted = true or l.isAccepted = null)";
            Query query = session.createQuery(hqlString);
            query.setParameter("username", username);
            List<Leave> leaveList = (List<Leave>) query.getResultList();
            return leaveList;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Leave> loadEmployeeLeaveRequestsForManager(String managerUsername) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select l from Leave l where l.employee.manager.username = :managerUsername and l.enabled = true";
            Query query = session.createQuery(hqlString);
            query.setParameter("managerUsername", managerUsername);
            List<Leave> leaveList = (List<Leave>) query.getResultList();
            return leaveList;
        } finally {
            session.close();
        }
    }

    @Override
    public void updateLeaveStatus(long leaveId, boolean isAccepted) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            Leave leave = session.load(Leave.class, leaveId);
            leave.setIsAccepted(isAccepted);
            session.update(leave);
            transaction.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public Leave loadLeaveById(long leaveId) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select l from Leave l where l.id = :leaveId and l.enabled = true";
            Query query = session.createQuery(hqlString);
            query.setParameter("leaveId", leaveId);
            Leave leave = (Leave) query.getSingleResult();
            return leave;
        } finally {
            session.close();
        }
    }
}
