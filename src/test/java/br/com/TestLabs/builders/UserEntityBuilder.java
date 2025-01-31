package br.com.TestLabs.builders;

import br.com.TestLabs.entities.UserEntity;

import java.time.LocalDateTime;

public final class UserEntityBuilder {

    private UserEntity userEntity;

    private UserEntityBuilder() {

    }

    public static UserEntityBuilder getInstance() {
        final UserEntityBuilder builder = new UserEntityBuilder();
        builder.userEntity = new UserEntity();
        builder.userEntity.setId(1L);
        builder.userEntity.setName("teste");
        builder.userEntity.setCreateDate(LocalDateTime.now());
        return builder;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
