package com.springstudy.shop.service;

import com.springstudy.shop.dto.ItemFormDTO;
import com.springstudy.shop.dto.ItemImgDTO;
import com.springstudy.shop.entity.Item;
import com.springstudy.shop.entity.ItemImg;
import com.springstudy.shop.repository.ItemImgRepository;
import com.springstudy.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    // 상품 정보 등록
    public Long saveItem(
                ItemFormDTO itemFormDTO,
                List<MultipartFile> itemImgFileList ) throws Exception{

        // 상품 등록
        Item item = itemFormDTO.createItem();//dto->entity로 전달
        itemRepository.save(item);

        // 이미지등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();

            itemImg.setItem(item);// 상품 이미지 엔티티에 상품엔티티를 맵핑

            // 대표 이미지 여부 설정
            if (i==0){
                itemImg.setRepImgYn("Y");
            }else{
                itemImg.setRepImgYn("N");
            }

            // 상품 이미지 정보 저장: 상품이미지 엔티티 DB 반영 및 파일업로드처리
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();// 상품 엔티티 아이디 반환
    }


    // 상품 정보(기본정보, 이미지) 읽어오기
    // JPA가 더티체킹(변경감지)를 수행하지않도록 설정(성능향상)
    @Transactional(readOnly = true)
    public ItemFormDTO getItemDtl(Long itemId){

        // 1. db-> entity : 특정 상품에 대한 상품이미지 모두 조회
        List<ItemImg> itemImgList =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 2. List안에 entity 값 -> List구조에 dto로 변환
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
        for(ItemImg itemImg: itemImgList){
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);// entity->dto 메서드호출
            itemImgDTOList.add(itemImgDTO);
        }

        // 3. 상품 정보 읽기
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 4. entity -> dto
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        itemFormDTO.setItemImgDTOList(itemImgDTOList);;

        return itemFormDTO;

    }

    // 5. 상품 정보 수정
    public Long updateItem(
            ItemFormDTO itemFormDTO,
            List<MultipartFile> itemImgFileList) throws Exception{

        // 5.1 기존에 상품 정보 호출
        Item item = itemRepository
                .findById(itemFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 5.2 상품 정보 수정폼으로 부터 전달 받은 data -> entity전달
        item.updateItem(itemFormDTO);
        //itemeRpository.save(item); // 생략
        // => 영속성상태=> 엔티티변경시 변경감지기능동작하여 update쿼리실행
        List<Long> itemImgIds = itemFormDTO.getItemImgIds();

        // 5.3 상품 이미지 정보 수정 서비스 호출하여 상품이미지 정보 수정
        for(int i=0; i<itemImgFileList.size(); i++){// 상품 이미지 개수 만큼 반복
            // 수정된 정보를 상품이미지 entity에 반영 => 영속성상태 적용받음
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        // 수정작업 완료된 상품 아이디 반환
        return item.getId();
    }
}
