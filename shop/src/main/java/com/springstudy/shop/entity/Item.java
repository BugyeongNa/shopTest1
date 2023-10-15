package com.springstudy.shop.entity;

import com.springstudy.shop.constant.ItemSellStatus;
import com.springstudy.shop.dto.ItemFormDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="item")
public class Item extends  BaseEntity{
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String itemNm;
    @Column(name="price", nullable = false)
    private int price;
    @Column(nullable = false)
    private int stockNumber;
    @Lob
    @Column(nullable = false)
    private String itemDetail;
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

//    private LocalDateTime regTime;//상품 등록시간
//    private LocalDateTime updateTime;// 상품 수정시간

    // 수정작업 메서드 선언(테스트용)
    public void change(String itemNm, String itemDetail){
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
    }

    // 수정폼으로 부터 변경된 상품정보를 entity에 전달
    public void updateItem(ItemFormDTO itemFormDTO){
        this.itemNm = itemFormDTO.getItemNm();
        this.price = itemFormDTO.getPrice();
        this.stockNumber = itemFormDTO.getStockNumber();
        this.itemDetail = itemFormDTO.getItemDetail();
        this.itemSellStatus = itemFormDTO.getItemSellStatus();
    }

}

/*
JPA(Java Persistence API): 자바 ORM에 대한 API

자바: 객체지향페러다임
관계형 데이터베이스: 데이터를 정규화해서 보관
객체 -> SQL문을 통해 변환 -> DB보관
객체  -> ORM -> DB  : 자바객체와 관계형데이터베이스 매핑

JPA: ORM기술의 표준명세로 자바에서 제공하는 API
JPA: 인터페이스 -> 구현체: Hibernate, ......

JPA 동작방식
EntityManagerFactory -> EntityManager -> Persistance Context(Entity객체 생성,....):영속성 컨텍스트






*/