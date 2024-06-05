package com.example.jhta_3team_finalproject.service.customer;


import com.example.jhta_3team_finalproject.domain.inquery.InqueryBoard;

import java.util.List;

public interface InqueryService {

    // 글의 갯수 구하기
    public int getListCount();

    // 글 목록 보기
    public List<InqueryBoard> getBoardList(int page, int limit);

    // 글 내용 보기
    public InqueryBoard getDetail(int num);

    // 글 수정
    public int boardModify(InqueryBoard inqueryBoard);

    // 글 삭제
    public int boardDelete(int num);

    // 조회수 업데이트
    public int setReadCountUpdate(int num);

    // 글쓴이인지 확인
    public boolean isBoardWriter(int num, String password);

    // 글 등록하기
    public void insertBoard(InqueryBoard inqueryBoard);

    public List<String> getDeleteFileList();

    public void deleteFileList(String filename);

    public boolean isBoardPassNull(int num);
}
