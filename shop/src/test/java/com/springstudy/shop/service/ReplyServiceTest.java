package com.springstudy.shop.service;

import com.springstudy.shop.dto.BoardListRepliesCountDTO;
import com.springstudy.shop.dto.ReplyDTO;
import com.springstudy.shop.entity.Board;
import com.springstudy.shop.entity.Reply;
import com.springstudy.shop.service.ReplyService;
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

import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class ReplyServiceTest {
    @Autowired
    private ReplyService replyService;

    @Test
    @DisplayName("Register테스트")
    public void testRegister(){
        // 게시글 번호: ex) 100번글에 대한 댓글 생성하기
        Long bno = 990L;
        IntStream.rangeClosed(1,5).forEach(i->{
            ReplyDTO replyDTO = ReplyDTO.builder()
                    .replyText("댓글..."+i)
                    .replyer("홍길동"+i)
                    .bno(bno)
                    .build();

            log.info("replyDTO=>"+replyDTO);
            replyService.register(replyDTO);
        });

    }

    @Transactional
    @Test
    @DisplayName("Replies  테스트")
    public void testReplies(){

        Long bno = 100L;

        // 페이징 설정 객체
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
//        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
//
//        result.getContent().forEach(reply -> {
//            log.info(reply);
//        });

    }


}
