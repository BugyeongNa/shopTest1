package com.springstudy.shop.service;

import com.springstudy.shop.dto.PageRequestDTO;
import com.springstudy.shop.dto.PageResponseDTO;
import com.springstudy.shop.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);
    ReplyDTO read(Long rno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
