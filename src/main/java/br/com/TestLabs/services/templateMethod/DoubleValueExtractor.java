package br.com.TestLabs.services.templateMethod;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.enums.InfoLineEnum;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DoubleValueExtractor extends ValueExtractor {

    private final MessageConfiguration messageConfiguration;

    @Override
    protected Double validateAndConvert(String value, InfoLineEnum infoLineEnum, Integer index) {
        try {
            double doubleValue = Double.parseDouble(value);
            if (doubleValue <= 0) {
                throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
            }
            return doubleValue;
        } catch (NumberFormatException ex) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.WARN_INVALID_POSITION_VALUE, infoLineEnum.getName(), index));
        }
    }
}
