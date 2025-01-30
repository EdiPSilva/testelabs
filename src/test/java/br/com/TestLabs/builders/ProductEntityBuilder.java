package br.com.TestLabs.builders;

import br.com.TestLabs.entities.ProductEntity;

import java.time.LocalDateTime;

public final class ProductEntityBuilder {

    private ProductEntity productEntity;

    private ProductEntityBuilder() {

    }

    public static ProductEntityBuilder getInstance() {
        final ProductEntityBuilder builder = new ProductEntityBuilder();
        builder.productEntity = ProductEntity.builder()
                .id(1L)
                .value(10.5)
                .createDate(LocalDateTime.now())
                .order(OrderEntityBuilder.getInstance().getOrderEntity())
                .build();
        return builder;
    }

    public ProductEntity getProductEntity() {
        return this.productEntity;
    }
}
