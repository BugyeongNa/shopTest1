package com.springstudy.shop.junittest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class TestEx01 {
    
    // 단위테스트
    @DisplayName("단위테스트연습01") // 테스트이름을 명시
    @Test
    public void Test01(){
        log.info("==> @Test01");
        // 1. given
        int a = 1;
        int b = 2;
        int sum = 3;
        
        // 2. when
        int result = a + b;
        
        // 3. then

        // 첫번째인자: 기대값, 두번째인자: 실제로 검증할 값
        Assertions.assertEquals(sum, result);
        //assertThan(result).isEquealTo(sum);
    }

    @DisplayName("단위테스트연습02") // 테스트이름을 명시
    @Test
    public void Test02(){
        log.info("==> @Test02");
    }
    @DisplayName("단위테스트연습03") // 테스트이름을 명시
    @Test
    public void Test03(){
        log.info("==> @Test03");
    }
    @DisplayName("단위테스트연습04") // 테스트이름을 명시
    @Test
    public void Test04(){
        log.info("==> @Test04");
    }


    @DisplayName("BeforeAll")
    @BeforeAll
    static void beforeAll(){
        // 전체 테스를 시작하기 전에 1회 실행, 데이터베이스 연결 객체 생성할 경우
        log.info("==> @BeforeAll - static");
    }
    @DisplayName("beforeEach")
    @BeforeEach
    public void BeforeEach(){
        // 테스트 케이스를 종료하기 전마다 실행
        log.info("==> @BeforeEach ");
    }

    @DisplayName("AfterEach")
    @AfterEach
    public void AfterEach(){
        // 테스트 케이스를 종료하기 전마다 실행
        log.info("==> @AfterEach ");
    }

    @DisplayName("afterAll")
    @AfterAll
    static void AfterAll(){
        // 전체 테스를 마치고 종료하기 전1회 실행, 데이이베이스 리소스 해제
        log.info("==> @afterAll ");
    }


}

/*
테스트 패턴
given-when-then패턴
1. given -> 준비단계
2. when -> 진행단계
3. then -> 결과검증단계

AssertJ : 검증문이 어설션을 작성하는 데 사용되는 라이브러리
Hamcrest : 표현식을 보다 이해하기 쉽게 만드는 데 사용되는 Mactcher라이브러리
Mockito : 테스트에 사용할 가짜 객체인 목 객체를 쉽게 생성, 관리, 검증할 수 있게 지원하는 테스트 프레임워크
JSONassert: JSON용 어설션 라이브러리
JsonPath: JSOn데이터에서 특정 데이터를 선택하고 검색하기 위한 라이브러리



 */
