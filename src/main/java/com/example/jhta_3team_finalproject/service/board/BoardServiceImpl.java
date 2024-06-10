package com.example.jhta_3team_finalproject.service.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jhta_3team_finalproject.domain.Board.Board;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.BoardMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.UpfilesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardMapper Bdao;
    private UpfilesMapper Fdao;

    @Autowired
    public BoardServiceImpl(BoardMapper Bdao, UpfilesMapper Fdao) {
        this.Bdao = Bdao;
        this.Fdao = Fdao;
    }

    // 글 개수 가져오기
    @Override
    public int getListCount(int index, String searchWord, String targetDepartment) {
        Map<String, String> map = new HashMap<String, String>();
        if(index != -1) {
            String[] search_field = new String[] {"board_title", "board_content", "user_name"};
            map.put("search_field", search_field[index]);
            map.put("search_word", "%" + searchWord + "%");
        }
        map.put("target_department", targetDepartment);
        return Bdao.getListCount(map);
    }

    // 글 목록 가져오기
    @Override
    public List<Board> getBoardList(int index, String searchWord, String targetDepartment, int page, int limit) {

        HashMap<String, Object> map = new HashMap<String, Object>();

        if(index != -1) {
            String[] search_field = new String[] {"board_title", "board_content", "user_name"};
            map.put("search_field", search_field[index]);
            map.put("search_word", "%" + searchWord + "%");
        }
        int startrow = (page - 1) * limit + 1;
        int endrow = startrow + limit - 1;
        map.put("start", startrow);
        map.put("end", endrow);
        map.put("target_department", targetDepartment);
        return Bdao.getBoardList(map);

    }

    // 글 등록
    @Override
    public void insertBoard(Board board) {
        Bdao.insertBoard(board);
    }

    // 글 등록 시 업로드 파일 저장
    public void insertFile(int boardNum, List<BoardUpfiles> files) {
        for (BoardUpfiles file : files) {
            file.setBoardNum(boardNum); // 게시글 번호 생성
            Fdao.insertFile(file);
        }
    }

    // 업로드파일 조회
    public List<BoardUpfiles> getFilesByBoardNum(int board_num) {
        return Fdao.getFilesbyBoardNum(board_num);
    }

    // 조회수 증가
    @Override
    public int setReadCountUpdate(int num) {
        return Bdao.setReadCountUpdate(num);
    }

    // 글 내용 가져오기
    @Override
    public Board getDetail(int num) {
        return Bdao.getDetail(num);
    }

    @Override
    public int boardDelete(int num) {
        int result = 0;
        Board board = Bdao.getDetail(num);
        if(board != null) {
            result = Bdao.boardDelete(board);
        }
        return result;
    }

    @Override
    public int deleteFile(int boardNum) {
        return Fdao.deleteFile(boardNum);
    }

    @Override
    public int boardModify(Board bdata) {
        return Bdao.boardModify(bdata);
    }

}
