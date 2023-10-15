package com.springstudy.shop.controller;

import com.springstudy.shop.dto.MemberDTO;
import com.springstudy.shop.entity.Member;
import com.springstudy.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value="/register")
    public String memberForm(Model model){
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/registerMember";
    }


    @PostMapping(value="/register")
    public String saveMember(
            @Valid MemberDTO memberDTO,
            BindingResult bindingResult,
            Model model){

        log.info("--- member post");

        if (bindingResult.hasErrors()){
            return "member/registerMember";
        }

        try {
            Member member = Member.createMember(memberDTO, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/registerMember";
        }


        return "redirect:/";
    }

    @GetMapping(value="/login")
    public String loginGET(String error, String logout){
        log.info("---- login get ----");
        log.info("/login error => "+error);
        log.info("logout => "+logout);

//        if (logout != null){
//            log.info("user logout....");
//        }

        return "member/login";
    }

    @GetMapping(value="/login/error")
    public String loginError(Model model){
        log.info("---- login/error ----");

        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }


}
