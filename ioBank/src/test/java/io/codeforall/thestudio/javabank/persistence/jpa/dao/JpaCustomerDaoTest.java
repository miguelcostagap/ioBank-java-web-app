package io.codeforall.thestudio.javabank.persistence.jpa.dao;

import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.persistence.dao.jpa.JpaCustomerDao;
import io.codeforall.thestudio.javabank.persistence.dao.jpa.JpaSessionManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaCustomerDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaSessionManager sessionManager;

    @InjectMocks
    private JpaCustomerDao customerDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionManager.getCurrentSession()).thenReturn(entityManager);
    }

    @Test
    void testFindById() {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);

        when(entityManager.find(Customer.class, 1)).thenReturn(customer);

        // Call the findById() method
        Customer result = customerDao.findById(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());

        // Verify that the entityManager.find() method was called
        verify(entityManager, times(1)).find(Customer.class, 1);
    }

    @Test
    void testFindAll() {
        // Create a list of sample customers
        List<Customer> customers = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setId(1);
        Customer customer2 = new Customer();
        customer2.setId(2);
        customers.add(customer1);
        customers.add(customer2);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery cQuery = mock(CriteriaQuery.class);
        Root root = mock(Root.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Customer.class)).thenReturn(cQuery);
        when(cQuery.from(Customer.class)).thenReturn(root);
        when(cQuery.select(root)).thenReturn(cQuery);
        when(cQuery.orderBy(cb.asc(root.get("id")))).thenReturn(cQuery);

        TypedQuery<Customer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(cQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(customers);

        // Call the findAll() method
        List<Customer> result = customerDao.findAll();

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
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);

        // Call the saveOrUpdate() method
        customerDao.saveOrUpdate(customer);

        // Verify that the entityManager.merge() method was called
        verify(entityManager, times(1)).merge(customer);
    }

    @Test
    void testDelete() {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setId(1);

        when(entityManager.find(Customer.class, 1)).thenReturn(customer);

        // Call the delete() method
        customerDao.delete(1);

        // Verify that the entityManager.remove() method was called
        verify(entityManager, times(1)).remove(customer);
    }
}
