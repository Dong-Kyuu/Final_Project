package com.example.jhta_3team_finalproject.controller;


import com.example.jhta_3team_finalproject.domain.inquery.InqueryBoard;
import com.example.jhta_3team_finalproject.service.customer.InqCommentService;
import com.example.jhta_3team_finalproject.service.customer.InqueryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.*;

@Controller
@RequestMapping(value = "/inquery")
public class InqueryController {
    @Value("${my.savefolder}")
    private String saveFolder;

    private static final Logger log = LoggerFactory.getLogger(InqueryController.class);

    private InqueryService inqueryService;

    private InqCommentService inqCommentService;

    @Autowired
    public InqueryController(InqueryService inqueryService, InqCommentService inqCommentService) {
        this.inqueryService = inqueryService;
        this.inqCommentService = inqCommentService;
    }

    @RequestMapping(value = "/list")
    public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1") int page, ModelAndView mv) {

        log.info("1대1문의 리스트");

        int limit = 10; // 한 화면에 출력할 로우 갯수

        int listcount = inqueryService.getListCount(); // 총 리스트 수를 받아옴

        // 총 페이지 수
        int maxpage = (listcount + limit - 1) / limit;

        // 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등 ...)
        int startpage = ((page - 1) / 10) * 10 + 1;

        // 현재 페이지에 보여줄 마지막 페이지 수 (10, 20, 30 등 ...)
        int endpage = startpage + 10 - 1;

        if (endpage > maxpage)
            endpage = maxpage;

        long beforeTime = System.currentTimeMillis();
        List<InqueryBoard> boardlist = inqueryService.getBoardList(page, limit); // 리스트를 받아옴
        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        log.info("실행 시간(ms): " + diffTime);

        mv.setViewName("customer/inqueryList");
        mv.addObject("page", page);
        mv.addObject("maxpage", maxpage);
        mv.addObject("startpage", startpage);
        mv.addObject("endpage", endpage);
        mv.addObject("listcount", listcount);
        mv.addObject("boardlist", boardlist);
        mv.addObject("limit", limit);

