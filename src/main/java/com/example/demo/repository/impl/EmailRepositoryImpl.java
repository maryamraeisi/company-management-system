package com.example.demo.repository.impl;

import com.example.demo.model.Email;
import com.example.demo.repository.api.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmailRepositoryImpl implements EmailRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Email saveEmail(Email email) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.save(email);
            transaction.commit();
            return email;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Email> loadEmployeeSentEmails(Long employeeId) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select e from Email e where e.employee.id = :employeeId and e.enabled = true";
            Query query = session.createQuery(hqlString);
            query.setParameter("employeeId", employeeId);
            List<Email> emails = query.getResultList();
            return emails;
        } finally {
            session.close();
        }
    }
}
