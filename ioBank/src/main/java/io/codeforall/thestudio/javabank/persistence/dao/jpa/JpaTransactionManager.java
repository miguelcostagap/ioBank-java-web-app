package io.codeforall.thestudio.javabank.persistence.dao.jpa;

import io.codeforall.thestudio.javabank.persistence.dao.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JpaTransactionManager implements TransactionManager {

    private JpaSessionManager sm;

    @Override
    public void beginRead() {

        sm.startSession();
    }

    @Override
    public void beginWrite() {

        sm.getCurrentSession().getTransaction().begin();
    }

    @Override
    public void commit() {

        if (sm.getCurrentSession().getTransaction().isActive()) {
            sm.getCurrentSession().getTransaction().commit();
        }

        sm.stopSession();
    }

    @Override
    public void rollback() {

        if (sm.getCurrentSession().getTransaction().isActive()) {
            sm.getCurrentSession().getTransaction().rollback();
        }

        sm.stopSession();
    }

    @Autowired
    public void setSm(JpaSessionManager sm) {
        this.sm = sm;
    }
}
