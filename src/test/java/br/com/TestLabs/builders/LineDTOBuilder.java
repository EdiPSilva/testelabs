package br.com.TestLabs.builders;

import br.com.TestLabs.dtos.LineDTO;

import java.time.LocalDate;

public final class LineDTOBuilder {

    private LineDTO lineDTO;

    private LineDTOBuilder() {

    }

    public static LineDTOBuilder getInstance() {
        final LineDTOBuilder builder = new LineDTOBuilder();
        builder.lineDTO = LineDTO.builder()
                .userId(1L)
                .userName("teste")
                .orderId(1L)
                .productId(1L)
                .productValue(10.5)
                .orderDate(LocalDate.now())
                .build();
        return builder;
    }

    public LineDTO getLineDTO() {
        return lineDTO;
    }
}
