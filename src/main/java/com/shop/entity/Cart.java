package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // "이 클래스는 DB 테이블로 만들어라"
@Table(name = "cart") // DB 테이블 이름은 cart
@Getter
@Setter
@ToString
public class Cart {
    @Id  // 기본키(PK)
    @Column(name = "cart_id")   // 실제 DB 컬럼 이름
    @GeneratedValue(strategy = GenerationType.AUTO)  // 자동 증가
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)  // Cart : Member = 1 : 1 관계, cart에 member_id FK 생성
    @JoinColumn(name = "member_id")  // member_id를 가져와서 Join함. 외래키(FK) 역할. Cart 테이블에 생김 (조회 시점에 JPA가 자동으로 객체를 만들어 넣어줌)
    private Member member;  // “이 Cart는 Member와 연결되어 있다”는 변수 선언
    
    // cart와 member 테이블생성
}
