package io.codeforall.thestudio.javabank.persistence.dao.jpa;

import io.codeforall.thestudio.javabank.model.Customer;
import io.codeforall.thestudio.javabank.persistence.dao.CustomerDao;
import org.springframework.stereotype.Repository;

@Repository
public class JpaCustomerDao extends GenericJpaDao<Customer> implements CustomerDao {

    public JpaCustomerDao() {
        super(Customer.class);
    }

}
