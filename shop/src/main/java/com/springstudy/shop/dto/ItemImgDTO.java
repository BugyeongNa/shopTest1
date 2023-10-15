package com.springstudy.shop.dto;

import com.springstudy.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ItemImgDTO {

    private Long id;

    private String imgName;// 이미지 파일명
    private String oriImgName;// 원본 이미지 파일명
    private String imgUrl;// 이미지 조회 경로
    private String repImgYn; // 대표 이미지 여부

    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemImgDTO of(ItemImg itemImg){
        // entity -> dto
        return modelMapper.map(itemImg, ItemImgDTO.class);
    }
}
