package me.kong.userservice.service;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import me.kong.userservice.controller.dto.UserRegisterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("회원 가입 시, 모두 정상적으로 입력할 경우 validation을 통과한다")
    void registerSuccess() {
        UserRegisterRequestDto registerDto = UserRegisterRequestDto.builder()
                .email("test@test.com")
                .password("password")
                .name("testuser")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequestDto>> validate = validator.validate(registerDto);
        assertThat(validate).isEmpty();
    }

    @Test
    @DisplayName("회원 가입 시, 잘못된 이메일 형식일 경우 validation에 실패한다")
    void registerFailedByEmailFormat() {
        UserRegisterRequestDto registerDto = UserRegisterRequestDto.builder()
                .email("test#test.com")
                .password("password")
                .name("testuser")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequestDto>> validate = validator.validate(registerDto);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("유효하지 않은 이메일 형식입니다.");
    }

    @Test
    @DisplayName("회원 가입 시, NotEmpty 속성이 비어있을 경우 validation에 실패한다")
    void registerFailedByEmptyField() {
        UserRegisterRequestDto registerDto = UserRegisterRequestDto.builder()
                .email("test@test.com")
                .password("")
                .name("")
                .nickname("")
                .build();

        Set<ConstraintViolation<UserRegisterRequestDto>> validate = validator.validate(registerDto);
        assertThat(validate.size()).isEqualTo(3);
    }
}
