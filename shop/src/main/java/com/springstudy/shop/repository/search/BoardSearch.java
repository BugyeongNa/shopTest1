package com.springstudy.shop.repository.search;

import com.springstudy.shop.dto.BoardListRepliesCountDTO;
import com.springstudy.shop.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
    Page<BoardListRepliesCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable );

}
