package io.codeforall.thestudio.javabank.persistence.dao;

import io.codeforall.thestudio.javabank.exceptions.TransactionInvalidException;
import io.codeforall.thestudio.javabank.model.Model;

import java.util.List;

public interface Dao<T extends Model> {

    List<T> findAll() throws TransactionInvalidException;

    T findById(Integer id);

    T saveOrUpdate(T modelObject);

    void delete(Integer id);
}
