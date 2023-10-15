package com.springstudy.shop.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="Reply", indexes = {@Index(name="idx_reply_board_bno", columnList = "board_bno")})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
//@ToString
public class Reply  extends BasicEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    // 여러댓글은 1개의 게시글과 연관관계 : n:1
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    private String replyText;
    private String replyer;
    
    // 댓글 내용 수정하는 메서드
    public void changeText(String text){
        this.replyText = text;
    }

}
