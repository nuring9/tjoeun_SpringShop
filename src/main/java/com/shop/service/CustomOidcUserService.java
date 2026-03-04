package com.shop.service;

import com.shop.constant.Role;
import com.shop.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 기본 OIDC 처리(구글 사용자 정보 가져오기)는 Spring 기본 서비스에게 맡김
    private final OidcUserService delegate = new OidcUserService();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {

        System.out.println("CustomOidcUserService 실행됨");

        OidcUser oidcUser = delegate.loadUser(userRequest);

        // 구글 계정 정보, google은 기본적으로 email, name 제공
        String email = oidcUser.getEmail();       // 제일 중요
        String name = oidcUser.getFullName();     // 없을 수도 있음

        // 혹시 email이 null이면 바로 에러 (scope에 email 빠졌거나 설정 문제)
        if (email == null) {
            throw new IllegalStateException("구글 email을 못 받았습니다. scope에 email이 포함됐는지 확인하세요.");
        }

        // 1) 우리 DB에 회원 있는지 확인
        Member member = memberService.getMemberByEmail(email);

        // 2) 없으면 자동 회원가입
        if (member == null) {
            Member newMember = new Member();
            newMember.setEmail(email);
            newMember.setName(name != null ? name : "GoogleUser");

            // 구글은 주소/전화번호 안 주므로 임시값
            newMember.setAddress("구글 로그인");
            newMember.setTel("000-0000-0000");

            // 임시 비밀번호(DB 저장용)
            newMember.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

            // 기본 권한
            newMember.setRole(Role.USER);

            member = memberService.saveMember(newMember);
        }

        // 3) 핵심: principal.getName()이 email이 되도록 "email"을 nameAttributeKey로 설정
        return new DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "email"
        );
    }
}