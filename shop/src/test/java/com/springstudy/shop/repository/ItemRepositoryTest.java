package com.springstudy.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springstudy.shop.constant.ItemSellStatus;
import com.springstudy.shop.entity.Item;
import com.springstudy.shop.entity.QItem;
import com.springstudy.shop.repository.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
//@TestPropertySource(locations = "classpath:application-test.properties")
@TestPropertySource(locations = "classpath:application.properties")
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    @DisplayName("상품 저장 테스트")
    public void createItem(){
        Item item = new Item();

        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트상품 상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegDate(LocalDateTime.now());
        item.setModDate(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        log.info(savedItem);

    }

    public void createItemList(){
        for (int i=1; i<=10; i++){
            Item item = Item.builder()
                    .itemNm("테스트 상품"+i)
                    .price(10000+i)
                    .itemDetail("테스트상품 상세설명"+i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
//                    .regTime(LocalDateTime.now())         // 추후 자동 생성
//                    .updateTime(LocalDateTime.now())
                    .build();


            Item savedItem = itemRepository.save(item);
        }
    }
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();

        //List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        List<Item> itemList = itemRepository.findAll();
        itemList.forEach(item ->{
            log.info(item);
        });
    }

    @Test
    @DisplayName("상품명, 상품상세설명: Or조건 테스트")
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();

        //List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트상품 상세설명5");
        itemList.forEach(item ->{
            log.info(item);
        });
    }

    @Test
    @DisplayName("가격필드 기준: LessThan 조건 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();

        List<Item> itemList = itemRepository.findByPriceLessThanOrderByItemNmDesc(10005);
        itemList.forEach(item ->{
            log.info(item);
        });
    }

    @Test
    @DisplayName("상품이름, 상품설명: 수정작업 테스트")
    public void findByUpdate(){
        this.createItemList();

        Long item_id = 5L;
        Optional<Item> item_result = itemRepository.findById(item_id);
        Item item = item_result.orElseThrow();

        item.change("update 테스트 상품5", "update 상품상세설명5");
        itemRepository.save(item);

        List<Item> itemList = itemRepository.findAll();
        itemList.forEach(vo ->{
            log.info(vo);
        });
    }

    @Test
    @DisplayName("상품코드: 삭제작업 테스트")
    public void findByDelete(){
        this.createItemList();

        Long item_id = 5L;
        itemRepository.deleteById(item_id);

        List<Item> itemList = itemRepository.findAll();
        itemList.forEach(vo ->{
            log.info(vo);
        });
    }

    @Test
    @DisplayName("@Query어노테이션을 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetail("9");
        itemList.forEach(item ->{
            log.info(item);
        });
    }

    @Test
    @DisplayName("@Query어노테이션 navieQuery=true 을 이용한 상품 조회 테스트")
    public void findByItemDetailTest2(){
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetailByNative("9");
        itemList.forEach(item ->{
            log.info(item);
        });
    }

    @Test
    @DisplayName("Pageable 객체 : 페이징객체 테스트")
    public void pageTest(){
        this.createItemList();

        // 페이징 설정: ageRequest.of(페이지번호, 1페이지구성하는 레코드개수, 정렬옵션)
        Pageable pageable = PageRequest.of(3,3, Sort.by("id").descending());
        Page<Item> itemList = itemRepository.findAll(pageable);
        itemList.forEach(item ->{
            log.info(item);
        });

        log.info("total count:"+itemList.getTotalElements());
        log.info("total page:"+itemList.getTotalPages());
        log.info("page number:"+itemList.getNumber());
        log.info("page size:"+itemList.getSize());

    }

    @Test
    @DisplayName("QueryDsl1 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"테스트상품 상세설명"+"%"))
                .orderBy(qItem.price.desc());

        // List<T> fetch() : 조회 결과 리스트반환
        // T fetchOne : 조회 대상이 1건인 경우 제네릭을 지정한 타입반환
        // T fetchFirst() : 조회 대상 중 1건만 반환
        // Long fetchCount(): 조회 대상 개수 반환
        // QueryResult<T> fetchResults() : 조회한 리스트와 전체 개수를 포함한 QueryResults반환
        List<Item> itemList = query.fetch();

        itemList.forEach(item ->{
            log.info(item);
        });
        // 메서드 테스트
        long total = query.fetchCount();
        //Item result1 = query.fetchOne();
        Item result2 = query.fetchFirst();
        log.info(total);
        //log.info(result1);
        log.info(result2);


    }


    public void createItemList2(){
        for (int i=1; i<=5; i++){
            Item item = Item.builder()
                    .itemNm("테스트 상품"+i)
                    .price(10000+i)
                    .itemDetail("테스트상품 상세설명"+i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
//                    .regTime(LocalDateTime.now())
//                    .updateTime(LocalDateTime.now())
                    .build();

            Item savedItem = itemRepository.save(item);
        }
        for (int i=6; i<=10; i++){
            Item item = Item.builder()
                    .itemNm("테스트 상품"+i)
                    .price(10000+i)
                    .itemDetail("테스트상품 상세설명"+i)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .stockNumber(0)
//                    .regTime(LocalDateTime.now())
//                    .updateTime(LocalDateTime.now())
                    .build();

            Item savedItem = itemRepository.save(item);
        }
    }


    @Test
    @DisplayName("QueryDsl2 조회 테스트2")
    public void queryDslTest2() {
        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qItem = QItem.item;

        String itemDetail = "테스트상품 상세설명";
        int price = 10003;
        String itemSellStatus = "SELL";

        booleanBuilder.and(qItem.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(qItem.price.gt(price));
        log.info(ItemSellStatus.SELL);

        if (StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)){
            booleanBuilder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        PageRequest pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        log.info("totoal elements:"+itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        resultItemList.stream().forEach(item -> {
            log.info(item);
        });

    }

}
