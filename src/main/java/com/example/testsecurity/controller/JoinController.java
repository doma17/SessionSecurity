package com.example.testsecurity.controller;

import com.example.testsecurity.dto.JoinDto;
import com.example.testsecurity.service.JoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class JoinController {

    private JoinService joinService;

    @Autowired
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDto joinDto) {
        log.info("[joinProcess] JoinDto = {}", joinDto.getUsername());

        joinService.joinProcess(joinDto);

        return "redirect:/login";
    }
}
