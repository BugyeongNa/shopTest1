package com.springstudy.shop.controller;

import com.springstudy.shop.dto.MemberDTO;
import com.springstudy.shop.entity.Member;
import com.springstudy.shop.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Log4j2
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
public class MemberControllerTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;



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

    @Test@DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@email.com";
        String password = "12341234";
        //this.createMember(email, password);

        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email)
                        .password(password))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin", roles="ADMIN")
    public void itemFormTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isOk());// 기대값은 상태코드 200
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 회원접근 테스트")
    @WithMockUser(username = "user", roles="USER")
    public void itemFormNotAdminTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden());// 기대값은 상태코드 403:권한에 설정에 대한 코드
    }



}
