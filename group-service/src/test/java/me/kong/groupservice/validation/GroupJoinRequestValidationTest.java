package me.kong.groupservice.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
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
        assertEquals("비어 있을 수 없습니다", violation.getMessage());
    }
}
