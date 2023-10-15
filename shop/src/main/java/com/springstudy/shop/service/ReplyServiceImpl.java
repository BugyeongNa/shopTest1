package com.springstudy.shop.service;

import com.springstudy.shop.dto.PageRequestDTO;
import com.springstudy.shop.dto.PageResponseDTO;
import com.springstudy.shop.dto.ReplyDTO;
import com.springstudy.shop.entity.Board;
import com.springstudy.shop.entity.Reply;
import com.springstudy.shop.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;


    @Override
    public Long register(ReplyDTO replyDTO) {
        // dto -> entity
        // 연관관계 (Reply entity에 Board Entity n:1관계)
        //Reply reply = modelMapper.map(replyDTO, Reply.class);// 적용안됨.

        // 연관관계 (Reply entity에 Board Entity n:1관계)일 경우
        Board board = Board.builder().bno(replyDTO.getBno()).build();
        Reply reply = Reply.builder()
                .board(board)
                .replyText(replyDTO.getReplyText())
                .replyer(replyDTO.getReplyer())
                .build();

        // entity -> repository
        Long rno = replyRepository.save(reply).getRno();

        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepository.findById(rno);
        Reply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        // 수정할 댓글 번호 조회
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());
        Reply reply = replyOptional.orElseThrow();
        // entity 수정작업
        reply.changeText(replyDTO.getReplyText());
        // entity -> repository
       replyRepository.save(reply).getRno();
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()<=0 ? 0: pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        List<ReplyDTO> dtoList =
                result.getContent().stream()
                        .map(reply-> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
