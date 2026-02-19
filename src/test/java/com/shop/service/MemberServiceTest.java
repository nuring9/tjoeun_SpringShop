package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }
    @Test
    @DisplayName("회원가입 테슽트")
    public void saveMemberTest(){
        Member member = createMember();
        // createMember()는 테스트용 객체 생성 메서드이고, saveMemberTest()는 저장 기능 검증을 위한 메서드다.
        // 즉, DB에 아직 저장되지 않았기 때문에 createMember() 를 한걸 member에 넣고
        Member savedMember = memberService.saveMember(member);
        // 저장 결과가 내가 의도한 값 그대로 유지됐는지 검증 하기 위해서 다시 또 호출
        assertEquals(member.getEmail(), savedMember.getEmail());
        // assertEquals는 저장 후에도 값이 그대로 유지되는지 검증하기 위해 하는 것
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }


    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicatememberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
        // 에러메세지 통일해야함 텍스트 띄어쓰기나 하나라도 틀리면 에러남.
    }
}