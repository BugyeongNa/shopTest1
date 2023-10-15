package com.springstudy.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    // 연관관계 맵핑할 경우 : 참조하는 FK키를 기준으로 설정
    // 하나의 장부구니에는 여러개의 상품을 담을 수 있는 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    // 하나의 상품은 여러 장바구니의 장바구니 상품을 담을 수 있는 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    private int count;

}

/*
장바구니 아이템 엔티티 설계:(장바구니정보, 상품정보, 맵핑관계)

==========================================================================
장바구니        1:N(상품)     장바구니와상품연결맵핑  M(장바구니):1     상품
==========================================================================
cart           ->           cart_item               ->          item
==========================================================================
cart_id(PK)                 cart_item_id(PK)                    item_id(PK)
membeer_id(FK)              cart_id(FK)                         item_nm
                            item_id(FK)                         price
                            count                               stock_number, item_....


 cart_item
 id         cart        item
1           A           A1
2           A           A2
3           B           A1
4           B           A2
5           C           A1
..


 */
