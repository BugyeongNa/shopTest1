package com.springstudy.shop.entity;


import com.springstudy.shop.dto.MemberDTO;
import com.springstudy.shop.entity.Cart;
import com.springstudy.shop.entity.Member;
import com.springstudy.shop.repository.CartRepository;
import com.springstudy.shop.repository.MemberRepository;
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


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional// 테스트용, rollback자동으로 작동
@Log4j2
@TestPropertySource(locations = "classpath:application.properties")
public class CartTest {
    @Autowired
    CartRepository cartRepository ;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("test@email.com");
        memberDTO.setName("홍길동");
        memberDTO.setAddress("경남 양산시");
        memberDTO.setPassword("1234");

        return Member.createMember(memberDTO, passwordEncoder);
    }

    @Test
    @DisplayName("장마구니 회원 덴티티 매핑 조회 테스트")
    public void findCartAandMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        // JPA는 영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날 때 flush()호출하여
        // 데이터베이스에 반영
        em.flush();
        // JPA는 영속성 컨텍스트로부터 엔티티를 조회 후 영속성 컨텍스트에 엔티티가 없을 경우
        // 데이터베이스를 조회
        em.clear();

        Cart saveCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(saveCart.getMember().getId(), member.getId());


    }
}
