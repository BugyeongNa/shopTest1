package com.springstudy.shop.repository;

import com.springstudy.shop.dto.BoardListRepliesCountDTO;
import com.springstudy.shop.entity.Board;
import com.springstudy.shop.entity.Reply;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class ReplyRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository ;

    @Test
    @DisplayName("reply insert테스트")
    public void testInsert(){
        // 게시글 번호: ex) 100번글에 대한 댓글 생성하기
        Long bno = 100L;

        Board board = Board.builder().bno(bno).build();
        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글...")
                .replyer("홍길동1")
                .build();

        replyRepository.save(reply);

//        IntStream.rangeClosed(1,1001).forEach(i -> {
//            // 객체 생성
//            Board board = Board.builder()
//                    .title("title..."+i)
//                    .content("content..."+i)
//                    .writer("user"+i)
//                    .build();
//
//            // 영속성 컨텍스트에 반영
//            Board result = replyRepository.save(board);
//            log.info("BNO: "+result.getBno());
//        });

        log.info("테스트");
    }

    @Transactional
    @Test
    @DisplayName("Replies  테스트")
    public void testReplies(){

        Long bno = 100L;

        // 페이징 설정 객체
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> {
            log.info(reply);
        });

    }

    @Test
    @DisplayName("board update 테스트")
    public void testUpdate(){

        Long bno = 100L;
//        Optional<Board> result = replyRepository.findById(bno);
//        Board board = result.orElseThrow();

        // update  영속성 컨텍스트에 반영
//        board.change("update title 100", "update content 100");
//        replyRepository.save(board);
    }

    @Test
    @DisplayName("board delete 테스트")
    public void testDelete(){

        Long bno = 1L;
//        replyRepository.deleteById(bno);

    }

    @Test
    @DisplayName("pageable 테스트")
    public void testPage(){

//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        Page<Board> result = replyRepository.findAll(pageable);
//
//        log.info("total count: "+result.getTotalElements());
//        log.info("total pages: "+result.getTotalPages());
//        log.info("page number: "+result.getNumber());
//        log.info("page size: "+result.getSize());
//
//        List<Board> boardList = result.getContent();// .getContent() :데이터 가져오는 메서드
//        boardList.forEach(board -> log.info(board));


    }

    @Test
    @DisplayName("searchReplyCount 기능 테스트")
    public void testSearchReplyCount(){
        String[] types = {"t","c","w"};
        String keyword = "100";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<BoardListRepliesCountDTO> result =
                boardRepository.searchWithReplyCount(types, keyword,pageable);
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasNext());

        result.getContent().forEach(board->log.info(board));

    }


    @Test
    @DisplayName("searchAll기능 테스트")
    public void testSearchAll(){
//
//        String[] types = {"t","c","w"};
//        String keyword = "1";
//
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        Page<Board> result = replyRepository.searchAll(types, keyword, pageable);
//
//        log.info(result.getTotalPages());
//        log.info(result.getSize());
//        log.info(result.getNumber());
//        log.info(result.hasPrevious()+":"+result.hasNext());
//
//        result.getContent().forEach(board -> log.info(board));
    }


}
