package com.springstudy.shop.controller;

import com.springstudy.shop.dto.PageRequestDTO;
import com.springstudy.shop.dto.PageResponseDTO;
import com.springstudy.shop.dto.ReplyDTO;
import com.springstudy.shop.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;

    @ApiOperation(value ="Replies POST", notes="POST방식으로 댓글 등록")// swagger-ui적용시 사용
    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< Map<String, Long>> register(
                @Valid @RequestBody ReplyDTO replyDTO,
                BindingResult bindingResult
                ) throws BindException { //  컨트롤러에서 발생하는 예외처리 대해 JSON형식 응답 메시지를 생성해서 응답

        log.info(replyDTO);

        if (bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        // 서비스 요청(댓글 추가)
        Long rno = replyService.register(replyDTO);

        Map<String, Long> resultMap = Map.of("rno",rno);

        return ResponseEntity.ok( resultMap);
    }

    @ApiOperation(value ="Replies of Board", notes="GET방식으로 특정 게시글에 대한 댓글 목록")// swagger-ui적용시 사용
    @GetMapping(value="/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(
            @PathVariable("bno") long bno,
            PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> responseDTO =
                replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }

    @ApiOperation(value ="Read Reply", notes="GET방식으로 특정 댓글 조회")// swagger-ui적용시 사용
    @GetMapping(value="/{rno}")
    public ReplyDTO getReplyDTO(
            @PathVariable("rno") long rno){

        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }

    @ApiOperation(value ="Delete Reply", notes="DELETE방식으로 특정 댓글 삭제")// swagger-ui적용시 사용
    @DeleteMapping(value="/{rno}")
    public Map<String, Long> remove( @PathVariable("rno") long rno){

        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }

    @ApiOperation(value ="Modify Reply", notes="PUT방식으로 특정 댓글 수정")// swagger-ui적용시 사용
    @PutMapping(value="/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(
            @PathVariable("rno") long rno,
            @RequestBody ReplyDTO replyDTO){

        replyDTO.setRno(rno);
        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", replyDTO.getRno());

        return resultMap;
    }



}

/*

REST방식 댓글 처리
- URL설게와 데이터 포맷 결정
- 컨트롤러의  JSON/XML 처리
- 동작확인
- 자바스크립트를 통한 화면 처리


URL설계

URL(Method)                 기능 설명                                   반환 데이터
/replices(POST)             특정한 게시물의 댓글 추가                      {'rno':11} 생성된 댓글의 번호
/replices/list/:bno(GET)    특정 게시물(bno)의 댓글 목록'?'뒤 페이징처리    PageResponseDTO를 JSON으로 처리
/replices/:rno(PUT)         특정한 번호의 댓글 수정                       {'rno':11} 수정된 댓글
/replies/rno(DELETE)        특정한 번호의 댓글 삭제                       {'rno':11} 삭제된 댓글 번호
/replies/:rno(GET)          특정한 번호의 댓글 조회                       댓글 객체를 JSON으로 변환한 문자열

 */
