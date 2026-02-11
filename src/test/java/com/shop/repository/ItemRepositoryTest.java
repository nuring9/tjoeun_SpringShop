package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
    // 자동(new안하고) 와이어 연결(필요한 곳에 꽂아줌)  -> 컨테이너(스프링이 객체관리하는 곳)에서 ItemRepository 객체를 가지고 온다.
    // “내가 직접 new 하지 말고, 스프링이 만들어 둔 객체를 자동으로 넣어줘”
    // ItemRepository itemRepository = new ItemRepository();     =>  @Autowired  ItemRepository itemRepository;
    // 인터페이스는 new로 생성 불가하므로 @Autowired 를 사용해서 인터페이스지만 스프링이 객체를 만들어 줌.
    @Autowired
    ItemRepository itemRepository;

    @Test  // 이 메소드는 test 이다.
    @DisplayName("상품 저장 테스트")   // 이름 상품 저장 테스트
    public void createItemTest(){
        Item item = new Item();  // Item 객체 생성
        // 생성 된 객체 item에 데이터 넣기
        item.setItemNm("테스트 상품");  
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        // itemRepository -> save를 이용해서 DB에 저장
        // 저장한 그 객체를 올려줌. savedItem 받는다.
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());  // 롬복때문에 데이터가 출력 됨.
    }

}