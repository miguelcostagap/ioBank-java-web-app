package io.codeforall.thestudio.javabank.persistence.dao.jpa;

import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Model;
import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.persistence.dao.Dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;


public abstract class GenericJpaDao<T extends Model> implements Dao<T> {

    protected JpaSessionManager sm;
    protected Class<T> modelType;

    public GenericJpaDao(Class<T> modelType) {
        this.modelType = modelType;
    }

    @Override
    public List<T> findAll() {

        try {
            EntityManager em = sm.getCurrentSession();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(modelType);
            Root<T> root = query.from(modelType);
            query.select(root).orderBy(cb.asc(root.get("id")));

            return em.createQuery(query).getResultList();

            //Using JPA
            //      String simpleName = modelType.getSimpleName();
            //   return em.createQuery("FROM " + simpleName + " " + simpleName + " ORDER BY " + simpleName.toLowerCase() + ".id", modelType).getResultList();

        } catch (PersistenceException ex) {
            throw new TransactionInvalidException();
        }
    }

    @Override
    public T findById(Integer id) {

        try {
            EntityManager em = sm.getCurrentSession();

            return em.find(modelType, id);

        } catch (PersistenceException ex) {
            throw new TransactionInvalidException();
        }
    }

    @Override
    public T saveOrUpdate(T modelObject) {

        try {

            EntityManager em = sm.getCurrentSession();

            return em.merge(modelObject);

        } catch (PersistenceException ex) {
            throw new TransactionInvalidException();
        }

    }

    @Override
    public void delete(Integer id) {

        try {

            EntityManager em = sm.getCurrentSession();

            em.remove(em.find(modelType, id));

        } catch (PersistenceException ex) {
            throw new TransactionInvalidException();
        }
    }

    @Autowired
    public void setSm(JpaSessionManager sm) {
        this.sm = sm;
    }

}
