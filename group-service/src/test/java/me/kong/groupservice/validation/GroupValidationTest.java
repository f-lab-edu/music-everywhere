package me.kong.groupservice.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import me.kong.groupservice.domain.entity.group.GroupScope;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupValidationTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("그룹 생성 시, 모두 정상적으로 입력됐다면 성공적으로 변환한다.")
    void successToCreateNewGroup() {
        SaveGroupRequestDto dto = SaveGroupRequestDto.builder()
                .groupName("테스트 그룹")
                .description("테스트용 그룹입니다")
                .groupScope(GroupScope.PUBLIC)
                .joinCondition(JoinCondition.OPEN)
                .nickname("테스트유저123")
                .build();

        Set<ConstraintViolation<SaveGroupRequestDto>> validate = validator.validate(dto);
        assertThat(validate).isEmpty();
    }

    @Test
    @DisplayName("그룹 생성 시, 그룹 이름이 비어있다면 변환에 실패한다")
    void failedByEmptyGroupName() {
        SaveGroupRequestDto dto = SaveGroupRequestDto.builder()
                .groupName("")
                .description("테스트용 그룹입니다")
                .groupScope(GroupScope.PUBLIC)
                .joinCondition(JoinCondition.OPEN)
                .nickname("테스트유저123")
                .build();

        Set<ConstraintViolation<SaveGroupRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<SaveGroupRequestDto> violation = violations.iterator().next();
        assertEquals("groupName", violation.getPropertyPath().toString());

    }

    @Test
    @DisplayName("그룹 생성 시, 닉네임이 비어있다면 변환에 실패한다")
    void failedByEmptyNickname() {
        SaveGroupRequestDto dto = SaveGroupRequestDto.builder()
                .groupName("테스트 그룹")
                .description("테스트용 그룹입니다")
                .groupScope(GroupScope.PUBLIC)
                .joinCondition(JoinCondition.OPEN)
                .nickname("")
                .build();

        Set<ConstraintViolation<SaveGroupRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<SaveGroupRequestDto> violation = violations.iterator().next();
        assertEquals("nickname", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("그룹 생성 시, 그룹 범위가 비어있다면 변환에 실패한다")
    void failedByNullGroupScope() {
        SaveGroupRequestDto dto = SaveGroupRequestDto.builder()
                .groupName("테스트 그룹")
                .description("테스트용 그룹입니다")
                .groupScope(null)
                .joinCondition(JoinCondition.OPEN)
                .nickname("테스트유저123")
                .build();

        Set<ConstraintViolation<SaveGroupRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<SaveGroupRequestDto> violation = violations.iterator().next();
        assertEquals("groupScope", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("그룹 생성 시, 가입 조건이 비어있다면 변환에 실패한다")
    void failedByNullJoinCondition() {
        SaveGroupRequestDto dto = SaveGroupRequestDto.builder()
                .groupName("테스트 그룹")
                .description("테스트용 그룹입니다")
                .groupScope(GroupScope.PUBLIC)
                .joinCondition(null)
                .nickname("테스트유저123")
                .build();

        Set<ConstraintViolation<SaveGroupRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<SaveGroupRequestDto> violation = violations.iterator().next();
        assertEquals("joinCondition", violation.getPropertyPath().toString());
    }

}