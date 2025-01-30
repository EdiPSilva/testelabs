package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.enums.InfoLineEnum;
import br.com.TestLabs.exceptions.CustomException;
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
public class UploadFileService {

    private final String validExtension = "txt";
    private final MessageConfiguration messageConfiguration;
    private final LogService logService;

    public void uploadFile(final MultipartFile multipartFile) throws IOException {
        log.info("m=readFile");
        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_FILE), HttpStatus.BAD_REQUEST);
        }
        validateExtension(multipartFile.getName());
        log.info("m=readFile, fileName={}", multipartFile.getName());
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String line = null;
            Integer index = 0;
            while((line = reader.readLine()) != null) {
                index++;
                try {
                    final LineDTO lineDTO = LineDTO.builder()
                            .userId(extractLongValue(line, index, InfoLineEnum.USER_ID))
                            .userName(extractStringValue(line, index, InfoLineEnum.USER_NAME))
                            .orderId(extractLongValue(line, index, InfoLineEnum.ORDER_ID))
                            .productId(extractLongValue(line, index, InfoLineEnum.PRODUCT_ID))
                            .productValue(extractDoubleValue(line, index, InfoLineEnum.PRODUCT_VALUE))
                            .build();
                } catch (CustomException customException) {
                    log.warn(customException.getMessage());
                    logService.createLog(multipartFile.getName(), index, customException.getMessage());
                }
            }
        }
    }

    private void validateExtension(final String fileName) {
        assert fileName != null;
        final String extension = fileName.substring(fileName.indexOf(".") + 1);
        if (!extension.contains(validExtension)) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_FILE_EXTENSION), HttpStatus.BAD_REQUEST);
        }
    }

    private String extractValue(final String line, final InfoLineEnum infoLineEnum) {
        return line.substring(infoLineEnum.getStart(), infoLineEnum.getEnd());
    }

    private Long extractLongValue(final String line,
                                  final Integer index,
                                  final InfoLineEnum infoLineEnum) {
        try {
            final String value = extractValue(line, infoLineEnum);
            final Long longValue = Long.valueOf(value);
            if (longValue >= 0) {
                throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
            }
            return longValue;
        } catch (NumberFormatException numberFormatException) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
        }
    }

    private String extractStringValue(final String line,
                                       final Integer index,
                                       final InfoLineEnum infoLineEnum) {
        final String value = extractValue(line, infoLineEnum);
        if (value.isBlank()) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
        }
        return value;
    }

    private Double extractDoubleValue(final String line,
                                      final Integer index,
                                      final InfoLineEnum infoLineEnum) {
        try {
            final String value = extractValue(line, infoLineEnum);
            final Double doubleValue = Double.valueOf(value);
            if (doubleValue >= 0) {
                throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
            }
            return doubleValue;
        } catch (NumberFormatException numberFormatException) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
        }
    }
}
