package com.shop.controller;

import com.shop.service.NaverAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class NaverAuthController {
    private final NaverAuthService naverAuthService;

    @GetMapping("/naver/callback")
    public String callback(@RequestParam String code, @RequestParam String state, HttpServletRequest request) {

        System.out.println("code = " + code);
        System.out.println("state = " + state);
        System.out.println("request = " + request);
        naverAuthService.login(code, request);

        return "redirect:/";
    }
}
