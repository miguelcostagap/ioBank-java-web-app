package io.codeforall.thestudio.javabank.persistence.jpa.dao;

import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.model.account.CheckingAccount;
import io.codeforall.thestudio.javabank.persistence.dao.jpa.JpaAccountDao;
import io.codeforall.thestudio.javabank.persistence.dao.jpa.JpaSessionManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaAccountDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaSessionManager sessionManager;

    @InjectMocks
    private JpaAccountDao accountDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionManager.getCurrentSession()).thenReturn(entityManager);
    }

    @Test
    void testFindById() {
        // Create a sample account
        Account account = new CheckingAccount();
        account.setId(1);

        when(entityManager.find(Account.class, 1)).thenReturn(account);

        // Call the findById() method
        Account result = accountDao.findById(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());

        // Verify that the entityManager.find() method was called
        verify(entityManager, times(1)).find(Account.class, 1);
    }

    @Test
    void testFindAll() {
        // Create a list of sample accounts
        List<Account> accounts = new ArrayList<>();
        Account account1 = new CheckingAccount();
        account1.setId(1);
        Account account2 = new CheckingAccount();
        account2.setId(2);
        accounts.add(account1);
        accounts.add(account2);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery cQuery = mock(CriteriaQuery.class);
        Root root = mock(Root.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Account.class)).thenReturn(cQuery);
        when(cQuery.from(Account.class)).thenReturn(root);
        when(cQuery.select(root)).thenReturn(cQuery);
        when(cQuery.orderBy(cb.asc(root.get("id")))).thenReturn(cQuery);

        TypedQuery<Account> query = mock(TypedQuery.class);
        when(entityManager.createQuery(cQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(accounts);

        // Call the findAll() method
        List<Account> result = accountDao.findAll();

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());

        // Verify that the entityManager.createQuery() and query.getResultList() methods were called
        verify(entityManager, times(1)).createQuery(cQuery);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testSaveOrUpdate() {
        // Create a sample account
        Account account = new CheckingAccount();
        account.setId(1);

        // Call the saveOrUpdate() method
        accountDao.saveOrUpdate(account);

        // Verify that the entityManager.merge() method was called
        verify(entityManager, times(1)).merge(account);
    }

    @Test
    void testDelete() {
        // Create a sample account
        Account account = new CheckingAccount();
        account.setId(1);

        when(entityManager.find(Account.class, 1)).thenReturn(account);

        // Call the delete() method
        accountDao.delete(1);

        // Verify that the entityManager.remove() method was called
        verify(entityManager, times(1)).remove(account);
    }
}
