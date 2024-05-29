package me.kong.userservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.userservice.controller.dto.UserRegisterRequestDto;
import me.kong.userservice.domain.entity.user.Role;
import me.kong.userservice.domain.entity.user.User;
import me.kong.userservice.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addNewUser(UserRegisterRequestDto dto) {
        checkUniqueEmail(dto.getEmail());
        User user = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        user.updateRole(Role.USER);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void checkUniqueEmail(String email) {
        boolean isExist = userRepository.existsUserByEmail(email);
//        log.info("input email : {}", email);
        if (isExist) {
            throw new DuplicateElementException("중복된 이메일입니다. email : " + email);
        }
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 사용자가 없습니다. id : " + id));
    }
}
