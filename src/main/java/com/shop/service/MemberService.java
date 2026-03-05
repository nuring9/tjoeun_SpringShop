package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor  // final, @NotNull이 붙은 필드(변수)에 자동으로 생성자 주입 된다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    // JPA Repository, DB 접근 담당
    //final → 반드시 생성자 주입

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        // 1.중복 회원 검사
        return memberRepository.save(member);
        // 2.문제 없으면 저장 3.저장된 Entity 반환
    }


    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }  // 구글, 네이버 로그인때문에 추가, 이메일로 회원 조회


    // 회원가입 시 이메일 중복 여부를 검사하는 메서드
    private void validateDuplicateMember(Member member) {

        // 회원가입하려는 회원의 이메일로 DB에서 기존 회원 조회
        Member findMember = memberRepository.findByEmail(member.getEmail());

        // 동일한 이메일을 가진 회원이 이미 존재하면
        if (findMember != null) {
            // 회원가입을 막기 위해 예외 발생
            // IllegalStateException : 현재 상태에서 허용되지 않는 작업이라는 의미
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    // 이메일 기준으로 DB 조회 -> 이미 존재하면 예외 발생 -> 없으면 정상 진행


    @Override
    // UserDetailsService 인터페이스에서 구현해야 하는 메서드
    // Spring Security가 로그인 시 자동으로 호출하는 메서드
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 로그인 시 입력한 email을 기준으로 DB에서 회원 조회
        Member member = memberRepository.findByEmail(email);

        // 해당 이메일을 가진 회원이 존재하지 않으면
        if (member == null) {
            // UsernameNotFoundException 예외 발생
            // Spring Security에서 "존재하지 않는 사용자"로 처리됨
            throw new UsernameNotFoundException(email);
        }

        // Spring Security에서 사용할 UserDetails 객체 생성 후 반환
        return User.builder()
                .username(member.getEmail())   // 로그인 아이디(여기서는 email)를 username으로 설정
                .password(member.getPassword())   // DB에 저장된 암호화된 비밀번호 설정
                .roles(member.getRole().toString())   // 회원의 권한(Role)을 설정, 예: ROLE_USER, ROLE_ADMIN
                .build();  // UserDetails 객체 생성
    }
}
// 현재 방식은 동시성 문제가 발생할 수 있다.
// A 사용자가 동시에 2번 요청 -> 두 요청 모두 validate 통과 -> 두 개 다 저장됨 -> 중복 데이터 발생
// DB에 Unique 제약조건 추가 (필수)
