package com.springstudy.shop.service;


import com.springstudy.shop.entity.ItemImg;
import com.springstudy.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    // 1. 상품 이미지 정보 등록 서비스
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(
                    itemImgLocation, // 실제 업로드할 파일 위치=>"d:/shop/item"
                    oriImgName, // 파일이름
                    itemImgFile.getBytes());

            // path: "d:/shop" => url:"/images" 와 1:1 연결(맵핑)
            imgUrl ="/images/item/"+imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);


    }

    // 2. 상품 이미지 정보 수정 서비스
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile ) throws Exception {

        if (!itemImgFile.isEmpty()) {// 첨부(상품이미지)파일이 있으면 처리

            // 등록된 상품이미지 정보 호출
            ItemImg savedItemImg = itemImgRepository
                    .findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            // 등록된(기존) 상품이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            // 변경된 상품이미지 업로드 및 상품이미지 entity 정보 변경(DB에는 반영안된 상태)
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(
                    itemImgLocation,
                    oriImgName,
                    itemImgFile.getBytes());

            String imgUrl = "/images/item/item/" + imgName;

            // 수정 폼으로 받은 상품이미지 정보 entity로 전달
            // entity가 변경되면 영속성 상태에서 자동으로 update쿼리 실행

            log.info("==> 상품이미지수정 서비스 => "+oriImgName+","+imgName);
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            log.info("==> 상품이미지 entity변경후 => "+savedItemImg.getOriImgName());

        }
    }

}
