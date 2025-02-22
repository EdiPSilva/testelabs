package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.enums.InfoLineEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.resources.response.ReadFileResponse;
import br.com.TestLabs.services.templateMethod.DoubleValueExtractor;
import br.com.TestLabs.services.templateMethod.LocalDateValueExtractor;
import br.com.TestLabs.services.templateMethod.LongValueExtractor;
import br.com.TestLabs.services.templateMethod.StringValueExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ReadFileService {

    private final MessageConfiguration messageConfiguration;
    private final LogService logService;
    private final UserService userService;

    public ReadFileResponse readFile(final MultipartFile multipartFile) throws IOException {
        log.info("m=readFile");
        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_FILE), HttpStatus.BAD_REQUEST);
        }
        validateExtension(Objects.requireNonNull(multipartFile.getContentType()));
        log.info("m=readFile, fileName={}", multipartFile.getOriginalFilename());
        Integer index = 0;
        int error = 0;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String line = null;
            while((line = reader.readLine()) != null) {
                index++;
                try {
                    final LineDTO lineDTO = LineDTO.builder()
                            .userId(new LongValueExtractor(messageConfiguration).extractValueWithValidation(line, index, InfoLineEnum.USER_ID))
                            .userName(new StringValueExtractor(messageConfiguration).extractValueWithValidation(line, index, InfoLineEnum.USER_NAME))
                            .orderId(new LongValueExtractor(messageConfiguration).extractValueWithValidation(line, index, InfoLineEnum.ORDER_ID))
                            .productId(new LongValueExtractor(messageConfiguration).extractValueWithValidation(line, index, InfoLineEnum.PRODUCT_ID))
                            .productValue(new DoubleValueExtractor(messageConfiguration).extractValueWithValidation(line, index, InfoLineEnum.PRODUCT_VALUE))
                            .orderDate(new LocalDateValueExtractor(messageConfiguration).extractValueWithValidation(line, index, InfoLineEnum.ORDER_DATE))
                            .build();
                    userService.createUser(lineDTO);
                } catch (CustomException customException) {
                    error++;
                    log.warn(customException.getMessage());
                    logService.createLog(multipartFile.getOriginalFilename(), index, customException.getMessage());
                }
            }
        }

        return new ReadFileResponse(multipartFile.getOriginalFilename(), index, error, validateMessage(index, error));
    }

    private void validateExtension(final String contentType) {
        assert contentType != null;
        final String validExtension = "text/plain";
        if (!contentType.contains(validExtension)) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_FILE_EXTENSION), HttpStatus.BAD_REQUEST);
        }
    }

    private String validateMessage(final int index, final int error) {
        if ((index - error) == 0) {
            return messageConfiguration.getMessageByCode(MessageCodeEnum.MESSAGE_IMPORT_ERROR);
        } else if (error < index) {
            return messageConfiguration.getMessageByCode(MessageCodeEnum.MESSAGE_IMPORT_PARTIAL);
        }
        return messageConfiguration.getMessageByCode(MessageCodeEnum.MESSAGE_IMPORT_SUCCESS);
    }
}
