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

    public void createOrUpdateOrder(final UserEntity userEntity, final LineDTO lineDTO) {
        log.info("m=createOrUpdateOrder, lineDTO={}", lineDTO);
        if (Objects.isNull(userEntity) || Objects.isNull(lineDTO)) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_OBJECT), HttpStatus.BAD_REQUEST);
        }
        final Optional<OrderEntity> orderEntityOptional = orderRepository.findOrderByIdAndUserId(lineDTO.getOrderId(), userEntity.getId());
        OrderEntity orderEntity = null;
        if (orderEntityOptional.isEmpty()) {
            orderEntity = OrderEntity.builder()
                    .id(lineDTO.getOrderId())
                    .date(lineDTO.getOrderDate())
                    .createDate(LocalDateTime.now())
                    .user(userEntity)
                    .build();
            orderEntity = orderRepository.save(orderEntity);
        } else {
            orderEntity = orderEntityOptional.get();
        }

    }
}
