package io.codeforall.thestudio.javabank.persistence.dao.jpa;

import io.codeforall.thestudio.javabank.persistence.dao.SessionManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Component;

@Component
public class JpaSessionManager implements SessionManager<EntityManager> {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void startSession() {

        if (em == null) {
            em = emf.createEntityManager();
        }

    }

    @Override
    public void stopSession() {

        if (em != null) {
            em.close();
        }

        em = null;
    }

    @Override
    public EntityManager getCurrentSession() {

        startSession();
        return em;
    }

    @PersistenceUnit
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
