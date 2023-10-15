package com.springstudy.shop.dto;

import com.springstudy.shop.constant.ItemSellStatus;
import com.springstudy.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {
    // 클라이언트로 부터 넘어온 데이터 서버에서 데이터 검증

    private Long id;
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int price;
    @NotBlank(message = "상품 상세 내용은 필수 입력 값입니다.")
    private String itemDetail;
    @NotNull(message = "재고수량은 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;
    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    // 상품의 이미지 아이디를 저장하는 리스트
    private List<Long> itemImgIds = new ArrayList<>();



    // entity -> DTO
    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemFormDTO of (Item item){
        return modelMapper.map(item, ItemFormDTO.class);
    }
    // DTO -> entity
    public  Item createItem(){
        return modelMapper.map(this, Item.class);
    }

}
