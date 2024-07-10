package com.example.jhta_3team_finalproject.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.User.UserAuth;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import com.example.jhta_3team_finalproject.service.S3.S3Service;
import com.example.jhta_3team_finalproject.service.board.AnnounceBoardService;
import com.example.jhta_3team_finalproject.service.User.UserAuthService;
import com.example.jhta_3team_finalproject.util.PagingUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/annboard")
public class AnnounceBoardController {

    private static final Logger logger = LoggerFactory.getLogger(AnnounceBoardController.class);

    private AnnounceBoardService AnnounceBoardService;
    private S3Service S3Service;
    private UserAuthService UserAuthService;
    private SseService sseService;

    @Autowired
    public AnnounceBoardController(AnnounceBoardService AnnounceBoardService, S3Service S3Service,
                                   UserAuthService UserAuthService, SseService sseService) {
        this.AnnounceBoardService = AnnounceBoardService;
        this.S3Service = S3Service;
        this.UserAuthService = UserAuthService;
        this.sseService = sseService;
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

        if (AnnounceBoard.getAnnboardFix() == 1 && AnnounceBoard.getAnnboardImportance() == 1 ) {
            AnnounceBoard.setAnnboardImportance(2);
        }
        // Board 객체를 먼저 저장하고, BOARD_NUM을 받아옵니다.
        AnnounceBoardService.insertBoard(AnnounceBoard); // Board 객체 저장
        int boardNum = AnnounceBoard.getAnnboardNum(); // 저장된 BOARD_NUM 가져오기

        List<BoardUpfiles> files = new ArrayList<>();

        for (MultipartFile uploadfile : uploadfiles) {
            if (!uploadfile.isEmpty()) {

                String fileUrl = S3Service.uploadFile(uploadfile);
                logger.info("Uploaded file URL: " + fileUrl);

                BoardUpfiles file = new BoardUpfiles();
                file.setUpfilesOriginalFileName(uploadfile.getOriginalFilename());
                file.setUpfilesFileName(fileUrl); // S3 URL로 설정
                files.add(file);
            }
        }

        AnnounceBoardService.insertFile(boardNum, files); // 저장메서드 호출
        AnnounceBoardService.autoCheck(AnnounceBoard.getUserNum(), boardNum);
        AnnounceBoard.setBoardWriter(AnnounceBoardService.getWriter(AnnounceBoard.getUserNum()));
        logger.info(AnnounceBoardService.toString()); //selectKey로 정의한 BOARD_NUM 값 확인
        if(AnnounceBoard.getAnnboardImportance() == 3) {
            int[] allUserNum = AnnounceBoardService.getAllUserData(AnnounceBoard.getUserNum(), AnnounceBoard.getAnnboardDepartment());
            logger.info(allUserNum.toString());
            for (int userNum : allUserNum) {
                // 받는 사람 넘버(필수) , 보내는 사람 넘버, 보내는사람 이름(안넣으면 이상하게보임), 링크, 메세지(필수)
                sseService.sendNotification(userNum, AnnounceBoard.getUserNum(), AnnounceBoard.getBoardWriter(),
                        "http://43.203.196.38:9000/annboard/detail?num="+boardNum,
                        "필독 공지를 등록하셨습니다.");

                logger.info("알림전송");
            }
        }
        return "redirect:announceList";

    }


    @GetMapping("/detail")
    public ModelAndView Detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            @RequestHeader(value = "referer", required = false) String beforeURL,
            @RequestParam(value = "notidata", defaultValue = "0") int notidata
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
            // 조건에 따라 메시지를 추가
            if (notidata == 1 && (AnnounceBoard.getAnnboardRequest() == 2)) {
                logger.info("요청이미 처리됨");
                mv.addObject("requestMessage", "이미 요청이 처리된 게시물입니다");
            }
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
        String targetDepartment = "";
        int maxCheck;
        int checkedUserByDepartment;

        int addcheck=AnnounceBoardService.addCheck(loginNum, annboardNum);
        targetDepartment = AnnounceBoardService.targetDepartment(annboardNum);
        AnnounceBoard AnnounceBoard = AnnounceBoardService.getDetail(annboardNum);
        int writer = AnnounceBoard.getUserNum();
        logger.info(targetDepartment + "관련 글입니다.");
        maxCheck = AnnounceBoardService.getMaxCheck(targetDepartment);
        logger.info(targetDepartment+" 사원의 수는 " + maxCheck );
        checkedUserByDepartment = AnnounceBoardService.checkedUserByDepartment(targetDepartment, annboardNum);
        logger.info("확인한 " +targetDepartment+" 사원의 수는 " + checkedUserByDepartment );
        int imsi;
        if(maxCheck == checkedUserByDepartment) {
            int boardImportance = AnnounceBoardService.downImportance(annboardNum);
            if(boardImportance == 1) {
                logger.info("중요도 lev이 낮아졌습니다. 상단 고정이 해제됩니다.");
                sseService.sendNotification(writer,0 , "", "http://43.203.196.38:9000/annboard/detail?num=" + annboardNum, "모든" + targetDepartment + "의 사원들이 No." + annboardNum + "공지를 확인했습니다.");
            }
        } else {
            logger.info("아직 공지를 확인하지 못한 사원이 존재합니다.");
        }

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

        List<User> management;                      // 관리
        Map<Integer, List<User>> checkedMG;         // 확인한 관리부서 사원 정보

        List<User> relations;                       // 홍보
        Map<Integer, List<User>> checkedRT;         // 확인한 홍보부서 사원 정보

        List<User> support;                         // 지원
        Map<Integer, List<User>> checkedSP;         // 확인한 지원부서 사원 정보

        List<User> sales;           // 영업
        Map<Integer, List<User>> checkedSL;         // 확인한 지원부서 사원 정보

