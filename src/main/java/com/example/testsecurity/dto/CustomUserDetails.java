package com.example.testsecurity.dto;

import com.example.testsecurity.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    /**
     * 사용자의 특정 권한(Role)에 대해서 return해주는 메소드
     * @return Collection<? extends GrantedAuthority>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 사용자 계정이 만료되었는지 확인
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 사용자 계정이 잠겼는지 확인
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 사용자 계정의 자격증명이 만료되었는지 확인
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 계정이 사용가능한지 확인
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
