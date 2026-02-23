package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {  // 엔티티 데이터를 화면(View)이나 컨트롤러로 전달하기 위한 객체.
    private Long id;  // 이미지 고유 ID

    private String imgName;  // 저장된 이미지 파일 이름

    private String oriImgName;  // 원본 이미지 파일 이름

    private String imgUrl;  // 이미지 접근 URL

    private String repImgYn;  // 대표 이미지 여부 (Y / N)

    private static ModelMapper modelMapper = new ModelMapper();  // ModelMapper 인스턴스를 생성.
    // static으로 선언 -> 클래스 공통으로 하나만 사용. 매번 new 하지 않고 재사용 가능.

    public static ItemImgDto of(ItemImg itemImg) {
        // ItemImg 엔티티 객체를 ItemImgDto 객체로 변환해주는 메서드.
        return modelMapper.map(itemImg, ItemImgDto.class);
        // 필드명이 같으면 자동으로 값 복사. itemImg.getImgName() -> dto.setImgName()
    }
}
