package com.example.jhta_3team_finalproject.service;

import com.example.jhta_3team_finalproject.domain.inquery.InqueryComment;
import com.google.gson.JsonArray;

public interface InqCommentService {
	
	// 글의 갯수 구하기
	public int getListCount(int board_num);
	
	// 댓글 등록하기
	public int commentsInsert(InqueryComment c);
	
	// 댓글 삭제
	public int commentsDelete(int num);
	
	// 댓글 수정
	public int commentsUpdate(InqueryComment co);

	// 댓글 목록 가져오기
	JsonArray getCommentJsonList(int commentBoardNum, int state);

	int commentsReply(InqueryComment co);

	int commentReplyUpdate(InqueryComment co);

}
