package com.springstudy.shop.controller;

import com.springstudy.shop.dto.ReplyDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
public class RestControllerTest {


    @ApiOperation(value ="Replies POST", notes="POST방식으로 댓글 등록")// swagger-ui적용시 사용
    @PostMapping(value="/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> register(
             @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult
    ) throws BindException {//  컨트롤러에서 발생하는 예외처리 대해 JSON형식 응답 메시지를 생성해서 응답

        log.info(replyDTO);

        if (bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        // 서비스 요청(댓글 추가)

        Map<String, Long> resultMap = Map.of("rno",111L);

        return new ResponseEntity( resultMap, HttpStatus.CREATED);

//         형식1
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Game", "Chess");
//        new ResponseEntity<MoveResponseDto>(moveResponseDto, headers, HttpStatus.valueOf(200));

//      형식2
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(moveResponseDto);
    }

}
