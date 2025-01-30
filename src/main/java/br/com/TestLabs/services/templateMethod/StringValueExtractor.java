package br.com.TestLabs.services.templateMethod;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.enums.InfoLineEnum;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StringValueExtractor extends ValueExtractor {

    private final MessageConfiguration messageConfiguration;

    @Override
    protected String validateAndConvert(final String value, final InfoLineEnum infoLineEnum, final Integer index) {
        if (value.isBlank()) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
        }
        return value;
    }
}
