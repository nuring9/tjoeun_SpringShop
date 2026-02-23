package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest  // 스프링 전체 컨텍스트 띄워서 실제 환경처럼 테스트
@Transactional  // 테스트 끝나면 자동 롤백 → DB에 실제 반영 안 됨 (테스트 안전)
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {
    @Autowired  // 스프링이 객체를 대신 만들어서 자동으로 넣어주는 기능 (의존성 주입, DI)
            // 의존성 주입 : “내가 필요한 객체를 직접 만들지 않고, 외부에서(스프링) 대신 만들어서 넣어주는 것” new 안 해도 됨.
    OrderRepository orderRepository;  // 스프링이 OrderRepository 구현체를 자동으로 주입

    @Autowired
    ItemRepository itemRepository;  // Item 엔티티를 DB에 저장하거나 조회할 때 사용.

    @Autowired
    MemberRepository memberRepository;  // 회원 직접 조작 가능

    @Autowired
    OrderItemRepository orderItemRepository; // OrderItem을 직접 다루기 위해 필요함.

    @PersistenceContext
    EntityManager em;   // EntityManager란? 영속성 컨텍스트 관리 flush clear detach 직접 JPQL 실행,
    // 영속성 컨텍스트 초기화위해 사용 em.clear();

    // repository CRUD 편하게 사용
    // EntityManager JPA 핵심 엔진 직접 제어


    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 성공");
        item.setPrice(10000);
        item.setItemDetail("상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")  // Order 저장 시 OrderItem도 같이 저장되는지 확인
    public void cascadeTest() {
        Order order = new Order();  // 아직 DB에 저장 안 됨.

        for (int i = 0; i < 3; i++) {  // OrderItem을 3개 만듦
            Item item = this.createItem();  // OrderItem은 Item을 참조하니까 Item은 DB에 먼저 있어야 안전함.
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);   // 연관관계 주인(OrderItem)에 Order 세팅
            order.getOrderItems().add(orderItem);  // 양방향 관계 맞춰줌
        }

        orderRepository.saveAndFlush(order);
        // Order 엔티티에: @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) 있기 때문에 Order 저장하면 OrderItem 3개도 같이 저장됨
        // 이게 바로 영속성 전이 (cascade)

        em.clear();  // 메모리 캐시 제거 (진짜 DB에서 다시 조회하도록 만들기)
        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        // DB에서 Order 조회, OrderItem도 같이 조회됨 (LAZY면 접근 시 조회)

        assertEquals(3, savedOrder.getOrderItems().size());
        //OrderItem이 3개인지 확인
    }

    public Order createOrder() {
        Order order = new Order();
        for (int i = 0; i < 3; i++) {  // OrderItem을 3개 만듦
            Item item = this.createItem();  // OrderItem은 Item을 참조하니까 Item은 DB에 먼저 있어야 안전함.
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);   // 연관관계 주인(OrderItem)에 Order 세팅
            order.getOrderItems().add(orderItem);  // 양방향 관계 맞춰줌
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;

    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    // orphanRemoval = true 옵션(컬렉션에서 제거된 자식 객체를 DB에서도 삭제)이 동작하는지 확인.
    public void orphanRemovalTest() {
        Order order = this.createOrder();  // Order와 여러 OrderItem을 생성하고 연관관계 연결
        order.getOrderItems().remove(0);
        // OrderItem 리스트에서 첫 번째 객체 제거 (부모(Order)와 관계 끊김)
        em.flush();
        // DB에 반영, orphanRemoval = true 덕분에 첫 번째 OrderItem 삭제 쿼리 실행
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    //OrderItem → Order 관계에서 LAZY 로딩이 제대로 되는지 확인
    public void lazeLoadingTest(){
        Order order = this.createOrder(); // Order와 OrderItem 생성
        Long orderItemId = order.getOrderItems().get(0).getId();
        // DB 조회용

        em.flush();
        em.clear();
        // 1차 캐시 제거 → DB에서 새로 조회해야 함

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
       // OrderItem만 DB에서 조회, Order는 아직 로딩 안 됨 (LAZY)

        System.out.println("Order class : " + orderItem.getOrder().getClass());
        // 실제 Order 객체가 아닌 프록시 객체 확인 가능
        System.out.println("========");
        orderItem.getOrder().getOrderDate();
        // 실제 Order 객체 접근, 이 시점에 DB 조회 (select) 발생
        System.out.println("========");

    }
}