package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
    // 자동(new안하고) 와이어 연결(필요한 곳에 꽂아줌)  -> 컨테이너(스프링이 객체관리하는 곳)에서 ItemRepository 객체를 가지고 온다.
    // “내가 직접 new 하지 말고, 스프링이 만들어 둔 객체를 자동으로 넣어줘”
    // ItemRepository itemRepository = new ItemRepository();     =>  @Autowired  ItemRepository itemRepository;
    // 인터페이스는 new로 생성 불가하므로 @Autowired 를 사용해서 인터페이스지만 스프링이 객체를 만들어 줌.
    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Querydsl 조회테스트1")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" +"테스트 상품 상세 설명" +"%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }


    public void createItemList(){
        for(int i = 1; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
            // item 객체를 DB에 저장하는 기능
        }
    }

    @Test
    @DisplayName("상품 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public  void findByItemNmOrItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        // 어떤 값보다 작은 데이터 찾을 때 사용.
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@nativeQuery를 이용한 상품 조회 테스트")
    public void findByItemDetailByNativeTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }



    @Test
    @DisplayName("")
    public void findByPriceLessThanOrderByPriceDesc(){
        // 메서드명은 마음대로 
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        // itemRepository. 여기 이름은 인터페이스를 맞춰야해서 이름이 내가만든거 그대로 사용
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

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

    public void createItemList2(){
        for(int i = 1; i<=5; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품");
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
        for(int i = 6; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }
    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){
        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // BooleanBuilder => 조건문을 모으는 역할 (쿼리 조건을 구성) QueryDSL이 되야함.
        QItem item = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%")); // 테스트 상품 상세 설명이 포함된 경우
        booleanBuilder.and(item.price.gt(price)); // price(10003)보다 가격에 더 큰 경우

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            // StringUtils => String이랑 비슷한데,문자열 비교를 null이여도 안전하게 equals 비교해주는유틸 클래스
            // 즉, null safe => null이여도 죽지않음.
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        // Pageable => 페이징 정보를 담는 객체(페이지 단위로 select 하려고 사용)
        Pageable pageable = PageRequest.of(0,5);
        // 0 → 첫 번째 페이지 (0부터 시작) 5 → 한 페이지에 5개
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        // itemRepository.findAll 로 조건에 맞는 전체 데이터 기준으로 페이징된 결과를 반환(개수와 데이터)

        System.out.println("total elements :" + itemPagingResult.getTotalElements());
        // getTotalElements() 조건에 맞는 전체 개수 (페이징 적용 전, 조건에 맞는 전체 개수)


        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem : resultItemList){
            System.out.println(resultItem.toString());
        }
    }


}