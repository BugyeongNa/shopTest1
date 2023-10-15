package com.springstudy.shop.controller;

import com.springstudy.shop.dto.ItemFormDTO;
import com.springstudy.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {

    private final ItemService itemService;

    // 상품 정보 등록 폼 호출
    @GetMapping(value="/admin/item/new")
    public String itemForm(Model model){
        log.info("===> Get /admin/item/new 요청");

        model.addAttribute("itemFormDTO", new ItemFormDTO());
        return "item/itemForm";
    }

    // 상품 정보 DB등록 처리
    @PostMapping(value="/admin/item/new")
    public String itemNew(
            @Valid ItemFormDTO itemFormDTO,
            BindingResult bindingResult,
            Model model,
            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList   //"itemImgFile" 클라이언트로 넘겨받은 매개변수(files객체)
            ){
        log.info("===> Post /admin/item/new 요청");

        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDTO, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    // 상품 조회
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try{
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDTO",itemFormDTO);
            log.info("==> itemformDTO: "+itemFormDTO.getItemImgDTOList());

        }catch (EntityNotFoundException e){

            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDTO", new ItemFormDTO());

            return "item/itemForm";
        }

        return "item/itemForm";
    }

    // 상품 정보 수정
    @PostMapping(value="/admin/item/{itemId}")
    public String itemUpdate(
            @Valid ItemFormDTO itemFormDTO,
            BindingResult bindingResult,
            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
            Model model){

        // 데이터 검증 확인
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        // 첨부파일 여부 체크
        if (itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        // 상품 수정 서비스 호출
        try{
            itemService.updateItem(itemFormDTO, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생했습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }


}