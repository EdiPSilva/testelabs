package br.com.TestLabs.builders;

import br.com.TestLabs.entities.UserEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public final class UserEntityBuilder {

    private UserEntity userEntity;

    private UserEntityBuilder() {

    }

    public static UserEntityBuilder getInstance() {
        final UserEntityBuilder builder = new UserEntityBuilder();
        builder.userEntity = UserEntity.builder()
                .id(1L)
                .name("teste")
                .createDate(LocalDateTime.now())
                .build();
        return builder;
    }
}
