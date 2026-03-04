package com.shop.service;

import com.shop.constant.Role;
import com.shop.dto.NaverUserInfoDto;
import com.shop.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverAuthService {

    // 네이버 API 키/설정값 (application.properties에서 읽어옴)
    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    // 회원 저장/조회 + 비번 암호화(임시비번 저장용)
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 네이버 API 호출용
    private final RestClient restClient = RestClient.create();

    /**
     * 네이버 로그인 콜백에서 받은 code로:
     * 1) access_token 발급
     * 2) 사용자 정보 조회
     * 3) 우리 DB에 회원이 없으면 자동가입
     * 4) 스프링 시큐리티로 강제 로그인 처리(세션 생성)
     */
    public void login(String code, HttpServletRequest request) {

        // 1) code -> access_token
        String accessToken = requestAccessToken(code);

        // 2) access_token -> 사용자 정보
        NaverUserInfoDto userInfo = requestUserInfo(accessToken);

        System.out.println("네이버 이메일 = " + userInfo.getEmail());
        System.out.println("네이버 이름 = " + userInfo.getName());

        String email = userInfo.getEmail();
        String name = userInfo.getName();

        // 3) 우리 DB에 회원 있는지 조회
        Member member = memberService.getMemberByEmail(email);

        // 4) 없으면 자동 회원가입
        if (member == null) {
            Member newMember = new Member();

            //  필수: 이메일
            newMember.setEmail(email);

            //  선택: name 필드가 Member에 있으면 사용 (없으면 이 줄 삭제)
            newMember.setName(name);

            // 네이버는 주소/전화번호 안주기 때문에 임시값
            newMember.setAddress("네이버 로그인");
            newMember.setTel("000-0000-0000");

            //  네이버 로그인은 비번이 없어서 임시 비번 만들어 저장(암호화 필수)
            String tempPw = UUID.randomUUID().toString();
            newMember.setPassword(passwordEncoder.encode(tempPw));

            //  Member에 role이 필수라면 기본값 세팅 필요
            // 예: newMember.setRole(Role.USER);
            // 기본 권한 USER
            newMember.setRole(Role.USER);

            //  address/phone 같은 필수 컬럼(nullable=false)이 있으면 기본값 세팅 필요
            // 예: newMember.setAddress("");

            member = memberService.saveMember(newMember);
        }

        // 5) 강제 로그인 처리 (Spring Security 세션 로그인)
        // 기존 로그인 방식(UserDetailsService) 그대로 재사용
        UserDetails userDetails = memberService.loadUserByUsername(member.getEmail());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        //  세션에 SecurityContext 저장해야 다음 요청부터 "로그인 상태"로 유지됨
        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );
    }

    // ------------------- 아래는 네가 이미 만든 토큰/유저정보 요청 로직 그대로 -------------------

    private String requestAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("code", code);
        form.add("redirect_uri", redirectUri);

        Map<String, Object> tokenRes = restClient.post()
                .uri("https://nid.naver.com/oauth2.0/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<>() {});

        // ️ 토큰은 민감정보라 실서비스에서는 출력하지 않는 게 좋아요.
        // System.out.println("네이버 토큰 응답 = " + tokenRes);

        if (tokenRes == null) {
            throw new IllegalStateException("토큰 응답이 null입니다.");
        }

        Object at = tokenRes.get("access_token");
        if (at == null) {
            throw new IllegalStateException("access_token이 없습니다. 응답=" + tokenRes);
        }

        return at.toString();
    }

    private NaverUserInfoDto requestUserInfo(String accessToken) {
        Map<?, ?> res = restClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);

        if (res == null || res.get("response") == null) {
            throw new IllegalStateException("사용자 정보 조회 실패: " + res);
        }

        Map<?, ?> response = (Map<?, ?>) res.get("response");

        NaverUserInfoDto dto = new NaverUserInfoDto();
        dto.setId(toStr(response.get("id")));
        dto.setEmail(toStr(response.get("email")));
        dto.setName(toStr(response.get("name")));
        dto.setMobile(toStr(response.get("mobile")));
        return dto;
    }

    private String toStr(Object v) {
        return v == null ? null : v.toString();
    }
}