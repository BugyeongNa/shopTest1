package com.springstudy.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String type; // 검색 타입 t,c,w,tc,tw,twc
    private String keyword;

    
    // 메서드
    public String[] getTypes(){
        if (type==null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }
    // 페이징 구성을 설정(보여질 페이지번호, 페이지당 레코드수, 정렬여부)
    public Pageable getPageable(String...props){
        return PageRequest.of(this.page-1, this.size, Sort.by(props).descending());
    }

    private String link;
    public String getLink(){
        if(link == null){
            StringBuilder builder = new StringBuilder();
            builder.append("page="+this.page);// 기본값 page=1
            builder.append("&size="+this.size);// 기본값 size=10

            // 검색 타입(글제목, 글내용, 글제목+글내용,...)
            if(type!=null && type.length()>0){
                builder.append("&type="+type);
            }
            //  검색 단어(키워드)
            if (keyword!=null && type.length()>0){
                try {
                    builder.append("&keyword="+ URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }

            link=builder.toString();
        }
        return link;// "board/list?page=1&size=10&type=c&keyword=길순이"
    }

}
