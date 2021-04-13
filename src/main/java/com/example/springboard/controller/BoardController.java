package com.example.springboard.controller;

import com.example.springboard.dto.BoardDto;
import com.example.springboard.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class BoardController {
    private BoardService boardService;

    @GetMapping("/")
    public String list(Model model) { //Model 객체를 통해서 View에 데이터를 전달한다.
        List<BoardDto> boardList = boardService.getBoardList();

        model.addAttribute("boardList", boardList);

        return "board/list.html";
    }

    @GetMapping("/post")
    public String write() {
        return "board/write.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    //게시글 상세조회 페이지를 위한 컨트롤러
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);

        return "board/detail.html";
    }

    //게시글 수정 페이지를 위한 컨트롤러
    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model ) {
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);

        return "board/update.html";
    }

    //게시글 검색을 위한 컨트롤러
    @GetMapping("/board/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/list.html";

    }

    // 문제 해결 : https://victorydntmd.tistory.com/327?category=764331
    //게시글 수정을 위한 컨트롤러
    @PutMapping("/post/edit/{no}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);

        return "redirect:/";
    }

    // 문제 해결 : https://victorydntmd.tistory.com/327?category=764331
    //게시글 삭제를 위한 컨트롤러
    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") long no) {
        boardService.deletePost(no);

        return "redirect:/";
    }
}
