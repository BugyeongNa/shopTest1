package com.springstudy.shop.service;

import com.springstudy.shop.dto.BoardDTO;
import com.springstudy.shop.dto.BoardListRepliesCountDTO;
import com.springstudy.shop.dto.PageRequestDTO;
import com.springstudy.shop.dto.PageResponseDTO;


public interface BoardService {

    public Long register(BoardDTO boardDto);
    public BoardDTO readOne(Long bno);
    public void modify(BoardDTO boardDto);
    public void remove(Long bno);

    // 클라이언트로 부터 요청한 페이지정보 처리하여 응답하는 메서드
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 댓글의 숫자까지 처리
    PageResponseDTO<BoardListRepliesCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

}
