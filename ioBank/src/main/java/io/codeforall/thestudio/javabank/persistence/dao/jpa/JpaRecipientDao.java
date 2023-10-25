package io.codeforall.thestudio.javabank.persistence.dao.jpa;

import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.persistence.dao.RecipientDao;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRecipientDao extends GenericJpaDao<Recipient> implements RecipientDao {

    public JpaRecipientDao() {
        super(Recipient.class);
    }
}
