package br.com.TestLabs.builders;

import br.com.TestLabs.entities.OrderEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class OrderEntityBuilder {

    private OrderEntity orderEntity;

    private OrderEntityBuilder() {

    }

    public static OrderEntityBuilder getInstance() {
        final OrderEntityBuilder builder = new OrderEntityBuilder();
        builder.orderEntity = new OrderEntity();
        builder.orderEntity.setId(1L);
        builder.orderEntity.setTotalAmount(10.5);
        builder.orderEntity.setDate(LocalDate.now());
        builder.orderEntity.setCreateDate(LocalDateTime.now());
        builder.orderEntity.setUpdateDate(LocalDateTime.now().plusMinutes(1));
        builder.orderEntity.setUser(UserEntityBuilder.getInstance().getUserEntity());
        return builder;
    }

    public OrderEntity getOrderEntity() {
        return this.orderEntity;
    }
}
