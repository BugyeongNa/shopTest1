package com.springstudy.shop.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 엔티티안에 다른 엔티티가 있을경우 연관관계 맵핑을 설정해야함.
    // 장바구니가 회원을 참조하는 형태 : 장바구니(외래키), 회원(참조:기본키)
    // 생략시(fetch = FetchType.EAGER):즉시로딩, (fetch = FetchType.LAZY):지연: 필요할 때 연결
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래키로 설정됨.
    private Member member;

}



/*
연관관계 매핑 종규 : 단방향, 양방향

일대일(1:1) @OneToOne : 회원엔티티와 장바구니 엔티티 관계
일대다(1:N) @OneToMany: 1개의 장바구니에는 여러개의 상품
다대일(N:1) @ManyToOne: (1:N)+(M:1) => 양방향 동일기능
다대다(N:M) @ManyToMany

 */