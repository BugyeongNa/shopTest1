package com.springstudy.shop.service;

import com.springstudy.shop.constant.ItemSellStatus;
import com.springstudy.shop.dto.ItemFormDTO;
import com.springstudy.shop.entity.Item;
import com.springstudy.shop.entity.ItemImg;
import com.springstudy.shop.repository.ItemImgRepository;
import com.springstudy.shop.repository.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Log4j2
public class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgRepository itemImgRepository;

    // 첨부파일 5개를 생성하는 메서드
    List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0; i<5;i++){
            String path ="d:/shop/item/";
            String imageName = "image"+i+".jpg";

            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName,"image/jpg", new byte[]{1,2,3,4} );

            multipartFileList.add(multipartFile);
        }// end for

        return multipartFileList;
    }

    @Test@DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles="ADMIN")
    void saveItem() throws Exception{
        ItemFormDTO itemFormDTO = new ItemFormDTO();

        itemFormDTO.setItemNm("테스트상품");
        itemFormDTO.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDTO.setItemDetail("테스트 상품입니다.");
        itemFormDTO.setPrice(1000);
        itemFormDTO.setStockNumber(100);


        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDTO, multipartFileList);

        List<ItemImg> itemImgList =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        Assertions.assertEquals(itemFormDTO.getItemNm(), item.getItemNm());
        Assertions.assertEquals(itemFormDTO.getItemSellStatus(), item.getItemSellStatus());
        Assertions.assertEquals(itemFormDTO.getItemDetail(), item.getItemDetail());
        Assertions.assertEquals(itemFormDTO.getPrice(), item.getPrice());
        Assertions.assertEquals(itemFormDTO.getStockNumber(), item.getStockNumber());
        Assertions.assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());


    }
}
