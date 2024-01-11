package com.example.testsecurity.service;

import com.example.testsecurity.dto.JoinDto;
import com.example.testsecurity.entity.UserEntity;
import com.example.testsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 가입 불가 문자 정규식 처리
     * - 아이디의 자리수 제한
     * - 아이디의 특수문자 포함 불가
     * - admin과 같은 아이디 사용불가
     * - 비밀번호 자리수
     * - 비밀번호 특수문자 포함 필수
     * ... 과 같은 regex를 사용하는 방법도 생각
     */
    public void joinProcess(JoinDto joinDto) {
        // username 중복 검증 메소드 구현
        if (userRepository.existsByUsername(joinDto.getUsername())) {
            return;
        }

        UserEntity user = new UserEntity();

        user.setUsername(joinDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }
}
