package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.entities.OrderEntity;
import br.com.TestLabs.entities.ProductEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MessageConfiguration messageConfiguration;

    public void createOrUpdateProduct(final OrderEntity orderEntity, final LineDTO lineDTO) {
        log.info("m=createOrUpdateProduct, lineDTO={}", lineDTO);
        if (Objects.isNull(orderEntity) || Objects.isNull(lineDTO)) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_OBJECT), HttpStatus.BAD_REQUEST);
        }
        final Optional<ProductEntity> productEntityOptional = productRepository.findProductByIdAndOrderId(lineDTO.getProductId(), orderEntity.getId());
        if (productEntityOptional.isEmpty()) {
            final ProductEntity productEntity = ProductEntity.builder()
                    .id(lineDTO.getProductId())
                    .value(lineDTO.getProductValue())
                    .createDate(LocalDateTime.now())
                    .order(orderEntity)
                    .build();
            productRepository.save(productEntity);
        } else {
            final ProductEntity productEntity = productEntityOptional.get();
            productEntity.setValue(lineDTO.getProductValue());
            productEntity.setUpdateDate(LocalDateTime.now());
            productRepository.save(productEntity);
        }
    }

    public Double getTotalAmountByOrder(final Long orderId) {
        log.info("m=getTotalAmountByOrder, orderId={}", orderId);
        return productRepository.totalAmountByOrder(orderId);
    }
}
