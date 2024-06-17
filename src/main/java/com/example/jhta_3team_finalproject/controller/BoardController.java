package com.example.jhta_3team_finalproject.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.example.jhta_3team_finalproject.domain.Board.Board;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.service.S3.S3Service;
import com.example.jhta_3team_finalproject.service.board.BoardService;
import com.example.jhta_3team_finalproject.service.board.TableCommentService;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.*;
import java.util.Random;

@Controller
@RequestMapping(value = "/table")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);


    private BoardService BS;
    private TableCommentService CS;
    private S3Service s3Service;

    @Autowired
    public BoardController(BoardService BS, TableCommentService CS, S3Service s3Service) {
        this.BS = BS;
        this.CS = CS;
        this.s3Service = s3Service;
    }

    // 리스트 가져오기
    @RequestMapping(value = "/freelist", method = RequestMethod.GET)
    public ModelAndView freeTable(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "searchField", defaultValue = "-1") int index,
                                  @RequestParam(value = "search", defaultValue = "") String searchWord,
                                  @RequestParam(value = "targetDepartment", defaultValue = "") String targetDepartment,

                                  ModelAndView mv) {
        logger.info("타겟보기="+targetDepartment);
        int limit = 10; // 한 화면에 출력할 로우 갯수
        int listcount = BS.getListCount(index, searchWord, targetDepartment); // 총 리스트 수를 받아온다.
        logger.info("listcount:" + listcount);
        // 총 페이지 수
        int maxpage = (listcount + limit - 1) / limit;

        // 현재 페이지에 보여줄 시작 페이지 수
        int startpage = ((page - 1) / 10) * 10 + 1;
        // 현재 페이지에 보여줄 마지막 페이지 수
        int endpage = startpage + 10 - 1;

        if (endpage > maxpage)
            endpage = maxpage;

        List<Board> boardlist = BS.getBoardList(index, searchWord, targetDepartment, page, limit); // 리스트를 받아옴

        mv.setViewName("table/Free_table");
        mv.addObject("page", page);
        mv.addObject("maxpage", maxpage);
        mv.addObject("startpage", startpage);
        mv.addObject("endpage", endpage);
        mv.addObject("listcount", listcount);
        mv.addObject("boardlist", boardlist);
        mv.addObject("limit", limit);
        mv.addObject("search_field", index);
        mv.addObject("search_word", searchWord);
        mv.addObject("targetDepartment", targetDepartment);


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
        int boardNum = board.getBoardNum(); // 저장된 BOARD_NUM 가져오기

        List<BoardUpfiles> files = new ArrayList<>();
        for (MultipartFile uploadfile : uploadfiles) {
            if (!uploadfile.isEmpty()) {
                String fileUrl = s3Service.uploadFile(uploadfile);
                logger.info("Uploaded file URL: " + fileUrl);

                BoardUpfiles file = new BoardUpfiles();
                file.setUpfilesOriginalFileName(uploadfile.getOriginalFilename());
                file.setUpfilesFileName(fileUrl); // S3 URL로 설정
                files.add(file);
            }
        }

        BS.insertFile(boardNum, files); // 저장메서드 호출
        logger.info(board.toString()); //selectKey로 정의한 BOARD_NUM 값 확인
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
        logger.info(homedir);
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
        logger.info("index = " + index);

        String fileExtension = fileName.substring(index + 1);
        logger.info("fileExtension = " + fileExtension);
        // 확장자 구하기 끝

        // 새로운 파일명
        String refileName = "bbs" + year + month + date + random + "." + fileExtension;
        logger.info("refileName = " + refileName);

        // MySQL 디비에 저장될 파일 명
        // String fileDBName = "/" + year + "-" month + "-" + date + "/" + refileName;
        String fileDBName = File.separator + year + "-" + month + "-" + date
                + File.separator + refileName;
        logger.info("fileDBName = " + fileDBName);

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

        logger.info("referer : " + beforeURL);
        if (beforeURL != null && beforeURL.endsWith("list")) {
            BS.setReadCountUpdate(num);
        }

        Board board = BS.getDetail(num);
        List<BoardUpfiles> upfiles = BS.getFilesByBoardNum(num);
        // board = null; // error페이지 이동 확인하고자 임의로 지정.
        if (board == null) {
            logger.info("상세보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "실패하였습니다.");
        } else {
            logger.info("상세보기 성공");
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
            logger.info("삭제 실패!");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "삭제 실패");
            return "error/error";
        } else {
            // 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분
            logger.info("삭제 완료!");
            rattr.addFlashAttribute("result", "deleteSuccess");
            return "redirect:freelist";
        }
    }

    @PostMapping("/modifyView")
    public ModelAndView BoardModifyView(
            int num, ModelAndView mv,
            HttpServletRequest request) {

        Board board = BS.getDetail(num);
        List<BoardUpfiles> upfiles = BS.getFilesByBoardNum(num);

        logger.info("upfiles = " + upfiles.toString());

        // 글 내용 불러오기 실패한 경우
        if(board == null) {
            logger.info("수정보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "수정폼을 불러오는데 실패하였습니다.");
        } else {
            logger.info("수정폼 불러오기 성공");
            mv.setViewName("table/Fmodify");
            mv.addObject("bdata", board);
            mv.addObject("upfiles", upfiles);
        }
        return mv;
    }

    @PostMapping("/modifyAction")
    public String BoardModifyAction(
            Board bdata, Model mv,
            @RequestParam(value = "check", required = false, defaultValue = "false") boolean check,
            HttpServletRequest request,
            RedirectAttributes rattr,
            @RequestParam("uploadfile[]") MultipartFile[] uploadfiles
    ) throws Exception {

        String url = "";
        int boardNum = bdata.getBoardNum();
        List<BoardUpfiles> files = new ArrayList<>();
        logger.info(String.valueOf(check));
        // String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload";

        if(!check) { 					// 기존 파일을 그대로 사용하는 경우
            logger.info("기존 파일 그대로 사용");
        } else {
            int result;
            result = BS.deleteFile(boardNum);
            logger.info(String.valueOf(result));

            if(uploadfiles != null) {
                logger.info("파일 변경되었습니다.");

                for (MultipartFile uploadfile : uploadfiles) {
                    if (!uploadfile.isEmpty()) {

                        String fileUrl = s3Service.uploadFile(uploadfile);
                        logger.info("Uploaded file URL: " + fileUrl);

                        BoardUpfiles file = new BoardUpfiles();
                        file.setUpfilesOriginalFileName(uploadfile.getOriginalFilename());
                        file.setUpfilesFileName(fileUrl); // S3 URL로 설정
                        files.add(file);
                    }
                }

                BS.insertFile(boardNum, files); // 저장메서드 호출
            } else { // 기존 파일이 없는데 파일 선택하지 않은 경우 또는 기존 파일이 있었는데 삭제한 경우
                logger.info("선택 파일이 없음");
            }
        }

        // DAO에서 수정 메서드 호출하여 수정.
        int result = BS.boardModify(bdata);
        // 수정에 실패한 경우
        if(result == 0) {
            logger.info("게시판 수정 실패");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "게시판 수정 실패");
            url="error/error";

        } else { // 수정 성공의 경우
            logger.info("게시판 수정 완료");
            // 수정한 글 내용을 보여주기 위해 글 내용 보기. 보기페이지로 이동하기 위해 경로를 설정.
            url = "redirect:detail";
            rattr.addAttribute("num", bdata.getBoardNum());
        }
        return url;

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
            S3Object s3Object = s3Service.downloadFile(s3Key);
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

    @GetMapping("/t")
    public String test_table() {
        return "/table/basic-table_backup";
    }
}
