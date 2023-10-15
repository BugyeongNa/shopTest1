package com.springstudy.shop.repository;

import com.springstudy.shop.constant.ItemSellStatus;
import com.springstudy.shop.entity.Board;
import com.springstudy.shop.entity.Item;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository ;

    @Test
    @DisplayName("board insert테스트")
    public void testInsert(){
        IntStream.rangeClosed(1,1001).forEach(i -> {
            // 객체 생성
            Board board = Board.builder()
                    .title("title..."+i)
                    .content("content..."+i)
                    .writer("user"+i)
                    .build();

            // 영속성 컨텍스트에 반영
            Board result = boardRepository.save(board);
            log.info("BNO: "+result.getBno());
        });

        log.info("테스트");
    }

    @Test
    @DisplayName("board select 테스트")
    public void testSelect(){

        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    @DisplayName("board update 테스트")
    public void testUpdate(){

        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        // update  영속성 컨텍스트에 반영
        board.change("update title 100", "update content 100");
        boardRepository.save(board);
    }

    @Test
    @DisplayName("board delete 테스트")
    public void testDelete(){

        Long bno = 1L;
        boardRepository.deleteById(bno);

    }

    @Test
    @DisplayName("pageable 테스트")
    public void testPage(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: "+result.getTotalElements());
        log.info("total pages: "+result.getTotalPages());
        log.info("page number: "+result.getNumber());
        log.info("page size: "+result.getSize());

        List<Board> boardList = result.getContent();// .getContent() :데이터 가져오는 메서드
        boardList.forEach(board -> log.info(board));


    }

    @Test
    @DisplayName("search기능 테스트")
    public void testSearch(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.search1(pageable);

        result.getContent().forEach(board -> log.info(board));
    }


    @Test
    @DisplayName("searchAll기능 테스트")
    public void testSearchAll(){

        String[] types = {"t","c","w"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+":"+result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }


}
