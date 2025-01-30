package br.com.TestLabs.services.templateMethod;

import br.com.TestLabs.enums.InfoLineEnum;

public abstract class ValueExtractor {

    protected String extractValue(final String line, final InfoLineEnum infoLineEnum) {
        return line.substring(infoLineEnum.getStart(), infoLineEnum.getEnd());
    }

    public <T> T extractValueWithValidation(final String line, final Integer index, final InfoLineEnum infoLineEnum) {
        final String value = extractValue(line, infoLineEnum);
        return validateAndConvert(value, infoLineEnum, index);
    }

    protected abstract <T> T validateAndConvert(String value, InfoLineEnum infoLineEnum, Integer index);
}
