package br.com.TestLabs.services.templateMethod;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.enums.InfoLineEnum;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
public class LocalDateValueExtractor extends ValueExtractor {

    private final MessageConfiguration messageConfiguration;

    @Override
    protected LocalDate validateAndConvert(final String value,
                                           final InfoLineEnum infoLineEnum,
                                           final Integer index) {
        try {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(value, formatter);
        } catch (DateTimeParseException ex) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
        }
    }
}
