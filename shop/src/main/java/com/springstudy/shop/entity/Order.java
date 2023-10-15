package com.springstudy.shop.entity;

import com.springstudy.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter@Setter
public class Order extends BasicEntity{
    @Id @GeneratedValue
    @Column(name ="order_id")
    private Long id;

    // 한 명의 회원은 여러 번 주문을 할 수 있는 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;// 주문상태

    // 하나의 주문이 여러 개의 주문 상품을 갖는 List자료형으로 맵핑
    // Order엔티티가 주인(주체)이 아니므로 "mappedby"속성으로 연관관계의 주인을 설정
    // orderItems에 있는 Order에 의해 관리
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            orphanRemoval = true, // 고아객체 제거
            cascade = CascadeType.ALL) // 고객이 주문할 상품을 선택하고 주문할 때 주문 엔티티를 저장하면서 주문 상품 엔티티도 함께 저장되는 경우
    private List<OrderItem> orderItems = new ArrayList<>();

    // 1주문서 : 1개상품만 주문
//    @JoinColumn(name = "orderItem_id")
//    private OrderItem orderItem;

//    private LocalDateTime regTime;
//    private LocalDateTime updateTime;
}

// cascade = CascadeType.ALL
// 영속성전이 : 부모 엔티티의 영속성 상태 변화를 자식 엔티티 모두 전이하는 옵션
// - 부모엔티티 저장되면, 자식 엔티티 자동 저장
// - 고객이 주문할 상품을 선택하고 주문할 때 주문 엔티티를 저장하면서
//   주문 상품 엔티티도 함께 저장되는 경우

// CASCADE종류
// PERSIST, MERGE, REMOVE, REFRESH, DETACH, ALL

/*
고아 객체 제거
 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 고아객체
 부모 엔티티를 통해서 자식의 생명주기를 관리
 주의사항: 고아객체제거기능은 참조하는 곳이 하나일 때만 사용
 주로 @OneToOne, @OneToMany어노테이션에 적용
 */





/*

Orders
oreder_id
member_id
oreder_date
order_status

 */