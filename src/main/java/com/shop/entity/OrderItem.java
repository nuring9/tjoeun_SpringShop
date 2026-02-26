package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity  // 주문 안에 들어가는 개별 상품 엔티티
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 여러 주문상품은 하나의 상품을 참조 가능
    @JoinColumn(name = "item_id") // 외래키(FK 생성)  Item 1 : N OrderItem
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 주문(Order) 여러 주문상품(OrderItem)
    // (fetch = FetchType.LAZY) -> 엔티티를 실제로 DB에서 필요할 때만 가져오겠다는 의미
    // 반대로 EAGER = 즉시 로딩 -> 엔티티를 조회할 때 무조건 join해서 같이 가져옴
    @JoinColumn(name = "order_id") // 외래키(FK 생성)  Order 1 : N OrderItem
    private Order order;

    private int orderPrice;  // 주문 당시 가격 저장 (가격 변동 대비)

    private int count;  // 구매 수량

    //    private LocalDateTime regTime;
    //    private LocalDateTime updateTime;

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}
