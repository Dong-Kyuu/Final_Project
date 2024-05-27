package com.example.jhta_3team_finalproject.mybatis.mapper;


import com.example.jhta_3team_finalproject.domain.inquery.InqueryBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/*
 * Mapper 인터페이스란 매퍼 파일에 기재된 SQL을 호출하기 위한 인터페이스입니다.
 * MyBatis-Spring은 Mapper 인터페이스를 이용해서 실제 SQL 처리가 되는 클래스를 자동으로 생성합니다.
 * */
@Mapper
public interface InqueryBoardMapper {
	
	// 글의 갯수 구하기
	public int getListCount();

	// 글의 리스트 구하기
	public List<InqueryBoard> getBoardList(HashMap<String, Integer> map);
	
	// 글 내용 보기
	public InqueryBoard getDetail(int num);
	
	// 글 답변
	public int boardReply(InqueryBoard inqueryBoard);
	
	// 글 수정
	public int boardModify(InqueryBoard inqueryBoard);
	
	// 글 삭제
	public int boardDelete(InqueryBoard inqueryBoard);
	
	// 조회수 업데이트
	public int setReadCountUpdate(int num);
	
	// 글쓴이인지 확인
	public InqueryBoard isBoardWriter(HashMap<String, Object> map);

	// 비밀번호가 없는지 확인
	public InqueryBoard isBoardPassNull(int num);

	// 글 등록하기
	public void insertBoard(InqueryBoard inqueryBoard);
	
	// BOARD_RE_SEQ 값 수정
	public int boardReplyUpdate(InqueryBoard inqueryBoard);

	public List<String> getDeleteFileList();

	public void deleteFileList(String filename);


}
