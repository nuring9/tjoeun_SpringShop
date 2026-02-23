package com.shop.entity;

import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest  // 스프링 전체 컨텍스트 띄워서 실제 환경처럼 테스트
@Transactional  // 테스트 끝나면 자동 롤백 → DB에 실제 반영 안 됨 (테스트 안전)
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId()).orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + member.getRegTiem());
        System.out.println("update time : " + member.getUpdateTime());
        System.out.println("create time : " + member.getCreatedBy());
        System.out.println("modify time : " + member.getModifiedBy());

    }

}