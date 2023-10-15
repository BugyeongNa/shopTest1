package com.springstudy.shop.repository;

import com.springstudy.shop.entity.Board;
import com.springstudy.shop.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long >, BoardSearch {

}
