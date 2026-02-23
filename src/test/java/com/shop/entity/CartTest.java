package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;  // member때문에 이씀

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();  // Member 객체 생성
        memberRepository.save(member);  // DB에 저장 → member 테이블에 insert
        Cart cart = new Cart();  // Cart 객체 생성
        cart.setMember(member);  // member 설정
        cartRepository.save(cart);  // 이때 cart 테이블의 member_id에 member PK 값 들어감
        em.flush();  // 지금까지 작업한 insert를 실제 DB에 반영
        em.clear();  // 영속성 컨텍스트 초기화, 메모리에 있는 객체들 다 지움 (진짜 DB에서 다시 조회되는지 테스트하려고)
        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);  // DB에서 select 실행, Cart 객체 새로 생성
        assertEquals(savedCart.getMember().getId(), member.getId());  // 조회된 Cart의 member id , 처음 저장한 member id
    }
}