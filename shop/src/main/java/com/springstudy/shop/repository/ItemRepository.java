package com.springstudy.shop.repository;

import com.springstudy.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    List<Item> findByItemNm(String itemNm);
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    List<Item> findByPriceLessThanOrderByItemNmDesc(Integer price);

    // query내에 매개변수 => ":매개변수"형식으로 표시
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // navieQuery: 특정 데이터베이스에서 사용되는 쿼리를 그대로 사용(독립성을 보장하지 못함)
    @Query(value="select * from Item i where i.item_detail like %:itemDetail% order by i.price desc " ,  nativeQuery=true )
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);



    // queryDsl 추가
}

// 쿼리메서드
// find + (엔티티이름) + By + 변수이름(엔티티속성(필드)명)