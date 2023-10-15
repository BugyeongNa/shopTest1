package com.springstudy.shop.entity;


import com.springstudy.shop.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional// 테스트용, rollback자동으로 작동
@Log4j2
@TestPropertySource(locations = "classpath:application.properties")
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username="gilDong", roles = "USER")
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();em.close();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow();

        log.info("=======");
        log.info("=> register time: "+member.getRegDate());
        log.info("=> update time: "+member.getModDate());
        log.info("=> create member: "+member.getCreateBy());
        log.info("=> modify member: "+member.getModifiedBy());
    }
}
