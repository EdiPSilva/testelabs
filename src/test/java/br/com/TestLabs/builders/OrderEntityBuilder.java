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
        builder.orderEntity = OrderEntity.builder()
                .id(1L)
                .totalAmount(10.5)
                .date(LocalDate.now())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now().plusMinutes(1))
                .user(UserEntityBuilder.getInstance().getUserEntity())
                .build();
        return builder;
    }

    public OrderEntity getOrderEntity() {
        return this.orderEntity;
    }
}
