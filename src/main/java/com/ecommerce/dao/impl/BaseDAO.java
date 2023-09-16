package com.ecommerce.dao.impl;

import com.ecommerce.dao.DAO;
import com.ecommerce.model.BaseEntity;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: hn-naitei19-02-ecommerce
 * @Author: sonle
 * @Date: 15/09/2023
 * @Time: 15:34
 */
@RequiredArgsConstructor
public abstract class BaseDAO<PK extends Serializable, E extends BaseEntity> implements DAO<PK, E> {
    @PersistenceContext
    protected Session session;

    protected final Class<? extends E> persistentClass;

    @Override
    public List<E> get() {
        return new ArrayList<>();
    }

    @Override
    public E get(PK id) {
        return session.get(persistentClass, id);
    }

    @Override
    public void persist(E e) {
        session.persist(e);
    }

    @Override
    public void delete(E e) {
        session.remove(e);
    }
}
