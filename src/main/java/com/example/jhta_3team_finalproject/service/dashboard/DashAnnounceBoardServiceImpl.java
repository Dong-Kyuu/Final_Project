package com.example.jhta_3team_finalproject.service.dashboard;

import com.example.jhta_3team_finalproject.controller.AnnounceBoardController;
import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.AnnounceBoardMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.UpfilesMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.ViewCheckMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashAnnounceBoardServiceImpl implements DashAnnounceBoardService {

    private static final Logger logger = LoggerFactory.getLogger(AnnounceBoardController.class);

    private AnnounceBoardMapper AnnounceBoardMapper;
    private UpfilesMapper UpfilesMapper;
    private ViewCheckMapper ViewCheckMapper;

    @Autowired
    public DashAnnounceBoardServiceImpl(AnnounceBoardMapper AnnounceBoardMapper, UpfilesMapper UpfilesMapper, ViewCheckMapper ViewCheckMapper) {
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

    @Override
    public List<User> getUserData(int annboardNum) {
        List<User> users = AnnounceBoardMapper.getUserData(annboardNum);
        logger.info("users : " + users);
        return users;
    }

    @Override
    public List<String> getDepartment() {
        return AnnounceBoardMapper.getDepartment();
    }

    @Override
    public String targetDepartment(int annboardNum) {
        String department = AnnounceBoardMapper.targetDepartment(annboardNum);
//        if(department.equals("ALL")) {
//            department = "임원";
//        }
        return department;
    }

    @Override
    public int getMaxCheck(String targetDepartment) {
        return AnnounceBoardMapper.getMaxCheck(targetDepartment);
    }

    @Override
    public int checkedUserByDepartment(String targetDepartment, int annboardNum) {
        return AnnounceBoardMapper.checkedUserByDepartment(targetDepartment, annboardNum);
    }

    @Override
    public int downImportance(int annboardNum) {
        return AnnounceBoardMapper.downImportance(annboardNum);
    }

    @Override
    public int doTopFix(int annboardNum) {
        return AnnounceBoardMapper.doTopFix(annboardNum);
    }

    @Override
    public int TopFixclear(int annboardNum) {
        return AnnounceBoardMapper.topFixClear(annboardNum);
    }

    @Override
    public int[] searchFixAuth() {
        return AnnounceBoardMapper.searchFixAuth();
    }

    @Override
    public int fixRequest(int annboardNum) {
        return AnnounceBoardMapper.fixRequest(annboardNum);
    }

    @Override
    public int requestRefuse(int annboardNum) {
        return AnnounceBoardMapper.requestRefuse(annboardNum);
    }

    @Override
    public int[] getAllUserData(int userNum, String Department) {

        int departmentId=0;

        if(Department.equals("ALL")) {
            departmentId=0;
        } else if (Department.equals("인사부")) {
            departmentId=1;
        } else if (Department.equals("관리부")) {
            departmentId=2;
        } else if (Department.equals("홍보부")) {
            departmentId=3;
        } else if (Department.equals("지원부")) {
            departmentId=4;
        } else if (Department.equals("영업부")) {
            departmentId=5;
        }
        return AnnounceBoardMapper.getAllUserData(userNum, departmentId);
    }

    @Override
    public String getWriter(int userNum) {
        return AnnounceBoardMapper.getWriter(userNum);
    }
}