        return mv;
    }

    // 글쓰기
    @RequestMapping(value = "/write") // /board/write
    // @RequestMapping(value="/write", method=RequestMethod.GET)
    public String board_write() {
        return "customer/inqueryWrite";
    }

    /*
     * 스프링 컨테이너는 매개변수 Board 객체를 생성하고 Board 객체의 setter 메서드들을 호출하여 사용자 입력값을 설정합니다.
     * 매개변수의 이름과 setter의 property가 일치하면 됩니다.
     */
    @PostMapping("/add")
    //@RequestMapping(value="/add",method=RequestMethod.POST)
    public String add(InqueryBoard inqueryBoard, HttpServletRequest request)
            throws Exception {

        MultipartFile uploadfile = inqueryBoard.getUploadfile();

        if (!uploadfile.isEmpty()) {
            String fileName = uploadfile.getOriginalFilename();//원래 파일명
            inqueryBoard.setInqOriginal(fileName);// 원래 파일명 저장
            //String saveFolder =	request.getSession().getServletContext().getRealPath("resources")
            //		+ "/upload";
            String fileDBName = fileDBName(fileName, saveFolder);
            log.info("fileDBName = " + fileDBName);

            // transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
            uploadfile.transferTo(new File(saveFolder + fileDBName));
            log.info("transferTo path = " + saveFolder + fileDBName);
            // 바뀐 파일명으로 저장
            inqueryBoard.setInqFile(fileDBName);
        }

        inqueryService.insertBoard(inqueryBoard); // 저장메서드 호출
        log.info(inqueryBoard.toString());//selectKey로 정의한 BOARD_NUM 값 확인해 봅니다.
        return "redirect:list";
    }

    private String fileDBName(String fileName, String saveFolder) {
        // 새로운 폴더 이름 : 오늘 년+월+일
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
        int month = c.get(Calendar.MONTH) + 1; // 오늘 월 구합니다.
        int date = c.get(Calendar.DATE); // 오늘 일 구합니다.

        String homedir = saveFolder + "/" + year + "-" + month + "-" + date;
        log.info(homedir);
        File path1 = new File(homedir);
        if (!(path1.exists())) {
            path1.mkdirs(); // 새로운 폴더를 생성
        }

        // 난수를 구합니다.
        Random r = new Random();
        int random = r.nextInt(100000000);

        /**** 확장자 구하기 시작 ****/
        int index = fileName.lastIndexOf(".");
        // 문자열에서 특정 문자열의 위치 값(index)를 반환합니다.
        // indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
        // lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
        // (파일명에 점에 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)
        log.info("index = " + index);

        String fileExtension = fileName.substring(index + 1);
        log.info("fileExtension = " + fileExtension);
        /**** 확장자 구하기 끝 ****/

        // 새로운 파일명
        String refileName = "bbs" + year + month + date + random + "." + fileExtension;
        log.info("refileName = " + refileName);

        // 오라클 디비에 저장될 파일 명
        //String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
        String fileDBName = File.separator + year + "-" + month + "-" + date
                + File.separator + refileName;
        log.info("fileDBName" + fileDBName);
        return fileDBName;
    }

    @ResponseBody // 각 메서드의 실행 결과는 JSON으로 변환되어 HTTP Response Body에 설정됩니다.
    @RequestMapping(value = "/list_ajax")
    public Map<String, Object> boardListAjax(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        int listcount = inqueryService.getListCount(); // 총 리스트 수를 받아옴

        // 총 페이지 수
        int maxpage = (listcount + limit - 1) / limit;

        // 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등 ...)
        int startpage = ((page - 1) / 10) * 10 + 1;

        // 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등...)
        int endpage = startpage + 10 - 1;

        if (endpage > maxpage)
            endpage = maxpage;

        List<InqueryBoard> boardlist = inqueryService.getBoardList(page, limit); // 리스트를 받아옴

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("maxpage", maxpage);
        map.put("startpage", startpage);
        map.put("endpage", endpage);
        map.put("listcount", listcount);
        map.put("boardlist", boardlist);
        map.put("limit", limit);
        return map;
    }

    @GetMapping("/detail")
    public ModelAndView detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            @RequestHeader(value = "referer", required = false) String beforeURL) {
        /*
         * 1. String beforeURL = request.getHeader("referer"); 의미로
         * 	  어느 주소에서 detail로 이동했는지 header의 정보 중에서 "referer"를 통해 알 수 있습니다.
         * 2. 수정 후 이곳으로 이동하는 경우 조회수는 증가하지 않도록 합니다.
         * 3. myhome4/board/list에서 제목을 클릭한 경우 조회수가 증가하도록 합니다.
         * */
        log.info("referer:" + beforeURL);
        if (beforeURL != null && beforeURL.endsWith("list")) {
            inqueryService.setReadCountUpdate(num);
        }

        InqueryBoard inqueryBoard = inqueryService.getDetail(num);
        // board = null; // error 페이지 이동 확인하고자 임의로 지정합니다.
        if (inqueryBoard == null) {
            log.info("상세보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "상세보기 실패입니다.");
        } else {
            log.info("상세보기 성공");
            int count = inqCommentService.getListCount(num);
            mv.setViewName("customer/inqueryView");
            mv.addObject("count", count);
            mv.addObject("boarddata", inqueryBoard);
        }
        return mv;
    }

    @GetMapping("/modifyView")
    public ModelAndView modifyView(int num, ModelAndView mv, HttpServletRequest request) {
        InqueryBoard boarddata = inqueryService.getDetail(num);

        // 글 내용 불러오기 실패한 경우입니다.
        if (boarddata == null) {
            log.info("수정보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "수정보기 실패입니다.");
        } else {
            log.info("(수정)상세보기 성공");
            // 수정 폼 페이지로 이동할 때 원문 글 내용을 보여주기 때문에 boarddata 객체를
            // ModelAndView 객체에 저장합니다.
            mv.addObject("boarddata", boarddata);
            // 글 수정 폼 페이지로 이동하기 위해 경로를 설정합니다.
            mv.setViewName("customer/inqueryModify");
        }
        return mv;
    }

    @PostMapping("/modifyAction")
    public String BoardModifyAction(
            InqueryBoard boarddata,
            String check, Model mv,
            HttpServletRequest request,
            RedirectAttributes rattr
    ) throws Exception {

        boolean usercheck = false;
        if (boarddata.getInqPass() == null || boarddata.getInqPass().equals("")) {
            // 2024-04-04 비밀번호가 없는 1대1 문의 게시물인 경우 DAO에서 PASS가 null 인지 판단합니다.
            usercheck = inqueryService.isBoardPassNull(boarddata.getInqNum());
        } else {
            // 글쓴이 인지 확인하기 위해 저장된 비밀번호와 입력한 비밀번호를 비교합니다.
            usercheck = inqueryService.isBoardWriter(boarddata.getInqNum(), boarddata.getInqPass());
        }
        String url = "";

        // 비밀번호가 다른 경우
        if (usercheck == false) {
            log.info("패스 인증 실패");
            rattr.addFlashAttribute("result", "passFail");
            rattr.addAttribute("num", boarddata.getInqNum());
            return "redirect:modifyView";
        }

        MultipartFile uploadfile = boarddata.getUploadfile();
        //String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload";

        if (check != null && !check.equals("")) { // 기존 파일 그대로 사용하는 경우입니다.
            log.info("기존 파일 그대로 사용합니다.");
            boarddata.setInqOriginal(check);
            // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
            // 위 문장 때문에 board.setBOARD_FILE() 값은 자동 저장됩니다.
        } else {
            // 답변글의 경우 파일 첨부에 대한 기능이 없습니다.
            // 만약 답변글을 수정할 경우
            // <input type="file" id="upfile" name="uploadfile"> 엘리먼트가 존재하지 않아
            // private MultipartFile uploadfile; 에서 uploadfile은 null 입니다.
            if (uploadfile != null && !uploadfile.isEmpty()) {
                log.info("파일 변경되었습니다.");

                String fileName = uploadfile.getOriginalFilename(); // 원래 파일명
                boarddata.setInqOriginal(fileName);

                String fileDBName = fileDBName(fileName, saveFolder);
                log.info("fileDBName = " + fileDBName);
                // transferTo(File Path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
                uploadfile.transferTo(new File(saveFolder + fileDBName));
                log.info("transferTo path = " + saveFolder + fileDBName);
                // 바뀐 파일명으로 저장
                boarddata.setInqFile(fileDBName);
            } else { // 기존 파일이 없는데 파일 선택하지 않은 경우 또는 기존 파일이 있었는데 삭제한 경우
                log.info("선택 파일 없습니다.");
                // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
                // 위 태그에 값이 있다면 ""로 값을 변경합니다.
                boarddata.setInqFile(""); // ""로 초기화 합니다.
                boarddata.setInqOriginal(""); // ""로 초기화 합니다.
            } // else end
        } // else end

        // DAO에서 수정 메서드 호출하여 수정합니다.
        int result = inqueryService.boardModify(boarddata);

        if (result == 0) { // 수정에 실패한 경우
            log.info("게시판 수정 실패");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "게시판 수정 실패");
            url = "error/error";
        } else { // 수정 성공의 경우
            log.info("게시판 수정 완료");
            // 수정한 글 내용을 보여주기 위해 글 내용 보기 - 보기 페이지로 이동하기 위해 경로를 설정합니다.
            url = "redirect:detail";
            rattr.addAttribute("num", boarddata.getInqNum());
        }
        return url;
    }

    @GetMapping("/delete")
    public String BoardDeleteAction(String inq_pass, int num,
                                    Model mv, RedirectAttributes rattr,
                                    HttpServletRequest request) {
        // 글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기 위해
        // 입력한 비밀번호와 저장된 비밀번호를 비교하여 일치하면 삭제합니다.
        boolean usercheck = false;
        if (inq_pass == null || inq_pass.equals("")) {
            // 2024-04-04 비밀번호가 없는 1대1 문의 게시물인 경우 DAO에서 PASS가 null 인지 판단합니다.
            usercheck = inqueryService.isBoardPassNull(num);
        } else {
            // 글쓴이 인지 확인하기 위해 저장된 비밀번호와 입력한 비밀번호를 비교합니다.
            usercheck = inqueryService.isBoardWriter(num, inq_pass);
        }
        // 비밀번호 일치하지 않는 경우
        if (usercheck == false) {
            rattr.addFlashAttribute("result", "passFail");
            rattr.addAttribute("num", num);
            return "redirect:detail";
        }

        // 비밀번호 일치하는 경우 삭제 처리 합니다.
        int result = inqueryService.boardDelete(num);

        // 삭제 처리 실패한 경우
        if (result == 0) {
            log.info("게시판 삭제 실패");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "삭제 실패");
            return "error/error";
        } else {
            // 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분입니다.
            log.info("게시판 삭제 성공");
            rattr.addFlashAttribute("result", "deleteSuccess");
            return "redirect:list";
        }
    }

    @ResponseBody
    @PostMapping("/down")
    public byte[] BoardFileDown(String filename,
                                HttpServletRequest request,
                                String original,
                                HttpServletResponse response) throws Exception {

        //String savePath = "resources/upload";
        // 서블릿의 실행 환경 정보를 담고 있는 객체를 리턴합니다.
        //ServletContext context = request.getSession().getServletContext();
        //String sDownloadPath = context.getRealPath(savePath);
        //String sFilePath = sDownloadPath + filename;
        //수정
        String sFilePath = saveFolder + filename;

        File file = new File(sFilePath);

        byte[] bytes = FileCopyUtils.copyToByteArray(file); // 유틸을 통해 byte array로 쉽게 생성 가능

        String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");

        // Content-Disposition: attachment: 브라우저는 해당 content를 처리하지 않고, 다운로드하게 됩니다.
        response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding);

        response.setContentLength(bytes.length);

        return bytes;
    }
}
