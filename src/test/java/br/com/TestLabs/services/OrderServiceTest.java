package br.com.TestLabs.services;

import br.com.TestLabs.builders.LineDTOBuilder;
import br.com.TestLabs.builders.OrderEntityBuilder;
import br.com.TestLabs.builders.UserEntityBuilder;
import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.entities.OrderEntity;
import br.com.TestLabs.entities.UserEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MessageConfiguration messageConfiguration;

    @Mock
    private ProductService productService;

    @Captor
    private ArgumentCaptor<OrderEntity> orderEntityCaptor;

    @Test
    @DisplayName("Nao deve cadastrar o pedido quando o usuario for invalido")
    public void shouldNotCreteTheOrderWhenTheUserIsInvalid() {
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_OBJECT;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> orderService.createOrUpdateOrder(null, lineDTO));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(orderRepository, never()).save(any());
        verify(productService, never()).getTotalAmountByOrder(anyLong());
    }

    @Test
    @DisplayName("Nao deve cadastrar o pedido quando o objeto for invalido")
    public void shouldNotCreteTheOrderWhenTheObjectIsInvalid() {
        final UserEntity userEntity = UserEntityBuilder.getInstance().getUserEntity();
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_OBJECT;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> orderService.createOrUpdateOrder(userEntity, null));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(orderRepository, never()).save(any());
        verify(productService, never()).getTotalAmountByOrder(anyLong());
    }

    @Test
    @DisplayName("Deve cadastrar o pedido quando nao for localizado")
    public void shouldCreateTheOrderWhenNotFound() {
        final UserEntity userEntity = UserEntityBuilder.getInstance().getUserEntity();
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        when(orderRepository.findOrderByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());
        final Double totalAmount = 0.0;
        when(productService.getTotalAmountByOrder(anyLong())).thenReturn(totalAmount);
        assertDoesNotThrow(() -> orderService.createOrUpdateOrder(userEntity, lineDTO));
        verify(orderRepository).save(orderEntityCaptor.capture());
        final OrderEntity orderEntityResult = orderEntityCaptor.getValue();
        assertNotNull(orderEntityResult);
        assertEquals(orderEntityResult.getId(), lineDTO.getOrderId());
        assertEquals(orderEntityResult.getDate(), lineDTO.getOrderDate());
        assertEquals(orderEntityResult.getTotalAmount(), totalAmount + lineDTO.getProductValue());
        assertNotNull(orderEntityResult.getUser());
        assertNull(orderEntityResult.getUpdateDate());
        assertEquals(orderEntityResult.getUser().getId(), lineDTO.getUserId());
    }

    @Test
    @DisplayName("Deve atualizar o pedido quando for localizado")
    public void shouldUpdateTheOrderWhenItIsLocated() {
        final UserEntity userEntity = UserEntityBuilder.getInstance().getUserEntity();
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        final OrderEntity orderEntity = OrderEntityBuilder.getInstance().getOrderEntity();
        when(orderRepository.findOrderByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(orderEntity));
        final Double totalAmount = 11.35;
        when(productService.getTotalAmountByOrder(anyLong())).thenReturn(totalAmount);
        assertDoesNotThrow(() -> orderService.createOrUpdateOrder(userEntity, lineDTO));
        verify(orderRepository).save(orderEntityCaptor.capture());
        final OrderEntity orderEntityResult = orderEntityCaptor.getValue();
        assertNotNull(orderEntityResult);
        assertEquals(orderEntityResult.getId(), lineDTO.getOrderId());
        assertEquals(orderEntityResult.getDate(), lineDTO.getOrderDate());
        assertEquals(orderEntityResult.getTotalAmount(), totalAmount + lineDTO.getProductValue());
        assertNotNull(orderEntityResult.getUpdateDate());
        assertNotNull(orderEntityResult.getUser());
        assertEquals(orderEntityResult.getUser().getId(), lineDTO.getUserId());
    }


}
