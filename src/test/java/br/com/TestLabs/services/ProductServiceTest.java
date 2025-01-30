package br.com.TestLabs.services;

import br.com.TestLabs.builders.LineDTOBuilder;
import br.com.TestLabs.builders.OrderEntityBuilder;
import br.com.TestLabs.builders.ProductEntityBuilder;
import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.entities.OrderEntity;
import br.com.TestLabs.entities.ProductEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.ProductRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MessageConfiguration messageConfiguration;

    @Captor
    private ArgumentCaptor<ProductEntity> productEntityCaptor;

    @Test
    @DisplayName("Nao deve cadastrar o produto quando o pedido for invalido")
    public void shouldNotCreteTheProductWhenTheOrderIsInvalid() {
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_OBJECT;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> productService.createOrUpdateProduct(null, lineDTO));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(productRepository, never()).findProductByIdAndOrderId(anyLong(), anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Nao deve cadastrar o pedido quando o objeto for invalido")
    public void shouldNotCreteTheOrderWhenTheObjectIsInvalid() {
        final OrderEntity orderEntity = OrderEntityBuilder.getInstance().getOrderEntity();
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_OBJECT;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> productService.createOrUpdateProduct(orderEntity, null));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(productRepository, never()).findProductByIdAndOrderId(anyLong(), anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cadastrar o produto quando nao for localizado")
    public void shouldCreateTheProductWhenNotFound() {
        final OrderEntity orderEntity = OrderEntityBuilder.getInstance().getOrderEntity();
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        when(productRepository.findProductByIdAndOrderId(anyLong(), anyLong())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> productService.createOrUpdateProduct(orderEntity, lineDTO));
        verify(productRepository).save(productEntityCaptor.capture());
        final ProductEntity productEntity = productEntityCaptor.getValue();
        assertNotNull(productEntity);
        assertEquals(productEntity.getId(), lineDTO.getProductId());
        assertEquals(productEntity.getValue(), lineDTO.getProductValue());
        assertNotNull(productEntity.getOrder());
        assertEquals(productEntity.getOrder().getId(), lineDTO.getOrderId());
    }

    @Test
    @DisplayName("Deve atualizar o produto quando for localizado")
    public void shouldCreateTheProductWhenItIsLocated() {
        final OrderEntity orderEntity = OrderEntityBuilder.getInstance().getOrderEntity();
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        final ProductEntity productEntity = ProductEntityBuilder.getInstance().getProductEntity();
        productEntity.setValue(11.5);
        when(productRepository.findProductByIdAndOrderId(lineDTO.getProductId(), orderEntity.getId())).thenReturn(Optional.of(productEntity));
        assertDoesNotThrow(() -> productService.createOrUpdateProduct(orderEntity, lineDTO));
        verify(productRepository).save(productEntityCaptor.capture());
        final ProductEntity productEntityResult = productEntityCaptor.getValue();
        assertNotNull(productEntityResult);
        assertEquals(productEntityResult.getId(), lineDTO.getProductId());
        assertEquals(productEntityResult.getValue(), lineDTO.getProductValue());
        assertNotNull(productEntityResult.getOrder());
        assertEquals(productEntityResult.getOrder().getId(), lineDTO.getOrderId());
    }
}
