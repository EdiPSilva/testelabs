package br.com.TestLabs.configurations;

import br.com.TestLabs.enums.MessageCodeEnum;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;

@AllArgsConstructor
@Configuration
@PropertySource("messages.properties")
public class MessageConfiguration {

    private final Environment environment;

    public String getMessageByCode(final MessageCodeEnum messageCodeEnum) {
        final String property = environment.getProperty(messageCodeEnum.getValue());
        if (property != null) {
            return property.replace(" %s", "");
        }
        return null;
    }

    public String getMessageByCode(final MessageCodeEnum messageCodeEnum, final Object... value) {
        return String.format(Objects.requireNonNull(environment.getProperty(messageCodeEnum.getValue())), value);
    }
}
