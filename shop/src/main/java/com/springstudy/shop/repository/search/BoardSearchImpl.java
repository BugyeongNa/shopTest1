package com.springstudy.shop.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.springstudy.shop.dto.BoardListRepliesCountDTO;
import com.springstudy.shop.entity.Board;
import com.springstudy.shop.entity.QBoard;
import com.springstudy.shop.entity.QReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl(){
        super(Board.class);
    }
    @Override
    public Page<Board> search1(Pageable pageable) {

        // 작업대상인 Q도메인, JPQL 설정
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        // 조건식 설정
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(board.title.contains("1")); // title like ...
        booleanBuilder.or(board.content.contains("1"));

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();



        return new PageImpl<>(list, pageable, count);
//        return null;

        /*
        PageImpl을 이용한 Page<T>반환
        - 페이징 처리의 최종 결과는 Page<T>반환
        - Querydsl에서 처리하지않고 JPA에서 처리
        PageImpl(List<T> 데이터, Pagable:페이지 관련정보객체, long:전체 개수
         */
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        // 작업대상인 Q도메인, JPQL 설정
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ((types != null && types.length > 0) && keyword != null){

            // 조건식 설정
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type: types){
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }// end switch
            }// end for
            query.where(booleanBuilder);
        }// end if

        query.where(board.bno.gt(0L));

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListRepliesCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));// 댓글을 작성한 게시글 번호형식으로 연결
        query.groupBy(board);

        if ((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type: types){
                switch(type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }// end switch
            }// end for
            query.where(booleanBuilder);
        }// end if

        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListRepliesCountDTO> dtoQuery =
                query.select(
                        Projections.bean(
                                BoardListRepliesCountDTO.class,
                                board.bno,
                                board.title,
                                board.writer,
                                board.regDate,
                                reply.count().as("replyCount")));

        this.getQuerydsl().applyPagination(pageable,dtoQuery);
        List<BoardListRepliesCountDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
