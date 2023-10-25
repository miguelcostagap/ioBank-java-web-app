package io.codeforall.thestudio.javabank.persistence.dao.jpa;

import io.codeforall.thestudio.javabank.model.account.Account;
import io.codeforall.thestudio.javabank.persistence.dao.AccountDao;
import org.springframework.stereotype.Repository;

@Repository
public class JpaAccountDao extends GenericJpaDao<Account> implements AccountDao {

    public JpaAccountDao() {
        super(Account.class);
    }

}
