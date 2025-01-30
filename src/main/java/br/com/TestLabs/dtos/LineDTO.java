package br.com.TestLabs.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Getter
@Slf4j
@Builder
@ToString
public class LineDTO {

    private Long userId;
    private String userName;
    private Long orderId;
    private Long productId;
    private Double productValue;
    private LocalDate orderDate;
}
