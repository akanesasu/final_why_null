package org.whynull.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whynull.domain.BoardDTO;
import org.whynull.domain.Criteria;
import org.whynull.domain.WriteDTO;
import org.whynull.domain.PageDTO;
import org.whynull.service.BoardService;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;

    @GetMapping("/free")
    public void list(Criteria cri, Model model) {
        log.info("list // Criteria : " + cri);
        model.addAttribute("free", service.getList(cri));
        int total = service.getTotal(cri);
        model.addAttribute("pageMaker", new PageDTO(cri, total));
    }

    @GetMapping("/write")
    public void write() {
    }

    @PostMapping("/write")
    public String write(BoardDTO dto, RedirectAttributes rttr, Model model) {
        log.info("write : " + dto);
        service.write(dto);
        model.addAttribute("boardNum", dto.getBoard_num());
        rttr.addFlashAttribute("result", dto.getPost_num());
        return "redirect:/board/free";
    }

    @GetMapping("/read")
    public void read(@RequestParam("board_num") Long board_num, @RequestParam("post_num") Long post_num, @ModelAttribute("cri") Criteria cri, Model model) {
        log.info("/read");
        service.viewCount(post_num);
        model.addAttribute("board", service.read(board_num, post_num));
    }

    @GetMapping("/edit")
    public void edit(@RequestParam("board_num") Long board_num, @RequestParam("post_num") Long post_num, @ModelAttribute("cri") Criteria cri, Model model) {
        log.info("/edit");
        model.addAttribute("board", service.read(board_num, post_num));
    }

    @GetMapping(value="/getContentList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WriteDTO>> getContentList(Long board_num, Long post_num) {
        log.info("getContentList : " + post_num);
        return new ResponseEntity<>(service.getContentList(board_num, post_num), HttpStatus.OK);
    }
}