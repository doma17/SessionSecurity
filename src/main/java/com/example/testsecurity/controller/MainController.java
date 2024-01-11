package com.example.testsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Model model) {

        /**
         * 로그인한 사용자의 ID 값
         */
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        /**
         * 로그인한 사용자의 Role
         * e.g. 특정 글 삭제시 ADMIN 계정에서만 허용하기 위해서 이러한 검증 로직을 추가해야함
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        model.addAttribute("id", id);
        model.addAttribute("role", role);

        return "main";
    }
}
