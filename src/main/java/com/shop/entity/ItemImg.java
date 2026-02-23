package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity  // JPA가 관리하는 엔티티, DB 테이블과 매핑됨
@Table(name = "item_img")  // 실제 DB 테이블 이름은 item_img
@Getter
@Setter
public class ItemImg extends BaseEntity {
    @Id
    @Column(name = "item_img_id")  // PK 컬럼 이름: item_img_id
    @GeneratedValue(strategy = GenerationType.AUTO)  // 값 자동 생성 (AUTO 전략)
    private Long id;

    private String imgName;  // 서버에 저장된 실제 파일명

    private String oriImageName;  // 사용자가 업로드한 원본 파일명

    private String imgUrl;  // 이미지 접근 경로 (ex: /images/xxx.jpg)

    private String repImgYn;  // 대표 이미지 여부, 보통 "Y" 또는 "N"

    @ManyToOne(fetch = FetchType.LAZY) // 여러 이미지 -> 하나의 상품(Item) /  Item 1 : N ItemImg
    @JoinColumn(name = "item_id") // item_id라는 컬럼을 만들고,
    private Item item; //  그걸 item 테이블의 PK와 연결함.

    // updateItemImg는 변경감지가 작동하도록 값을 바꾸는 도메인 메서드다.
    public void updateItemImg(String oriImageName, String imgName, String imgUrl){
        this.oriImageName = oriImageName;
        this.imgName = imgName;  //
        this.imgUrl = imgUrl;
    }
}
