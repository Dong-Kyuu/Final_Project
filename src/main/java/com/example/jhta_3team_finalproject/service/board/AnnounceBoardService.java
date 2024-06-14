package com.example.jhta_3team_finalproject.service.board;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.User.User;

import java.util.List;

public interface AnnounceBoardService {
    int getListCount(int index, String searchWord, String targetDepartment);

    List<AnnounceBoard> getBoardList(int index, String searchWord, String targetDepartment, int page, int limit);

    void insertBoard(AnnounceBoard board);

    void insertFile(int boardNum, List<BoardUpfiles> files);

    int setReadCountUpdate(int num);

    AnnounceBoard getDetail(int num);

    List<BoardUpfiles> getFilesByBoardNum(int num);

    int boardDelete(int bnum);

    // 글 확인했는지 처리
    String viewChecking(int loginNum, int annboardNum);

    int Exist(int loginNum, int annboardNum);

    int addCheck(int loginNum, int annboardNum);

    int deleteCheck(int loginNum, int annboardNum);

    int checkedcheck(int loginNum, int annboardNum);

    void autoCheck(int userNum, int boardNum);

    List<User> getUserData(int annboardNum);

    List<String> getDepartment();

    String targetDepartment(int annboardNum);

    int getMaxCheck(String targetDepartment);

    int checkedUserByDepartment(String targetDepartment, int annboardNum);

    int downImportance(int annboardNum);

    int doTopFix(int annboardNum);

    int TopFixclear(int annboardNum);

    int[] searchFixAuth();
}
