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
        return memberRepository.save(member);
    }
    // 1.중복 회원 검사 2.문제 없으면 저장 3.저장된 Entity 반환

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    // 이메일 기준으로 DB 조회 -> 이미 존재하면 예외 발생 -> 없으면 정상 진행
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder().username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
// 현재 방식은 동시성 문제가 발생할 수 있다.
// A 사용자가 동시에 2번 요청 -> 두 요청 모두 validate 통과 -> 두 개 다 저장됨 -> 중복 데이터 발생
// DB에 Unique 제약조건 추가 (필수)
