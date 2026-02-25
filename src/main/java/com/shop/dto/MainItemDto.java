package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    @QueryProjection // qureydsl 사용하게 함. 원래는 entity만 사용됬는데, DTO도 가능하게 하기위해 넣는 어노테이션
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
