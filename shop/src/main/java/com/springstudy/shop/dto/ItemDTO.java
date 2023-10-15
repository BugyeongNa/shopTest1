package com.springstudy.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Getter
@Setter

@ToString
public class ItemDTO {

    private Long id;
    private String itemNm;
    private int price;
    private String itemDetail;
    private String itemSellCd;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

}
