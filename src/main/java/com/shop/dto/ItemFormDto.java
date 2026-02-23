package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id; // 상품 ID. 신규 등록 시에는 null.

    @NotBlank(message = "상품명은 필수 입력 값입니다.")  // @NotBlank는 String 전용으로 null + "" + 공백 막음
    // 에러 메시지: "상품명은 필수 입력 값입니다."
    private String itemNm; // 상품명. 값이 비어있으면 유효성 검사 실패.

    @NotNull(message = "가격은 필수 입력 값입니다.")  // @NotNull는 Integer에는 사용, 모든 객체 null만 막음
    private Integer price;
    // 추가로 @NotEmpty 는 String, List 등 null + 빈값 막음 (공백은 허용)

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;   // 상품 상세 설명.

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;   // 판매 상태 Enum.

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();  // 상품에 등록된 이미지 목록. 화면에 보여줄 이미지들.
    // 기본값을 new ArrayList로 초기화 → NullPointerException 방지.

    private List<Long> itemImgIds = new ArrayList<>(); // 이미지 ID 목록.
    // 수정 시 어떤 이미지가 기존 이미지인지 구분하기 위해 사용.

    private static ModelMapper modelMapper = new ModelMapper();  // 객체 매핑용 인스턴스.
    // static -> 공통으로 하나만 사용.

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }  // 현재 DTO 객체를 Item 엔티티 객체로 변환, DTO 값을 Entity에 자동 복사

    public static ItemFormDto of(Item item){  // of() 메서드 Entity → DTO 변환, 수정 화면에 기존 상품 데이터를 뿌릴 때 사용
        return modelMapper.map(item, ItemFormDto.class);
    }
}
