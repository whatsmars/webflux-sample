package org.hongxi.sample.webflux.model;

import lombok.Data;

@Data
public class Order {

    private String id;

    private Long start;

    private Long end;

    public Order() {
    }

    public Order(String id) {
        this.id = id;
    }
}
