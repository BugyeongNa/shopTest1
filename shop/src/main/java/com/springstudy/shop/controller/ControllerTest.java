package com.springstudy.shop.controller;

import com.springstudy.shop.dto.ItemDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
public class ControllerTest {

    @GetMapping("/hello")
    public void hello(Model model){
        log.info("/hello 요청중...");

        model.addAttribute("msg","HELLO WORLD!!!");
    }

    // 단일데이터 정보 전달
    @GetMapping(value="/thy/ex01")
    public String thyEx01(Model model){
        model.addAttribute("data", "타임리프 예제01");
        return "thymeleafEx/ex01";
    }
    // 객체 정보 전달
    @GetMapping(value="/thy/ex02")
    public String thyEx02(Model model){

        ItemDTO itemDto = new ItemDTO();

        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/ex02";
    }

    // 객체 정보 전달
    @GetMapping(value="/thy/ex03")
    public String thyEx03(Model model){
        List<ItemDTO> itemDtoList = new ArrayList<>();
        for(int i=1;i<=10; i++){

            ItemDTO itemDto = new ItemDTO();

            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/ex03";
    }

    @GetMapping("/thy/ex04")
    public String thyEx04(Model model){
        log.info("/thy/ex04 요청중...");

        model.addAttribute("msg","HELLO WORLD!!!");
        return "thymeleafEx/ex04";
    }
    @GetMapping("/thy/ex05")
    public String thyEx05(String name, String age, Model model){

        model.addAttribute("name",name);
        model.addAttribute("age",age);
        return "thymeleafEx/ex05";
    }
    @GetMapping("/start")
    public String thyStart(){
        return "index";
    }
    @GetMapping("/start2")
    public String thyStart2(){
        return "index2";
    }


}
