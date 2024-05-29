package com.example.jhta_3team_finalproject.service.table;

import java.util.HashMap;
import java.util.List;

import com.example.jhta_3team_finalproject.domain.Table.Board;
import com.example.jhta_3team_finalproject.domain.Table.Table_Files;
import com.example.jhta_3team_finalproject.mybatis.mapper.BoardMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.UpfilesMapper;
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
	public int getListCount() {
		return Bdao.getListCount();
	}

	// 글 목록 가져오기
	@Override
	public List<Board> getBoardList(int page, int limit) {

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int startrow = (page-1)*limit+1;
		int endrow = startrow+limit-1;
		map.put("start", startrow);
		map.put("end", endrow);
		return Bdao.getBoardList(map);

	}

	// 글 등록
	@Override
	public void insertBoard(Board board) {
		Bdao.insertBoard(board);
	}

	// 글 등록 시 업로드 파일 저장
	public void insertFile(int boardNum, List<Table_Files> files) {
		for(Table_Files file : files) {
			file.setBoard_num(boardNum); // 게시글 번호 생성
			Fdao.insertFile(file);
		}
	}

	// 업로드파일 조회
	public List<Table_Files> getFilesByBoardNum(int board_num) {
		return Fdao.getFilesbyBoardNum(board_num);
	}

	// 조회수 증가
	@Override
	public int setReadCountUpdate(int num) {
		return Bdao.setReadCountUpdate(num);
	}

	@Override
	public Board getDetail(int num) {
		return Bdao.getDetail(num);
	}
}