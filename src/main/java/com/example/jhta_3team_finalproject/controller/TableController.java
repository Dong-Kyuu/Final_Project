package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.Table.Board;
import com.example.jhta_3team_finalproject.domain.Table.Table_Files;
import com.example.jhta_3team_finalproject.service.table.BoardService;
import com.example.jhta_3team_finalproject.service.table.TableCommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.*;
import java.util.Random;

@Controller
@RequestMapping(value = "/table")
public class TableController {

    private static final Logger log = LoggerFactory.getLogger(TableController.class);

    @Value("${my.savefolder}")
    private String saveFolder;
    private BoardService BS;
    private TableCommentService CS;

    @Autowired
    public TableController(BoardService BS, TableCommentService CS) {
        this.BS = BS;
        this.CS = CS;
    }

    // 리스트 가져오기
    @RequestMapping(value = "/freelist", method = RequestMethod.GET)
    public ModelAndView freeTable(@RequestParam(value = "page", defaultValue = "1") int page,
                                  ModelAndView mv) {

        int limit = 10; // 한 화면에 출력할 로우 갯수
        int listcount = BS.getListCount(); // 총 리스트 수를 받아온다.

        // 총 페이지 수
        int maxpage = (listcount + limit - 1) / limit;

        // 현재 페이지에 보여줄 시작 페이지 수
        int startpage = ((page - 1) / 10) * 10 + 1;
        // 현재 페이지에 보여줄 마지막 페이지 수
        int endpage = startpage + 10 - 1;

        if (endpage > maxpage)
            endpage = maxpage;

        List<Board> boardlist = BS.getBoardList(page, limit); // 리스트를 받아옴

        mv.setViewName("table/Free_table");
        mv.addObject("page", page);
        mv.addObject("maxpage", maxpage);
        mv.addObject("startpage", startpage);
        mv.addObject("endpage", endpage);
        mv.addObject("listcount", listcount);
        mv.addObject("boardlist", boardlist);
        mv.addObject("limit", limit);

        return mv;

    }

    // 글쓰기 폼 불러오기
    @GetMapping("/f_write")
    public String wirteForm() {
        return "table/Fwrite";
    }

    // 작성 글 DB 등록
    @PostMapping("/add")
    public String add(Board board, HttpServletRequest request,
                      @RequestParam("uploadfile[]") MultipartFile[] uploadfiles)
            throws Exception {

        // Board 객체를 먼저 저장하고, BOARD_NUM을 받아옵니다.
        BS.insertBoard(board); // Board 객체 저장
        int boardNum = board.getBOARD_NUM(); // 저장된 BOARD_NUM 가져오기

        List<Table_Files> files = new ArrayList<>();
        for (MultipartFile uploadfile : uploadfiles) {
            if (!uploadfile.isEmpty()) {
                String fileName = uploadfile.getOriginalFilename(); // 원래 파일명

                String fileDBName = fileDBName(fileName, saveFolder);
                log.info("fileDBName = " + fileDBName);

                uploadfile.transferTo(new File(saveFolder + fileDBName));
                log.info("transferTo path = " + saveFolder + fileDBName);

                Table_Files file = new Table_Files();
                file.setOriginal_file_name(fileName);
                file.setFile_name(fileDBName);
                files.add(file);


            }
        }

        BS.insertFile(boardNum, files); // 저장메서드 호출
        log.info(board.toString()); //selectKey로 정의한 BOARD_NUM 값 확인
        return "redirect:freelist";

    }

    // 서버에 파일 생성/파일 이름 생성 로직
    private String fileDBName(String fileName, String saveFolder) {
        // 새로운 폴더 이름 : 오늘 년+월+일
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);            //오늘 년도
        int month = c.get(Calendar.MONTH) + 1;    //오늘 월
        int date = c.get(Calendar.DATE);            //오늘 일

