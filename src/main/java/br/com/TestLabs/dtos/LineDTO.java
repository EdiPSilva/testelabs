package br.com.TestLabs.dtos;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Builder
public class LineDTO {

    private Long userId;
    private String userName;
    private Long orderId;
    private Long productId;
    private Double productValue;
    private LocalDate orderDate;
}
