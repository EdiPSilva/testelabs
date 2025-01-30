package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadFileServiceTest {

    private final String validOrdersFile = "valid-orders.txt";

    @InjectMocks
    private ReadFileService readFileService;

    @Mock
    private MessageConfiguration messageConfiguration;

    @Mock
    private LogService logService;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<String> fileNameCaptor;

    @Captor
    private ArgumentCaptor<Integer> lineNumberCaptor;

    @Captor
    private ArgumentCaptor<String> logMessageCaptor;

    private MultipartFile readFile(final String fileName) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        try (final FileInputStream inputStream = new FileInputStream(file)) {
            return new MockMultipartFile(file.getName(), inputStream);
        }
    }

    @Test
    @DisplayName("Deve retorna um erro quando o arquivo for invalido")
    public void shouldReturnAnErroWhenTheFileIsInvalid() {
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_FILE;
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> readFileService.uploadFile(null));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Deve retorna um erro quando a extensao do arquivo for invalida")
    public void shouldReturnAnErroWhenTheFileExtensionIsInvalid() throws IOException {
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.ERROR_INVALID_FILE_EXTENSION;
        final String invalidExtensionFile = "invalid-extension.csv";
        final MultipartFile multipartFile = readFile(invalidExtensionFile);
        when(messageConfiguration.getMessageByCode(messageCodeEnum)).thenReturn(messageCodeEnum.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> readFileService.uploadFile(multipartFile));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), messageCodeEnum.getValue());
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Deve criar um log quando o id do usuario for invalido")
    public void shouldCreatALogWhenUserIdIsInvalid() throws IOException {
        testAboutFileValuesValidations("invalid-userId.txt");
    }

    @Test
    @DisplayName("Deve criar um log quando o nome do usuario for invalido")
    public void shouldCreatALogWhenUserNameIsInvalid() throws IOException {
        testAboutFileValuesValidations("invalid-userName.txt");
    }

    @Test
    @DisplayName("Deve criar um log quando o id do pedido for invalido")
    public void shouldCreatALogWhenOrderIdIsInvalid() throws IOException {
        testAboutFileValuesValidations("invalid-orderId.txt");
    }

    @Test
    @DisplayName("Deve criar um log quando o id do produto for invalido")
    public void shouldCreatALogWhenProductIdIsInvalid() throws IOException {
        testAboutFileValuesValidations("invalid-productId.txt");
    }

    @Test
    @DisplayName("Deve criar um log quando o valor do produto for invalido")
    public void shouldCreatALogWhenProductValueIsInvalid() throws IOException {
        testAboutFileValuesValidations("invalid-productValue.txt");
    }

    @Test
    @DisplayName("Deve criar um log quando a data do pedido for invalida")
    public void shouldCreatALogWhenOrderDateIsInvalid() throws IOException {
        testAboutFileValuesValidations("invalid-orderDate.txt");
    }

    private void testAboutFileValuesValidations(final String invalidFile) throws IOException {
        final MessageCodeEnum messageCodeEnum = MessageCodeEnum.WARN_INVALID_POSITION_VALUE;
        final MultipartFile multipartFile = readFile(invalidFile);
        when(messageConfiguration.getMessageByCode(any(), any(), any())).thenReturn(messageCodeEnum.getValue());
        assertDoesNotThrow(() -> readFileService.uploadFile(multipartFile));
        verify(logService).createLog(fileNameCaptor.capture(), lineNumberCaptor.capture(), logMessageCaptor.capture());
        final String fileName = fileNameCaptor.getValue();
        final Integer lineNumber = lineNumberCaptor.getValue();
        final String logMessage = logMessageCaptor.getValue();
        assertNotNull(fileName);
        assertEquals(fileName, invalidFile);
        assertTrue(lineNumber > 0);
        assertEquals(logMessage, messageCodeEnum.getValue());
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Nao deve retornar nenhum erro e executar a rotina de cadastro")
    public void shouldNotReturnNoErrorAndRunTheCreateRoutine() throws IOException {
        final MultipartFile multipartFile = readFile("valid-orders.txt");
        assertDoesNotThrow(() -> readFileService.uploadFile(multipartFile));
        verify(messageConfiguration, never()).getMessageByCode(any());
        verify(userService, times(5)).createUser(any());
    }
}
