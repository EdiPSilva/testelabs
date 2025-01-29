package br.com.TestLabs.enums;

public enum MessageCodeEnum {

    ERROR_INVALID_FILE("error.invalid.file"),
    ERROR_INVALID_FILE_EXTENSION("error.invalid.file.extension");

    private String value;

    MessageCodeEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
