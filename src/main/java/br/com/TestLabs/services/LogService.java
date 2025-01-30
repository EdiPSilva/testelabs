package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.entities.LogEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.LogRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final MessageConfiguration messageConfiguration;

    public void createLog(final String fileName, final int lineNumber, final String logMessage) {
        log.info("m=createLog, fileName={}, lineNumber={}, logMessage={}", fileName, lineNumber, logMessage);
        if (Objects.isNull(fileName) || fileName.isBlank() || lineNumber <= 0 || Objects.isNull(logMessage) || logMessage.isBlank()) {
            log.error("m=createLog, message= Dados invalidos para a gravacao do log");
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_DATA_CREATE_LOG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final LogEntity logEntity = LogEntity.builder()
                .fileName(fileName)
                .lineNumber(lineNumber)
                .logMessage(logMessage)
                .createDate(LocalDateTime.now())
                .build();
        logRepository.save(logEntity);
    }
}
