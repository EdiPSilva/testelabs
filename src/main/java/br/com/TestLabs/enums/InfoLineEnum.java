package br.com.TestLabs.enums;

import lombok.Getter;

@Getter
public enum InfoLineEnum {

    USER_ID("Id do usuario", 0, 10),
    USER_NAME("Nome do usuario", 11, 55),
    ORDER_ID("Id do pedido", 56, 65),
    PRODUCT_ID("Id do produto", 66, 75),
    PRODUCT_VALUE("Valor do produto", 76, 87),
    ORDER_DATE("Data do pedido", 87, 95);

    private String name;
    private int start;
    private int end;

    InfoLineEnum(final String name,
                 final int start,
                 final int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
