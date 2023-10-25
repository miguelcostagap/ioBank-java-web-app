package io.codeforall.thestudio.javabank.persistence.dao;

public interface SessionManager<T>{

    void startSession();

    void stopSession();

    T getCurrentSession();
}
