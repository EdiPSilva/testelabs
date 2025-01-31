package br.com.TestLabs.services;

import br.com.TestLabs.builders.LineDTOBuilder;
import br.com.TestLabs.builders.UserEntityBuilder;
import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.entities.UserEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageConfiguration messageConfiguration;

    @Mock
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;

    @Test
    @DisplayName("Nao deve cadastrar o usuario quando o objeto estiver invalido")
    public void shouldNotCreteTheUserWhenTheObjectIsInvalid() {
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_OBJECT;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> userService.createUser(null));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(userRepository, never()).save(any());
        verify(orderService, never()).createOrUpdateOrder(any(), any());
    }

    @Test
    @DisplayName("Nao deve cadastrar o usuario quando ja existir")
    public void shouldNotCreateTheUserWhenAlreadyExists() {
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        final UserEntity userEntity = UserEntityBuilder.getInstance().getUserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        assertDoesNotThrow(() -> userService.createUser(lineDTO));
        verify(userRepository, never()).save(any());
        verify(orderService, times(1)).createOrUpdateOrder(any(), any());
    }

    @Test
    @DisplayName("Deve cadastrar o usuario quando nao for localizado")
    public void shouldCreateTheUserWhenNotFound() {
        final LineDTO lineDTO = LineDTOBuilder.getInstance().getLineDTO();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        final UserEntity userEntity = UserEntityBuilder.getInstance().getUserEntity();
        when(userRepository.save(any())).thenReturn(userEntity);
        assertDoesNotThrow(() -> userService.createUser(lineDTO));
        verify(userRepository).save(userEntityCaptor.capture());
        final UserEntity userEntityResult = userEntityCaptor.getValue();
        assertNotNull(userEntityResult);
        assertEquals(userEntityResult.getId(), lineDTO.getUserId());
        assertEquals(userEntityResult.getName(), lineDTO.getUserName());
        assertNotNull(userEntityResult.getCreateDate());
        verify(orderService, times(1)).createOrUpdateOrder(any(), any());
    }
}
