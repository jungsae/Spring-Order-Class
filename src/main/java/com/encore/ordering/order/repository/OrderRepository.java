package com.encore.ordering.order.repository;

import org.hibernate.criterion.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>
{
}
