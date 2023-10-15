package com.springstudy.shop.service;

import com.springstudy.shop.constant.Role;
import com.springstudy.shop.dto.MemberDTO;
import com.springstudy.shop.entity.Member;
import com.springstudy.shop.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@Log4j2
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;


    public Member createMember(){
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setEmail("test@email.com");
        memberDTO.setName("홍길동");
        memberDTO.setAddress("양산시");
        memberDTO.setPassword("1111");

        // dto -> entity, 암호화 적용
        return Member.createMember(memberDTO, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){

//            Member member = createMember();
//            Member savedMember =  memberRepository.save(member);
//            log.info("saveMember=> "+savedMember);

//        IntStream.rangeClosed(1,95).forEach(i->{
//            Member member = Member.builder()
//                    .email("email"+i+"@aaa.bbb")
//                    .password(passwordEncoder.encode("1111"))
//                    .name("홍길동"+i)
//                    .address("양산시"+i)
//                    .role(Role.USER)
//                    .build();
//
//            memberService.saveMember(member);
//        });
        IntStream.rangeClosed(96,100).forEach(i->{
            Member member = Member.builder()
                    .email("email"+i+"@aaa.bbb")
                    .password(passwordEncoder.encode("1111"))
                    .name("홍길동"+i)
                    .address("양산시"+i)
                    .role(Role.ADMIN)
                    .build();

            memberService.saveMember(member);
        });

        //Member savedMember =  memberService.saveMember(member);

        // assertEquals메소드를 이용하여 저장하려고 요청했던 값과 실제 저장된 데이를 비교
        // assertEquals(기대값, 실제값);
//        assertEquals(member.getEmail(), savedMember.getEmail());
//        assertEquals(member.getName(), savedMember.getName());
//        assertEquals(member.getAddress(), savedMember.getAddress());
//        assertEquals(member.getPassword(), savedMember.getPassword());
//        assertEquals(member.getRole(), savedMember.getRole());

    }


    @Test@DisplayName("중복 회원 테스트-이메일 기준")
    public void saveDuplicateMemberTest() throws  Exception{
        Member memberTest1 = createMember();
        Member memberTest2 = createMember();

        memberService.saveMember(memberTest1);

        Throwable e = assertThrows(IllegalStateException.class, () ->{
            memberService.saveMember(memberTest2);
        });

        assertEquals("이미 가입된 회원입니다.",e.getMessage());
    }

    // ---------------------  //
    // 로그인/로그아웃 테스트
    // ---------------------  //

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws  Exception{
        String email = "test04@gmail.com";
        String password = "1234";

        mockMvc.perform(
                        formLogin().userParameter("email")
                            .loginProcessingUrl("/members/login")
                            .user(email)
                            .password(password)
                        )
                .andExpect(SecurityMockMvcResultMatchers.authenticated());

    }



}
