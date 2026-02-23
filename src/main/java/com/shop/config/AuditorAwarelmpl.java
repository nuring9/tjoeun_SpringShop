package com.shop.config;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwarelmpl implements AuditorAware<String> {
    // JPA가 현재 로그인한 사용자를 자동으로 기록할 수 있게 해줌
    // String 타입 → 사용자 ID, 이메일 등 문자열로 기록
    @Override
    public Optional<String> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // SecurityContextHolder : 스프링 시큐리티가 현재 로그인 정보(Authentication) 를 저장하는 컨테이너
        // SecurityContextHolder.getContext().getAuthentication() → 현재 로그인한 객체 반환

        // Authentication : 로그인된 사용자 정보 담고 있음
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
}
