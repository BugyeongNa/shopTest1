package com.springstudy.shop.junittest;

import com.springstudy.shop.entity.Board;
import com.springstudy.shop.repository.BoardRepository;
import com.springstudy.shop.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;



// @AutoConfigureMockMvc : MockMvc를 생성하고 자동으로 구성하는 어노테이션
// 어플리케이션을 서버에 배포하지 않고 테스트용 MVC환경 제공
//  응답 기능을 제공, Controller, Service, DAO 테스트용을 사용
@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class Test02 {

    // 1.
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


    // 2.
    @BeforeEach
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void cleanUp(){

    }

    @DisplayName("게시글 목록 테스트")
    @Test
    public void getBoardList() throws Exception{

    log.info(
        mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
                .andDo(print())// 수행 내역 검증
                .andExpect(status().isOk()) // response status 200 검증
                .andExpect(model().attributeExists("responseDTO"))// 데이터 검증
    );




    }

    @DisplayName("특정 게시글 조회 테스트")
    @WithMockUser(username = "email100@aaa.bbb", roles = "USER")
    @Test
    public void getBoardBno() throws Exception{
        log.info(

                mockMvc.perform(
                                MockMvcRequestBuilders.get("/board/read")
                                        .param("bno","1002")// 파라미터 검증
                                        .param("page","1")
                                        .param("size","10")
                        )
                        .andDo(print()) // 응답결과 출력
                        .andExpect(status().isOk())// response status 200 검증
                        .andExpect(model().attributeExists("dto"))// 데이터 검증

        );
    }


}
