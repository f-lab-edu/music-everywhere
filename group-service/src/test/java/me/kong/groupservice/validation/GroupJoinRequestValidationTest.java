package me.kong.groupservice.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import me.kong.groupservice.dto.request.GroupJoinProcessDto;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.enums.GroupJoinProcessAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupJoinRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("모두 정상적으로 입력받았을 경우 검증에 성공한다")
    void successToCreateNewGroupJoinRequest() {
        GroupJoinRequestDto dto = GroupJoinRequestDto.builder()
                .nickname("testUser")
                .requestInfo("info")
                .build();

        Set<ConstraintViolation<GroupJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("닉네임이 비어있을 경우 변환에 실패한다")
    void failedByEmptyNickname() {
        GroupJoinRequestDto dto = GroupJoinRequestDto.builder()
                .nickname("")
                .requestInfo("info")
                .build();

        Set<ConstraintViolation<GroupJoinRequestDto>> violations = validator.validate(dto);
        ConstraintViolation<GroupJoinRequestDto> violation = violations.iterator().next();
        assertEquals("nickname", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("가입 요청 처리의 action이 존재한다면 검증에 성공한다")
    void successToProcessGroupJoinRequest() {
        GroupJoinProcessDto dto = GroupJoinProcessDto.builder()
                .action(GroupJoinProcessAction.APPROVE)
                .build();

        Set<ConstraintViolation<GroupJoinProcessDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("가입 요청 처리의 action이 비어있다면 검증에 실패한다")
    void failedByEmptyAction() {
        GroupJoinProcessDto dto = GroupJoinProcessDto.builder()
                .action(null)
                .build();

        Set<ConstraintViolation<GroupJoinProcessDto>> violations = validator.validate(dto);
        ConstraintViolation<GroupJoinProcessDto> violation = violations.iterator().next();
        assertEquals("action", violation.getPropertyPath().toString());
    }
}
