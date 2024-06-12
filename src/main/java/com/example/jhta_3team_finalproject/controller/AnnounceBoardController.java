package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.S3CommomService;
import com.example.jhta_3team_finalproject.service.board.AnnounceBoardService;
import com.example.jhta_3team_finalproject.util.PagingUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/annboard")
public class AnnounceBoardController {

    private static final Logger logger = LoggerFactory.getLogger(AnnounceBoardController.class);

    @Value("${my.savefolder.board}")
    private String saveFolder;
    private AnnounceBoardService AnnounceBoardService;
    private S3CommomService s3CommomService;

    @Autowired
    public AnnounceBoardController(AnnounceBoardService AnnounceBoardService, S3CommomService s3CommomService) {
        this.AnnounceBoardService = AnnounceBoardService;
        this.s3CommomService = s3CommomService;
    }

    // 리스트 가져오기
    @RequestMapping(value = "/announceList", method = RequestMethod.GET)
    public ModelAndView freeTable(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "searchField", defaultValue = "-1") int index,
                                  @RequestParam(value = "search", defaultValue = "") String searchWord,
                                  @RequestParam(value = "targetDepartment", defaultValue = "") String targetDepartment,
                                  ModelAndView mv) {



        int limit = 10; // 한 화면에 출력할 로우 갯수
        int listcount = AnnounceBoardService.getListCount(index, searchWord, targetDepartment); // 총 리스트 수를 받아온다.
        logger.info("listcount:" + listcount);
        // 총 페이지 수
        PagingUtil.Paging pagingUtil = new PagingUtil.Paging(page, limit, listcount);

        List<AnnounceBoard> boardlist = AnnounceBoardService.getBoardList(index, searchWord, targetDepartment, page, limit); // 리스트를 받아옴

        mv.setViewName("table/announce/announceBoard");
        mv.addObject("page", page);
        mv.addObject("maxpage", pagingUtil.getMaxpage());
        mv.addObject("startpage", pagingUtil.getStartpage());
        mv.addObject("endpage", pagingUtil.getEndpage());
        mv.addObject("rownum", pagingUtil.getRowNum());
        mv.addObject("listcount", listcount);
        mv.addObject("boardlist", boardlist);
        mv.addObject("limit", limit);
        mv.addObject("search_field", index);
        mv.addObject("search_word", searchWord);
        mv.addObject("targetDepartment", targetDepartment);

        logger.info("StartPage="+ String.valueOf(pagingUtil.getStartpage()));
        return mv;

    }

    // 글쓰기 폼 불러오기
    @GetMapping("/announceWrite")
    public String wirteForm() {
        return "table/announce/announceWrite";
    }

    // 작성 글 DB 등록
    @PostMapping("/add")
    public String add(AnnounceBoard AnnounceBoard, HttpServletRequest request,
                      @RequestParam("uploadfile[]") MultipartFile[] uploadfiles)
            throws Exception {

        AnnounceBoard.getAnnboardImportance();
        // Board 객체를 먼저 저장하고, BOARD_NUM을 받아옵니다.
        AnnounceBoardService.insertBoard(AnnounceBoard); // Board 객체 저장
        int boardNum = AnnounceBoard.getAnnboardNum(); // 저장된 BOARD_NUM 가져오기

        List<BoardUpfiles> files = new ArrayList<>();

        for (MultipartFile uploadfile : uploadfiles) {
            if (!uploadfile.isEmpty()) {

                String fileUrl = s3CommomService.uploadFile(uploadfile);
                logger.info("Uploaded file URL: " + fileUrl);

                BoardUpfiles file = new BoardUpfiles();
                file.setUpfilesOriginalFileName(uploadfile.getOriginalFilename());
                file.setUpfilesFileName(fileUrl); // S3 URL로 설정
                files.add(file);
            }
        }

        AnnounceBoardService.insertFile(boardNum, files); // 저장메서드 호출
        AnnounceBoardService.autoCheck(AnnounceBoard.getUserNum(), boardNum);
        logger.info(AnnounceBoardService.toString()); //selectKey로 정의한 BOARD_NUM 값 확인
        return "redirect:announceList";

    }


    @GetMapping("/detail")
    public ModelAndView Detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            @RequestHeader(value = "referer", required = false) String beforeURL
            ) {
		/*
			1. String BeforeURL = request.getHeader("referer"); 의미로
			   어느 주소에서 Detail로 이동했는지 header의 정보 중 "referer"을 통해 알 수 있다.
			2. 수정 후 이곳으로 이동하는 경우 조회수는 증가하지 않도록 한다.
			3. myhome4/board/list에서 제목을 클릭한 경우 조회수가 증가하도록 한다.
		 */

        logger.info("referer : " + beforeURL);
        if (beforeURL != null && beforeURL.endsWith("announceList")) {
            AnnounceBoardService.setReadCountUpdate(num);
        }

        AnnounceBoard AnnounceBoard = AnnounceBoardService.getDetail(num);
        List<BoardUpfiles> upfiles = AnnounceBoardService.getFilesByBoardNum(num);
        // board = null; // error페이지 이동 확인하고자 임의로 지정.
        if (AnnounceBoard == null) {
            logger.info("상세보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "실패하였습니다.");
        } else {
            logger.info("상세보기 성공");

            mv.setViewName("table/announce/announceView");

            mv.addObject("boarddata", AnnounceBoard);
            mv.addObject("upfiles", upfiles);

        }
        return mv;
    }

    @PostMapping("/delete")
    public String BoardDeleteAction(int bnum,
                                    Model mv, RedirectAttributes rattr,
                                    HttpServletRequest request) {

        int result = AnnounceBoardService.boardDelete(bnum);

        // 삭제 처리 실패한 경우
        if(result == 0) {
            logger.info("삭제 실패!");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "삭제 실패");
            return "error/error";
        } else {
            // 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분
            logger.info("삭제 완료!");
            rattr.addFlashAttribute("result", "deleteSuccess");
            return "redirect:announceList";
        }
    }

    @PostMapping("/ViewAction")
    @ResponseBody
    public Map<String, Object> viewAction(@RequestParam("loginNum") int loginNum,
                                          @RequestParam("annboardNum") int annboardNum) {

        Map<String, Object> response = new HashMap<>();

        int beforecheck=AnnounceBoardService.Exist(loginNum, annboardNum);
        logger.info(String.valueOf(beforecheck));
        String result = "본적 있는 글입니다.";
        if(beforecheck != 1) {
            result = AnnounceBoardService.viewChecking(loginNum, annboardNum);
            logger.info(result);
            result="뷰 기록 완료";
        }

        int OX = AnnounceBoardService.checkedcheck(loginNum, annboardNum);
        logger.info("OX : " + OX);
        response.put("status", "success");
        response.put("result", result);
        response.put("OX", OX);


        return response;
    }

    @PostMapping("/ViewCheck")
    @ResponseBody
    public Map<String, Object> viewCheck(@RequestParam("loginNum") int loginNum,
                                          @RequestParam("annboardNum") int annboardNum) {

        Map<String, Object> response = new HashMap<>();

        int Checking=AnnounceBoardService.Exist(loginNum, annboardNum);
        logger.info("뷰 기록 확인");
        response.put("status", "success");
        response.put("OX", Checking);

        return response;
    }

    @PostMapping("/checkAction")
    @ResponseBody
    public Map<String, Object> checkAction(@RequestParam("loginNum") int loginNum,
                                          @RequestParam("annboardNum") int annboardNum) {

        Map<String, Object> response = new HashMap<>();
        String result = "확인";
        int addcheck=AnnounceBoardService.addCheck(loginNum, annboardNum);

        if(addcheck != 1) {
            result = "체킹 실패";
        }
        logger.info(result);
        response.put("status", "success");
        response.put("result", result);

        return response;
    }

    @PostMapping("/checkDelete")
    @ResponseBody
    public Map<String, Object> checkDelete(@RequestParam("loginNum") int loginNum,
                                           @RequestParam("annboardNum") int annboardNum) {

        Map<String, Object> response = new HashMap<>();
        String result = "확인 취소";
        int addcheck=AnnounceBoardService.deleteCheck(loginNum, annboardNum);

        if(addcheck != 1) {
            result = "체킹 실패";
        }
        logger.info(result);
        response.put("status", "success");
        response.put("result", result);

        return response;
    }

    @PostMapping("/getUsersdata")
    @ResponseBody
    public Map<String, Object> getUsersData(@RequestParam("loginNum") int loginNum,
                                           @RequestParam("annboardNum") int annboardNum) {

        logger.info("userdata run");
        Map<String, Object> response = new HashMap<>();

        // 회원의 모든 정보
        List<User> user;                            // 모든 회원의 정보
        Map<Integer, List<User>> departGroupUser;   // 부서별 회원 정보
        int totalUser;              // 전체 회원 수
        int totalDepartment;        // 부서의 수

        List<User> humanResource;                   // 인사부서 사원 정보
        Map<Integer, List<User>> checkedHR;         // 확인한 인사부서 사원 정보
        int totalHR;                                // 인사부서 인원 수
        int totalCheckHR;                           // 확인한 인사부서 사원 수

        List<User> management;                      // 관리
        Map<Integer, List<User>> checkedMG;         // 확인한 관리부서 사원 정보
        int totalMG;                                // 관리부서 인원 수
        int totalCheckMG;                           // 확인한 관리부서 사원 수

        List<User> relations;                       // 홍보
        Map<Integer, List<User>> checkedRT;         // 확인한 홍보부서 사원 정보
        int totalRT;                                // 홍보부서 인원 수
        int totalCheckRT;                           // 확인한 홍보부서 사원 수

        List<User> support;                         // 지원
        Map<Integer, List<User>> checkedSP;         // 확인한 지원부서 사원 정보
        int totalSP;                                // 지원부서 인원 수
        int totalCheckSP;                           // 확인한 지원부서 사원 수

        List<User> sales;           // 영업
        Map<Integer, List<User>> checkedSL;         // 확인한 지원부서 사원 정보
        int totalSL;                                // 지원부서 인원 수
        int totalCheckSL;                           // 확인한 지원부서 사원 수

        List<User> executive;                       // 임원
        Map<Integer, List<User>> checkedEX;         // 확인한 지원부서 사원 정보
        int totalEX;                                // 지원부서 인원 수
        int totalCheckEX;                           // 확인한 지원부서 사원 수

        List<String> department = AnnounceBoardService.getDepartment();
        user = AnnounceBoardService.getUserData(annboardNum);
        totalUser = user.size();

        departGroupUser = user.stream().collect(Collectors.groupingBy(User::getDepartmentId));
        totalDepartment = department.size();

        humanResource = departGroupUser.getOrDefault(1, user);
        checkedHR = humanResource.stream().collect(Collectors.groupingBy(User::getViewCheck));

        management = departGroupUser.getOrDefault(2, user);
        checkedMG = management.stream().collect(Collectors.groupingBy(User::getViewCheck));

        relations = departGroupUser.getOrDefault(3, user);
        checkedRT = relations.stream().collect(Collectors.groupingBy(User::getViewCheck));

        support = departGroupUser.getOrDefault(4, user);
        checkedSP = support.stream().collect(Collectors.groupingBy(User::getViewCheck));

        sales = departGroupUser.getOrDefault(5, user);
        checkedSL = sales.stream().collect(Collectors.groupingBy(User::getViewCheck));

        executive = departGroupUser.getOrDefault(6, user);
        checkedEX = executive.stream().collect(Collectors.groupingBy(User::getViewCheck));


        response.put("status", "success");
        response.put("user", user);
        response.put("department", department);
        response.put("departGroupUser", departGroupUser);
        response.put("totalUser", totalUser);
        response.put("totalDepartment", totalDepartment);
        response.put("humanResource", humanResource);
        response.put("management", management);
        response.put("relations", relations);
        response.put("supportDepartment", support);
        response.put("sales", sales);
        response.put("executive", executive);
        response.put("checkedHR", checkedHR);
        response.put("checkedMG", checkedMG);
        response.put("checkedRT", checkedRT);
        response.put("checkedSP", checkedSP);
        response.put("checkedSL", checkedSL);
        response.put("checkedEX", checkedEX);




        return response;
    }
}
