package io.codeforall.thestudio.javabank.persistence.dao;

public interface TransactionManager {

    void beginRead();

    void beginWrite();

    void commit();

    void rollback();
}
