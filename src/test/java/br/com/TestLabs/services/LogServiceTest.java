package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.entities.LogEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.LogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @InjectMocks
    private LogService logService;

    @Mock
    private LogRepository logRepository;

    @Mock
    private MessageConfiguration messageConfiguration;

    @Captor
    private ArgumentCaptor<LogEntity> logEntityArgumentCaptor;

    @Test
    @DisplayName("Não deve gravar o log quando o nome do arquivo for invalido")
    public void shouldNotCreateTheLogWhenFileNameIsInvalid() {
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_DATA_CREATE_LOG;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> logService.createLog(null, 1, "test"));
        assertNotNull(throwable);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(logRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve gravar o log quando a linha do arquivo for invalido")
    public void shouldNotCreateTheLogWhenFileLineIsInvalid() {
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_DATA_CREATE_LOG;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> logService.createLog("test", 0, "test"));
        assertNotNull(throwable);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(logRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve gravar o log quando a mensagem de log for invalido")
    public void shouldNotCreateTheLogWhenLogMessageIsInvalid() {
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_DATA_CREATE_LOG;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> logService.createLog("test", 1, null));
        assertNotNull(throwable);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(logRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve gravar o log quando as informacoes estiverem corretas")
    public void shouldCreateTheLogWhenTheInformationsIsCorrect() {
        final String fileName = "test.txt";
        final int lineNumber = 1;
        final String logMessagem = "message";
        assertDoesNotThrow(() -> logService.createLog(fileName, lineNumber, logMessagem));
        verify(logRepository).save(logEntityArgumentCaptor.capture());
        final LogEntity logEntity = logEntityArgumentCaptor.getValue();
        assertNotNull(logEntity);
        assertEquals(logEntity.getFileName(), fileName);
        assertEquals(logEntity.getLineNumber(), lineNumber);
        assertEquals(logEntity.getLogMessage(), logMessagem);
    }

}
