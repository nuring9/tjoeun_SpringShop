package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity // 이 클래스가 entity가 될예정, 테이블
@Table(name = "item") // 테이블명 item으로 
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    @Id // 이 변수가 기본키
    @Column(name = "item_id") // 테이블 컬럼명 item_id
    @GeneratedValue(strategy = GenerationType.AUTO) // 테이블 내용 들어올때마다 1씩 증가
    private Long id;  // 상품코드
    
    @Column(nullable = false, length = 50) // null 허용X, 길이 50개로 제한
    private String itemNm;  // 상품명
    
    @Column(name = "price", nullable = false)
    private int price;  // 가격
    
    @Column(nullable = false)
    private int stockNumber;   // 재고 수량
    
    @Lob // 용량 큰 데이터 컬럼
    @Column(nullable = false)
    private String itemDetail;   // 상품 상세 설명
    
    @Enumerated(EnumType.STRING)  // Enum 타입인데, DB에 넣을 때 문자열로 넣음.
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

//    private LocalDateTime regTime; // 등록 시간
//
//    private LocalDateTime updateTime;  // 수정 시간
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
}
