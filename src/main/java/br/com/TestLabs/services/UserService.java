package br.com.TestLabs.services;

import br.com.TestLabs.configurations.MessageConfiguration;
import br.com.TestLabs.dtos.LineDTO;
import br.com.TestLabs.entities.UserEntity;
import br.com.TestLabs.enums.MessageCodeEnum;
import br.com.TestLabs.exceptions.CustomException;
import br.com.TestLabs.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MessageConfiguration messageConfiguration;

    public void createUser(final LineDTO lineDTO) {
        log.info("m=createUser, lineDTO={}", lineDTO);
        if (Objects.isNull(lineDTO)) {
            throw new CustomException(messageConfiguration.getMessageByCode(MessageCodeEnum.ERROR_INVALID_OBJECT), HttpStatus.BAD_REQUEST);
        }
        final Optional<UserEntity> optionalUserEntity = userRepository.findById(lineDTO.getUserId());
        if (optionalUserEntity.isEmpty()) {
            final UserEntity userEntity = new UserEntity();
            userEntity.setId(lineDTO.getUserId());
            userEntity.setName(lineDTO.getUserName());
            userEntity.setCreateDate(LocalDateTime.now());
            userRepository.save(userEntity);
        }
    }
}
