package com.example.jhta_3team_finalproject.service.board;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.Board;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.AnnounceBoardMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.UpfilesMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.ViewCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnnounceBoardServiceImpl implements AnnounceBoardService{

    private AnnounceBoardMapper AnnounceBoardMapper;
    private UpfilesMapper UpfilesMapper;
    private ViewCheckMapper ViewCheckMapper;

    @Autowired
    public AnnounceBoardServiceImpl(AnnounceBoardMapper AnnounceBoardMapper, UpfilesMapper UpfilesMapper, ViewCheckMapper ViewCheckMapper) {
        this.AnnounceBoardMapper = AnnounceBoardMapper;
        this.UpfilesMapper = UpfilesMapper;
        this.ViewCheckMapper = ViewCheckMapper;
    }

    @Override
    public int getListCount(int index, String searchWord, String targetDepartment) {
        Map<String, String> map = new HashMap<String, String>();
        if(index != -1) {
            String[] search_field = new String[] {"annboard_title", "annboard_content", "user_name"};
            map.put("search_field", search_field[index]);
            map.put("search_word", "%" + searchWord + "%");
        }
        map.put("target_department", targetDepartment);
        return AnnounceBoardMapper.getListCount(map);

    }

    @Override
    public List<AnnounceBoard> getBoardList(int index, String searchWord, String targetDepartment, int page, int limit) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        if(index != -1) {
            String[] search_field = new String[] {"annboard_title", "annboard_content", "user_name"};
            map.put("search_field", search_field[index]);
            map.put("search_word", "%" + searchWord + "%");
        }
        int startrow = (page - 1) * limit + 1;
        int endrow = startrow + limit - 1;
        map.put("start", startrow);
        map.put("end", endrow);
        map.put("target_department", targetDepartment);
        return AnnounceBoardMapper.getBoardList(map);

    }

    @Override
    public void insertBoard(AnnounceBoard board) {
        AnnounceBoardMapper.insertBoard(board);
    }

    @Override
    public void insertFile(int boardNum, List<BoardUpfiles> files) {
        for (BoardUpfiles file : files) {
            file.setBoardNum(boardNum); // 게시글 번호 생성
            UpfilesMapper.insertAnnounceFile(file);
        }
    }

    @Override
    public int setReadCountUpdate(int num) {
        return AnnounceBoardMapper.setReadCountUpdate(num);
    }

    @Override
    public AnnounceBoard getDetail(int num) {
        return AnnounceBoardMapper.getDetail(num);
    }

    @Override
    public List<BoardUpfiles> getFilesByBoardNum(int num) {
        return UpfilesMapper.getFilesbyAnnBoardNum(num);
    }

    @Override
    public int boardDelete(int bnum) {
        int result = 0;
        AnnounceBoard AnnounceBoard = AnnounceBoardMapper.getDetail(bnum);
        if(AnnounceBoard != null) {
            result = AnnounceBoardMapper.boardDelete(AnnounceBoard);
        }
        return result;
    }

    @Override
    public String viewChecking(int loginNum, int annboardNum) {
        int result =  ViewCheckMapper.createView(loginNum, annboardNum);
        if(result == 1) {
            return "글 확인 기록됨";
        }
        return "글 확인 기록실패";
    }

    @Override
    public int Exist(int loginNum, int annboardNum) {

        return ViewCheckMapper.ExistViewRecord(loginNum, annboardNum);
    }

    @Override
    public int addCheck(int loginNum, int annboardNum) {
        return ViewCheckMapper.addCheck(loginNum, annboardNum);
    }

    @Override
    public int deleteCheck(int loginNum, int annboardNum) {
        return ViewCheckMapper.deleteCheck(loginNum, annboardNum);
    }

    @Override
    public int checkedcheck(int loginNum, int annboardNum) {
        return ViewCheckMapper.Checkedcheck(loginNum, annboardNum);
    }

    @Override
    public void autoCheck(int userNum, int boardNum) {
        ViewCheckMapper.autoCheck(userNum, boardNum);
    }
}
