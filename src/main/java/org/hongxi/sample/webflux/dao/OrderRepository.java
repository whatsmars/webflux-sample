package org.hongxi.sample.webflux.dao;

import org.hongxi.sample.webflux.model.Order;
import reactor.core.publisher.Mono;

public interface OrderRepository {

    Mono<Order> findById(String id);

    Mono<Order> save(Order order);

    Mono<Void> deleteById(String id);
}
