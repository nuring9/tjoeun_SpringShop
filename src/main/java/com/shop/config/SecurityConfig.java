package com.shop.config;

// 스프링 설정 관련 어노테이션과 클래스 import

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
// 이 클래스가 설정 클래스임을 의미 (스프링이 설정 파일로 인식)
@EnableWebSecurity
// Spring Security 기능 활성화
public class SecurityConfig {
    @Autowired
    MemberService memberService;

    // Spring Security의 필터 체인을 Bean으로 등록
    // 필터체인이란 웹 요청이 들어오면 가장 먼저 거치는 보안 검사 라인, 즉 컨트롤러 가기 전에 스프링 시큐리티가 먼저 검사함.
    // HttpSecurity 객체를 이용해 보안 설정을 구성할 수 있음
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Spring Security가 사용할 보안 필터 체인 설정, 이 메서드가 반환하는 객체가 Spring Security의 실제 보안 규칙
        // HttpSecurity http = "보안 설정 빌더"(URL 접근 권한,로그인 설정,로그아웃 설정,CSRF,세션 정책)
        http.authorizeHttpRequests(auth -> auth  // HTTP 요청에 대한 접근 권한을 설정하겠다(auth는 빌더 객체)
                                .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/error").permitAll()
                                // css 폴더 하위 전부, js 폴더 하위 전부, img 폴더 하위 전부, favicon, error 이 URL들은 로그인 없이 접근 허용
                                .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                                // 일반 페이지 허용(메인 페이지, 회원가입, 상품 페이지) 모두 비회원 접근 가능.
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                //   /admin/ 으로 시작하는 URL은 ROLE_ADMIN 권한이 있어야 접근 가능(DB에 저장된 권한이 ROLE_ADMIN)
                                .anyRequest().authenticated()
                        // 나머지 모든 요청(위에 명시하지 않은 모든 요청은) 로그인해야 접근 가능
                )
                .formLogin(formLogin -> formLogin  // Spring Security에서 기본 제공하는 로그인 방식을 사용하겠다는 의미
                        .loginPage("/members/login")  // 사용자 정의 로그인 페이지 경로 지정 , 기본 로그인 페이지 대신 /members/login 경로의 컨트롤러 + HTML을 사용하겠다는 의미
                        .defaultSuccessUrl("/")  // 로그인 성공 시 이동할 기본 URL
                        .usernameParameter("email") // 로그인 폼에서 아이디 파라미터 이름 지정 , MemberService를 email로 만들어놓음
                        .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL
                ).logout(logout -> logout
                        .logoutRequestMatcher(
                                PathPatternRequestMatcher
                                        .withDefaults().matcher(HttpMethod.GET, "/members/logout")
                        )
                        .logoutSuccessUrl("/")
                );
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        // custom 예외처리 추가

        // 실제 운영에서는 http.build()를 반환해야 정상 동작(Spring Security가 동작)
        // 실제 SecurityFilterChain 객체로 생성
        return http.build();
    }

    // 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
    @Bean
    public static PasswordEncoder passwordEncoder() {

        // DelegatingPasswordEncoder 생성
        // 여러 암호화 방식 지원 (bcrypt 기본 사용) -> DB에 암호화 된 값
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // password 확인
    // "사용자 정보를 어디서 가져올지 비밀번호를 어떻게 비교할지"
    // AuthenticationManagerBuilder 인증을 관리하는 객체
    // auth.userDetailsService(memberService) 사용자 정보를 DB에서 가져오는 서비스 지정
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
    // Spring Security는 로그인 시
    // 입력한 email 전달
    //DB에서 해당 회원 조회
    //UserDetails 객체 반환
    //권한 포함
}
