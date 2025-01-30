package br.com.TestLabs.enums;

public enum MessageCodeEnum {

    ERROR_INVALID_FILE("error.invalid.file"),
    ERROR_INVALID_FILE_EXTENSION("error.invalid.file.extension"),
    ERROR_INVALID_DATA_CREATE_LOG("error.invalid.data.create.log"),
    WARN_INVALID_POSITION_VALUE("warn.invalid.position.value");

    private String value;

    MessageCodeEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
