package com.shop.config;

// 스프링 설정 관련 어노테이션과 클래스 import

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// 이 클래스가 설정 클래스임을 의미 (스프링이 설정 파일로 인식)
@EnableWebSecurity
// Spring Security 기능 활성화
public class SecurityConfig {

    // Spring Security의 필터 체인을 Bean으로 등록
    // 필터체인이란 웹 요청이 들어오면 가장 먼저 거치는 보안 검사 라인, 즉 컨트롤러 가기 전에 스프링 시큐리티가 먼저 검사함.
    // HttpSecurity 객체를 이용해 보안 설정을 구성할 수 있음
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 현재는 보안 설정을 구성하지 않고 null 반환
        // (실제 운영에서는 http.build()를 반환해야 정상 동작)
        return null;
    }

    // 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
    @Bean
    public static PasswordEncoder passwordEncoder() {

        // DelegatingPasswordEncoder 생성
        // 여러 암호화 방식 지원 (bcrypt 기본 사용) -> DB에 암호화 된 값
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
