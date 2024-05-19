package me.kong.userservice.service;

import me.kong.userservice.common.exception.DuplicateElementException;
import me.kong.userservice.controller.dto.UserRegisterRequestDto;
import me.kong.userservice.domain.entity.user.User;
import me.kong.userservice.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    UserRegisterRequestDto registerRequestDto;

    @BeforeEach
    void init() {
        registerRequestDto = UserRegisterRequestDto.builder()
                .email("email@email.com")
                .password("password")
                .name("test")
                .nickname("test nickname")
                .build();
    }

    @Test
    @DisplayName("중복된 이메일로 회원 가입 시 DuplicateElementException을 발생시킨다")
    void saveMemberWithDuplicatedEmail() {
        //given
        when(userRepository.existsUserByEmail(any())).thenReturn(true);

        //then
        assertThrows(DuplicateElementException.class, () -> userService.addNewUser(registerRequestDto));
    }

    @Test
    @DisplayName("중복되지 않은 이메일로 회원 가입 시, 사용자를 저장한다.")
    void saveMember() {
        //given
        when(userRepository.existsUserByEmail(any())).thenReturn(false);

        //when
        userService.addNewUser(registerRequestDto);

        //then
        verify(passwordEncoder, times(1)).encode(registerRequestDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이메일이 중복되었을 경우, DuplicateElementException을 발생시킨다")
    void isDuplicatedEmailExist() {
        //given
        when(userRepository.existsUserByEmail(any())).thenReturn(true);

        //then
        assertThrows(DuplicateElementException.class, () -> userService.checkUniqueEmail(any()));
    }

    @Test
    @DisplayName("이메일이 중복되지 않았을경우, 정상적으로 진행한다")
    void isNotDuplicatedEmailExist() {
        //given
        when(userRepository.existsUserByEmail(any())).thenReturn(false);

        //when
        userService.checkUniqueEmail(any());

        //then
        verify(userRepository, times(1)).existsUserByEmail(any());
    }

    @Test
    @DisplayName("찾으려는 사용자가 존재하지 않을 경우 NoSuchElementException을 발생시킨다")
    void isNotExistUserFindById() {
        //given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(any()));
    }

    @Test
    @DisplayName("사용자 조회에 성공했을 경우, 해당 사용자를 반환한다.")
    void isExistUserFindById() {
        //given
        User user = mock();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        //when
        User findUser = userService.findUserById(any());

        //then
        assertEquals(user, findUser);
    }
}