package com.springstudy.shop.entity;

import com.springstudy.shop.constant.ItemSellStatus;
import com.springstudy.shop.dto.MemberDTO;
import com.springstudy.shop.entity.*;
import com.springstudy.shop.repository.ItemRepository;
import com.springstudy.shop.repository.MemberRepository;
import com.springstudy.shop.repository.OrderItemRepository;
import com.springstudy.shop.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional// 테스트용, rollback자동으로 작동
@Log4j2
@TestPropertySource(locations = "classpath:application.properties")
public class OrderTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

//    @Autowired(required=false)
//    Member member;

    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager em;

    // 상품아이템 생성
    public Item createItem(){
        Item item = new Item();

        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트상품 상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegDate(LocalDateTime.now());
        item.setModDate(LocalDateTime.now());

        return item;

    }

    @Test@DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();

        for(int i=0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);// 주문상품
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            // 주문서 정보(주문서, 주문상품, 수량,금액)
            order.getOrderItems().add(orderItem);
        }
        
        // 엔티티르 저장하면서  강제로 flush호출=>DB반영
        orderRepository.saveAndFlush(order);
        em.clear();// DB에 있는 데이터 호출 하기위해 영속성컨텍스트상태를 초기화

//        Optional<Board> result = orderRepository.findById(order.getId());
//        Order saveOrder = result.orElseThrow();
        Order saveOrder = orderRepository.findById(order.getId())
                .orElseThrow();

        Assertions.assertEquals(3, saveOrder.getOrderItems().size());
    }


    // 고객 정보 생성
    public Member createMember(){
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setEmail("test@email.com");
        memberDTO.setName("홍길동");
        memberDTO.setAddress("양산시");
        memberDTO.setPassword("1111");

        // dto -> entity, 암호화 적용
        return Member.createMember(memberDTO, passwordEncoder);
    }
    public Order createOrder(){
        Order order = new Order();

        for(int i=0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);// 주문상품
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            // 주문서 정보(주문서, 주문상품, 수량,금액)
            order.getOrderItems().add(orderItem);

        }

        Member member = new Member();
        //member = this.createMember();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();

        // 부모 엔티티와 연관관계가 끊어졌기 때문에 고아객체를 삭제하는 쿼리문이실행
        // Cascade REMOVE옵션인 경우 :
        // 부모 엔티티가 삭제될 때 연관된 자식 엔티티도 함께 삭제
        // order를 삭제하면 order에 매핑되어 있던 orderItem이 함께 삭제
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("======> ");
        log.info("order class:" + orderItem.getOrder().getClass());
    }


}
