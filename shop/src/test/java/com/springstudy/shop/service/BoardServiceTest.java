package com.springstudy.shop.service;

import com.springstudy.shop.dto.BoardDTO;
import com.springstudy.shop.dto.PageRequestDTO;
import com.springstudy.shop.dto.PageResponseDTO;
import com.springstudy.shop.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

@SpringBootTest
@Log4j2
@TestPropertySource(locations = "classpath:application.properties")
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("Service객체 생성 테스트")
    public void testRegister(){
        //log.info(boardService.getClass().getName());

        BoardDTO boardDto = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .regDate(LocalDateTime.now())
                .build();

        Long bno = boardService.register(boardDto);
        log.info("register test bno: "+bno);

    }

    @Test
    @DisplayName("ReadOne 테스트")
    public void testReadOne(){
        Long bno = 100L;
        log.info("Board read one: "+boardService.readOne(bno));
    }
    @Test
    @DisplayName("modify 테스트")
    public void testModify(){
        BoardDTO boardDto = BoardDTO.builder()
                .bno(50L)
                .title("Update ... 50")
                .content("Update content 50...")
                .build();

        boardService.modify(boardDto);
    }

    @Test
    @DisplayName("delete 테스트")
    public void testDelete(){
        Long bno = 101L;
        boardService.remove(bno);
    }

    @Test
    @DisplayName("페이징 list 테스트")
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
        responseDTO.getDtoList().forEach(dto -> {
            log.info(dto);
        });

    }

}
