package com.shop.entity;

import com.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity  // 테이블로 매핑
@Table(name = "orders")  // DB 테이블 이름은 orders
@Getter
@Setter

public class Order extends BaseEntity {
    @Id
    @Column(name = "order_id")  // PK 컬럼 이름 → order_id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)  // order 가 메인, 여러 주문(Order)은 한 명의 회원(Member)에 속함. Member 1 : N Order
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;  // 주문한 시간

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)  // "하나의 Order는 여러 개의 OrderItem을 가진다" Order 1 : N OrderItem 관계
    // order 필드가 실제 외래키(FK)를 관리하는 주인(owner)
    // orphanRemoval = true -> 부모(Order)와 관계가 끊어진 자식(OrderItem)을 자동으로 DB에서 삭제

    // cascade = CascadeType.ALL = 부모 엔티티의 작업을 자식 엔티티에게 전파, Order(부모) OrderItem(자식)

    private List<OrderItem> orderItems = new ArrayList<>();
    // Order 하나에 여러 OrderItem이 들어가니까 컬렉션(List)로 관리

    @Enumerated(EnumType.STRING)  // Enum 저장 방식 지정
    // DB에 "ORDER", "CANCEL" 이런 문자열로 저장됨(숫자(ORDINAL)보다 안전)

    private OrderStatus orderStatus;

//    private LocalDateTime regTime;
//
//    private LocalDateTime upateTime;

   public void addOrderItem(OrderItem orderItem){
       orderItems.add(orderItem);
       orderItem.setOrder(this);
   }

   public static Order createOrder(Member member, List<OrderItem> orderItemList){
       Order order = new Order();
       order.setMember(member);
       for(OrderItem orderItem : orderItemList){
           order.addOrderItem(orderItem);
       }
       order.setOrderStatus(OrderStatus.ORDER);
       order.setOrderDate(LocalDateTime.now());
       return order;
   }

   public int getTotalPrice(){
       int totalPrice = 0;
       for(OrderItem orderItem : orderItems){
           totalPrice += orderItem.getTotalPrice();
       }
       return totalPrice;
   }

   public void cancelOrder(){
       this.orderStatus = OrderStatus.CANCEL;

       for(OrderItem orderItem : orderItems){
           orderItem.cancel();
       }
   }
}