        String homedir = saveFolder + "/" + year + "-" + month + "-" + date;
        log.info(homedir);
        File path1 = new File(homedir);
        if (!(path1.exists())) {
            path1.mkdirs(); // 새로운 폴더 생성
        }

        // 난수 획득
        Random r = new Random();
        int random = r.nextInt(100000000);

        // 확장자 구하기 시작
        int index = fileName.lastIndexOf(".");
        // 문자열에서 특정 문자열의 위치 값(index) 반환
        // indexOf가 처음 발견되는 문자열에 대한 Index를 반환하는 반면,
        // lastIndexOf는 마지막으로 발견되는 문자열의 Index를 반환
        // (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴한다.
        log.info("index = " + index);

        String fileExtension = fileName.substring(index + 1);
        log.info("fileExtension = " + fileExtension);
        // 확장자 구하기 끝

        // 새로운 파일명
        String refileName = "bbs" + year + month + date + random + "." + fileExtension;
        log.info("refileName = " + refileName);

        // MySQL 디비에 저장될 파일 명
        // String fileDBName = "/" + year + "-" month + "-" + date + "/" + refileName;
        String fileDBName = File.separator + year + "-" + month + "-" + date
                + File.separator + refileName;
        log.info("fileDBName = " + fileDBName);

        return fileDBName;
    }

    @GetMapping("/detail")
    public ModelAndView Detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            @RequestHeader(value = "referer", required = false) String beforeURL) {
		/*
			1. String BeforeURL = request.getHeader("referer"); 의미로
			   어느 주소에서 Detail로 이동했는지 header의 정보 중 "referer"을 통해 알 수 있다.
			2. 수정 후 이곳으로 이동하는 경우 조회수는 증가하지 않도록 한다.
			3. myhome4/board/list에서 제목을 클릭한 경우 조회수가 증가하도록 한다.
		 */

        log.info("referer : " + beforeURL);
        if (beforeURL != null && beforeURL.endsWith("list")) {
            BS.setReadCountUpdate(num);
        }

        Board board = BS.getDetail(num);
        List<Table_Files> upfiles = BS.getFilesByBoardNum(num);
        // board = null; // error페이지 이동 확인하고자 임의로 지정.
        if (board == null) {
            log.info("상세보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "실패하였습니다.");
        } else {
            log.info("상세보기 성공");
            int count = CS.getListCount(num);
            mv.setViewName("table/Fview");
            mv.addObject("count", count);
            mv.addObject("boarddata", board);
            mv.addObject("upfiles", upfiles);

        }
        return mv;
    }

    @PostMapping("/delete")
    public String BoardDeleteAction(int bnum,
                                    Model mv, RedirectAttributes rattr,
                                    HttpServletRequest request) {

        int result = BS.boardDelete(bnum);

        // 삭제 처리 실패한 경우
        if(result == 0) {
            log.info("삭제 실패!");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "삭제 실패");
            return "error/error";
        } else {
            // 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분
            log.info("삭제 완료!");
            rattr.addFlashAttribute("result", "deleteSuccess");
            return "redirect:freelist";
        }
    }

    @ResponseBody
    @PostMapping("/down")
    public byte[] BoardFileDown(String filename,
                                HttpServletRequest request,
                                String original,
                                HttpServletResponse response) throws Exception {

        // String savePath = "resources/upload";
        // 서블릿의 실행 환경 정보를 담고 있는 객체를 리터한다.
        // ServletContext context = request.getSession().getServletContext();
        // String sDownloadPath = context.getRealPath(savePath);
        // 수정
        String sFilePath = saveFolder + filename;

        File file = new File(sFilePath);

        byte[] bytes = FileCopyUtils.copyToByteArray(file);

        String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");

        // Context-Disposition : attachment : 브라우저는 해당 Context를 처리하지 않고, 다운로드 하게 된다.
        response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding);

        response.setContentLength(bytes.length);
        return bytes;
    }

    @GetMapping("/t")
    public String test_table() {
        return "/table/basic-table_backup";
    }
}