        List<User> executive;                       // 임원
        Map<Integer, List<User>> checkedEX;         // 확인한 지원부서 사원 정보

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

    @PostMapping("/topfix")
    @ResponseBody
    public Map<String, Object> topFix(@RequestParam("loginNum") int loginNum,
                                           @RequestParam("annboardNum") int annboardNum,@AuthenticationPrincipal User user
                                        ) {

        Map<String, Object> response = new HashMap<>();
        AnnounceBoard AnnounceBoard = AnnounceBoardService.getDetail(annboardNum);
        String result = "권한이 없습니다.";
        int update = 0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuth userInfo = UserAuthService.getUserInfo(authentication);
        User loginuser = (User)authentication.getPrincipal();

        // 1. 권한 확인
        boolean hasRole = userInfo.getAuthorities().stream()
                .anyMatch(role -> role.equals("ROLE_SUB_MASTER"));
        // SUBMASTER 이상이거나 관리부 부장
        if(hasRole || (loginuser.getDepartmentId()== 2 && loginuser.getUserAuth().equals("ROLE_HEAD"))) {
            update = AnnounceBoardService.doTopFix(annboardNum);
            if(update==1)
                result = "상단고정 되었습니다.";
                if(AnnounceBoard.getUserNum() != loginNum) {
                    sseService.sendNotification(AnnounceBoard.getUserNum(), loginNum, loginuser.getUsername(),
                            "http://43.203.196.38:9000/annboard/detail?num=" + annboardNum,
                            "No." + annboardNum + "글을 상단고정하셨습니다.");
                }
        }else {
            // 아니면 권한이 있는 사용자에게 상단 고정을 요청한다.

            // 권한을 가진 사용자 찾기 ( SUB_MASTER, 관리부 HEAD)
            int[] FixAuthUser = AnnounceBoardService.searchFixAuth();
            logger.info(FixAuthUser + "에게 알림 전송준비중");
            for (int recipient : FixAuthUser) {
                                            // 받는 사람 넘버(필수) , 보내는 사람 넘버, 보내는사람 이름(안넣으면 이상하게보임), 링크, 메세지(필수)
                sseService.sendNotification(recipient, loginNum, loginuser.getUsername(),
                                        "http://43.203.196.38:9000/annboard/detail?num="+annboardNum+"&notidata=1",
                                        "공지게시판 No." +annboardNum+"글의 상단고정을 요청했습니다.");
                int request = AnnounceBoardService.fixRequest(annboardNum);
                logger.info("알림전송");

                result="상단 고정을 요청합니다.";
            }
        }


        response.put("status", "success");
        response.put("update", update);
        response.put("result", result);

        return response;
    }

    @PostMapping("/topfixclear")
    @ResponseBody
    public Map<String, Object> topFixclear(@RequestParam("loginNum") int loginNum,
                                      @RequestParam("annboardNum") int annboardNum) {

        Map<String, Object> response = new HashMap<>();
        AnnounceBoard AnnounceBoard = AnnounceBoardService.getDetail(annboardNum);
        String result = "권한이 없습니다.";
        int update = 0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuth userInfo = UserAuthService.getUserInfo(authentication);
        User loginuser = (User)authentication.getPrincipal();

        // 1. 권한 확인
        boolean hasRole = userInfo.getAuthorities().stream()
                .anyMatch(role -> role.equals("ROLE_SUB_MASTER"));
        logger.info("!");
        // SUBMASTER 이상이거나 관리부 부장
        if(hasRole || (loginuser.getDepartmentId()== 2 && loginuser.getUserAuth().equals("ROLE_HEAD"))) {
            update = AnnounceBoardService.TopFixclear(annboardNum);
            if(update==1)
                result = "상단고정 해제되었습니다.";
        }

        response.put("status", "success");
        response.put("update", update);
        response.put("result", result);

        return response;
    }

    @PostMapping("/topfixRefuse")
    @ResponseBody
    public Map<String, Object> topFixRefuse(@RequestParam("loginNum") int loginNum,
                                      @RequestParam("annboardNum") int annboardNum
    ) {

        Map<String, Object> response = new HashMap<>();
        int result = AnnounceBoardService.requestRefuse(annboardNum);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginuser = (User)authentication.getPrincipal();

        AnnounceBoard AnnounceBoard = AnnounceBoardService.getDetail(annboardNum);
        int writer = AnnounceBoard.getUserNum();
        sseService.sendNotification(writer, loginNum, loginuser.getUsername(), "", "공지게시판 No." + annboardNum + "글의 상단고정 요청을 거절했습니다.");
        sseService.deleteNotificationUrl("http://43.203.196.38:9000/annboard/detail?num="+annboardNum);
        response.put("status", "success");
        response.put("result", result);

        return response;
    }

    @ResponseBody
    @PostMapping("/down")
    public void BoardFileDown(String filename,
                              HttpServletRequest request,
                              String original,
                              HttpServletResponse response) throws IOException {

        String s3Key = filename.replace("https://mbtiawsbucket.s3.ap-northeast-2.amazonaws.com/", "");
        logger.info("S3 Key: {}", s3Key);

        try {
            S3Object s3Object = S3Service.downloadFile(s3Key);
            InputStream inputStream = s3Object.getObjectContent();

            String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");

            response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding);
            response.setContentLength((int) s3Object.getObjectMetadata().getContentLength());

            FileCopyUtils.copy(inputStream, response.getOutputStream());

            inputStream.close();
            response.getOutputStream().close();
            logger.info("File successfully written to response output stream.");
        } catch (AmazonS3Exception e) {
            logger.error("Error occurred while downloading file from S3", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The specified key does not exist.");
        }
    }
}
