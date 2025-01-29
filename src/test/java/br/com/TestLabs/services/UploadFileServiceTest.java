package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UploadFileServiceTest {

    private final String validOrdersFile = "valid-orders.txt";
    private final String invalidExtensionFile = "invalid-extension.csv";

    @InjectMocks
    private UploadFileService uploadFileService;

    @Mock
    protected MessageConfiguration messageConfiguration;

    private MultipartFile readFile(final String fileName) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        try (final FileInputStream inputStream = new FileInputStream(file)) {
            return new MockMultipartFile(file.getName(), inputStream);
        }
    }

    @Test
    @DisplayName("Deve retorna um erro quando a extensao do arquivo for invalida")
    public void shouldReturnAnErroWhenTheFileExtensionIsInvalid() throws IOException {
        final MultipartFile multipartFile = readFile(invalidExtensionFile);
        when(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_FILE_EXTENSION)).thenReturn(MessageCodeEnum.ERROR_INVALID_FILE_EXTENSION.getValue());
        final CustomException throwable = assertThrows(CustomException.class, () -> uploadFileService.uploadFile(multipartFile));
        assertNotNull(throwable);
        assertEquals(HttpStatus.BAD_REQUEST, throwable.getHttpStatus());
        assertEquals(throwable.getMessage(), MessageCodeEnum.ERROR_INVALID_FILE_EXTENSION.getValue());
    }
}
