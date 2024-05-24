package com.example.jhta_3team_finalproject.service;

import java.util.HashMap;
import java.util.List;

import com.example.jhta_3team_finalproject.domain.board.Board;
import com.example.jhta_3team_finalproject.mybatis.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl implements BoardService {


	private BoardMapper dao;
	
	@Autowired
	public BoardServiceImpl(BoardMapper dao) {
		this.dao = dao;
	}

	@Override
	public int getListCount() {
		return dao.getListCount();
	}

	@Override
	public List<Board> getBoardList(int page, int limit) {
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int startrow = (page-1)*limit+1;
		int endrow = startrow+limit-1;
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getBoardList(map);

	}

}
