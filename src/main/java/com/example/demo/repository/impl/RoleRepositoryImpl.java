package com.example.demo.repository.impl;

import com.example.demo.model.Role;
import com.example.demo.repository.api.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Role findRoleByName(String name) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select r from Role r where r.name = :name";
            Query query = session.createQuery(hqlString);
            query.setParameter("name", name);
            Role role = (Role) query.getSingleResult();
            return role;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public Role saveRole(Role role) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.save(role);
            transaction.commit();
            return role;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Role> loadAllRoles() {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select r from Role r";
            Query query = session.createQuery(hqlString);
            List<Role> roleList = (List<Role>) query.getResultList();
            return roleList;
        } finally {
            session.close();
        }
    }

    @Override
    public Role findRoleById(long id) {
        Session session = sessionFactory.openSession();
        try {
            String hqlString = "select r from Role r where r.id = :id";
            Query query = session.createQuery(hqlString);
            query.setParameter("id", id);
            Role role = (Role) query.getSingleResult();
            return role;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }
}
