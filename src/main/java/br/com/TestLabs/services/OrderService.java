package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.entities.OrderEntity;
import br.com.TestLabs.entities.UserEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.OrderRepository;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final MessageConfiguration messageConfiguration;
    private final ProductService productService;

    public void createOrUpdateOrder(final UserEntity userEntity, final LineDTO lineDTO) {
        log.info("m=createOrUpdateOrder, lineDTO={}", lineDTO);
        if (Objects.isNull(userEntity) || Objects.isNull(lineDTO)) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_OBJECT), HttpStatus.BAD_REQUEST);
        }
        final Double totalAmount = productService.getTotalAmountByOrder(lineDTO.getOrderId());
        final Optional<OrderEntity> orderEntityOptional = orderRepository.findOrderByIdAndUserId(lineDTO.getOrderId(), userEntity.getId());
        OrderEntity orderEntity;
        if (orderEntityOptional.isEmpty()) {
            orderEntity = new OrderEntity();
            orderEntity.setId(lineDTO.getOrderId());
            orderEntity.setDate(lineDTO.getOrderDate());
            orderEntity.setCreateDate(LocalDateTime.now());
            orderEntity.setUser(userEntity);
        } else {
            orderEntity = orderEntityOptional.get();
            orderEntity.setUpdateDate(LocalDateTime.now());
        }
        orderEntity.setTotalAmount(totalAmount + lineDTO.getProductValue());
        orderEntity = orderRepository.save(orderEntity);
        productService.createOrUpdateProduct(orderEntity, lineDTO);
    }
}
