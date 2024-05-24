package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.board.Board;
import com.example.jhta_3team_finalproject.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping(value="/table")
public class TableController {

    private BoardService TS;

    @Autowired
    public TableController(BoardService TS) {
        this.TS = TS;
    }

    @RequestMapping(value="/freelist", method= RequestMethod.GET)
    public ModelAndView freeTable(@RequestParam(value="page", defaultValue="1") int page,
                                  ModelAndView mv) {

        int limit = 10; // 한 화면에 출력할 로우 갯수
        int listcount = TS.getListCount(); // 총 리스트 수를 받아온다.

        // 총 페이지 수
        int maxpage = (listcount + limit - 1)/limit;

        // 현재 페이지에 보여줄 시작 페이지 수
        int startpage = ((page-1)/10)*10+1;
        // 현재 페이지에 보여줄 마지막 페이지 수
        int endpage = startpage+10-1;

        if(endpage > maxpage)
            endpage = maxpage;

        List<Board> freeboardlist = TS.getBoardList(page, limit); // 리스트를 받아옴

        mv.setViewName("table/Free_table");
        mv.addObject("page", page);
        mv.addObject("maxpage", maxpage);
        mv.addObject("startpage", startpage);
        mv.addObject("endpage", endpage);
        mv.addObject("listcount", listcount);
        mv.addObject("boardlist", freeboardlist);
        mv.addObject("limit", limit);

        return mv;

    }

    @GetMapping("/t")
    public String test_table() {
        return "/table/basic-table_backup";
    }
}
