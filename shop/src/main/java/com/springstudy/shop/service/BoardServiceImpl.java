package com.springstudy.shop.service;

import com.springstudy.shop.dto.BoardDTO;
import com.springstudy.shop.dto.BoardListRepliesCountDTO;
import com.springstudy.shop.dto.PageRequestDTO;
import com.springstudy.shop.dto.PageResponseDTO;
import com.springstudy.shop.entity.Board;
import com.springstudy.shop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;//dao

    @Override
    public Long register(BoardDTO boardDto) {
        // dto -> entity로 데이터 복사
        Board board = modelMapper.map(boardDto, Board.class);
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        BoardDTO boardDto = modelMapper.map(board, BoardDTO.class);

        return boardDto;
    }

    @Override
    public void modify(BoardDTO boardDto) {

      Optional<Board> result = boardRepository.findById(boardDto.getBno());
      Board board = result.orElseThrow();

      board.change(boardDto.getTitle(), boardDto.getContent());
      boardRepository.save(board);

    }

    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String [] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        // 레포지토리에서 entity데이터 추출
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        // entity -> dto
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListRepliesCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListRepliesCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);


        return PageResponseDTO.<BoardListRepliesCountDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
