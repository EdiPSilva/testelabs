package br.com.TestLabs.builders;

import br.com.TestLabs.entities.ProductEntity;

import java.time.LocalDateTime;

public final class ProductEntityBuilder {

    private ProductEntity productEntity;

    private ProductEntityBuilder() {

    }

    public static ProductEntityBuilder getInstance() {
        final ProductEntityBuilder builder = new ProductEntityBuilder();
        builder.productEntity = new ProductEntity();
        builder.productEntity.setId(1L);
        builder.productEntity.setValue(10.5);
        builder.productEntity.setCreateDate(LocalDateTime.now());
        builder.productEntity.setOrder(OrderEntityBuilder.getInstance().getOrderEntity());
        return builder;
    }

    public ProductEntity getProductEntity() {
        return this.productEntity;
    }
}
