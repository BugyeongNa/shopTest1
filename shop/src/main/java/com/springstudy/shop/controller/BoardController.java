package com.springstudy.shop.controller;

import com.springstudy.shop.dto.BoardDTO;
import com.springstudy.shop.dto.BoardListRepliesCountDTO;
import com.springstudy.shop.dto.PageRequestDTO;
import com.springstudy.shop.dto.PageResponseDTO;
import com.springstudy.shop.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model,RedirectAttributes redirectAttributes){
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);// 댓글수 없는 리스트
        PageResponseDTO<BoardListRepliesCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
        
        log.info(responseDTO);
        //redirectAttributes.addFlashAttribute("result", 101);
        model.addAttribute("responseDTO", responseDTO);
        // return 포워딩 생략시 => url와 동일하게 지정
        // return "/board/list"; 인식됨
    }

    @PreAuthorize("isAuthenticated()") // 로그인 상태인 경우만 처리
    @GetMapping(value={"/read","/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value="/register")
    public void registerGet(){ }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value="/register")
    public String registerPost(
            @Valid BoardDTO boardDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes){

        // Post방식에서 @Valid에서 문제가 발생했을 때 RedirectAtrributes객체을 적용하여 데이터 전송(일회용 정보유지)
        // BindingResult클래스는 @Valid검증결과에 대한 정보
        if (bindingResult.hasErrors()){
            log.info("has errors ...");

            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/board/register";
        }

        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }

    // 현재로그인 사용자와 현재파라미터다 수집된 BoardDTO객체 속성값과 동일여부 판단
    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping(value="/modify")
    public String modify(
            @Valid BoardDTO boardDTO,
            BindingResult bindingResult,
            PageRequestDTO pageRequestDTO,
            RedirectAttributes redirectAttributes){

        // Post방식에서 @Valid에서 문제가 발생했을 때 RedirectAtrributes객체을 적용하여 데이터 전송(일회용 정보유지)
        // BindingResult클래스는 @Valid검증결과에 대한 정보
        if (bindingResult.hasErrors()){
            log.info("has errors ...");

            String link = pageRequestDTO.getLink();
            String detail = bindingResult.getFieldError().getDefaultMessage();

            //redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            redirectAttributes.addFlashAttribute("detail", detail);

            log.info("form dto:"+boardDTO);
            return "redirect:/board/modify?"+link;

        }


        log.info("modify dto => "+boardDTO);
        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modifyed");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping(value="/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes){
        log.info("remove post... "+bno);

        boardService.remove(bno);

        redirectAttributes.addFlashAttribute("result",bno+"글번호 삭제되었습니다.");
        return "redirect:/board/list";

    }


}
