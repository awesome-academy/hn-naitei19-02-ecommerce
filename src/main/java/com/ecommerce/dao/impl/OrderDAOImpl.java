package com.ecommerce.dao.impl;

import com.ecommerce.dao.OrderDAO;
import com.ecommerce.model.BaseEntity;
import com.ecommerce.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Project: hn-naitei19-02-ecommerce
 * @Author: sonle
 * @Date: 15/09/2023
 * @Time: 23:11
 */
@Repository
public class OrderDAOImpl extends BaseDAO<Long, Order> implements OrderDAO{
    public OrderDAOImpl() {
        super(Order.class);
    }

    @Override
    public List<Order> get() {

        return super.get();
    }

    @Override
    public Order get(Long id) {
        return super.get(id);
    }

    @Override
    public void persist(Order e) {
        super.persist(e);
    }

    @Override
    public void delete(Order e) {
        super.delete(e);
    }
}
