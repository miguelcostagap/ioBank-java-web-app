package io.codeforall.thestudio.javabank.services;

import io.codeforall.thestudio.javabank.model.Recipient;
import io.codeforall.thestudio.javabank.persistence.dao.RecipientDao;
import io.codeforall.thestudio.javabank.persistence.dao.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class RecipientServiceImp implements RecipientService {

    private RecipientDao recipientDao;
    private TransactionManager tx;

    @Override
    public Recipient get(Integer id) {

        try {
            tx.beginRead();

            return recipientDao.findById(id);

        } finally {

            tx.commit();

        }

    }

    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }

    @Autowired
    public void setTx(TransactionManager tx) {
        this.tx = tx;
    }
}
