package com.example.demo.repository.impl;

import com.example.demo.model.Employee;
import com.example.demo.repository.api.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Employee saveEmployee(Employee employee) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            return employee;
        } finally {
            session.close();
        }
    }

    @Override
    public Employee findEmployeeByUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select e from Employee e where e.username = :username";
            Query query = session.createQuery(hqlString);
            query.setParameter("username", username);
            Employee employee = (Employee) query.getSingleResult();
            return employee;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public Employee findEmployeeByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select e from Employee e where e.email = :email";
            Query query = session.createQuery(hqlString);
            query.setParameter("email", email);
            Employee employee = (Employee) query.getSingleResult();
            return employee;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Employee> findManagers() {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select e from Employee e join e.roles r where r.name = 'ROLE_MANAGER'";
            Query query = session.createQuery(hqlString);
            List<Employee> employeeList = (List<Employee>) query.getResultList();
            return employeeList;
        } finally {
            session.close();
        }
    }

    @Override
    public Employee findEmployeeById(long id) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select e from Employee e where e.id = :id";
            Query query = session.createQuery(hqlString);
            query.setParameter("id", id);
            Employee employee = (Employee) query.getSingleResult();
            return employee;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public String loadEmployeeEmailByUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select e.email from Employee e where e.username = :username";
            Query query = session.createQuery(hqlString);
            query.setParameter("username", username);
            String email = (String) query.getSingleResult();
            return email;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public Long loadEmployeeIdByUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select e.id from Employee e where e.username = :username";
            Query query = session.createQuery(hqlString);
            query.setParameter("username", username);
            Long id = (Long) query.getSingleResult();
            return id;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }
}
