package com.example.jhta_3team_finalproject.service;


import com.example.jhta_3team_finalproject.domain.inquery.InqueryComment;
import com.example.jhta_3team_finalproject.mybatis.mapper.InqCommentMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InqCommentServiceImpl implements InqCommentService {

    private InqCommentMapper dao;

    @Autowired
    public InqCommentServiceImpl(InqCommentMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getListCount(int board_num) {
        return dao.getListCount(board_num);
    }

    @Override
    public int commentsInsert(InqueryComment c) {
        return dao.commentsInsert(c);
    }

    @Override
    public int commentsUpdate(InqueryComment co) {
        return dao.commentsUpdate(co);
    }

    @Override
    public int commentsDelete(int num) {
        return dao.commentsDelete(num);
    }

    @Override
    public JsonArray getCommentJsonList(int comment_board_num, int state) {
        JsonArray array = new JsonArray();

        String sort = "asc"; // 등록순
        if (state == 2) {         // 최신순
            sort = "desc";
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("comment_board_num", comment_board_num);
        map.put("sort", sort);

        List<InqueryComment> list = dao.getCommentList(map);
        if (state == 2) // 최신순
            Collections.sort(list, Collections.reverseOrder());
        else // 등록순
            Collections.sort(list);

        list.forEach(inqueryComment -> {
            JsonObject object = new JsonObject();
            object.addProperty("num", inqueryComment.getNum());
            object.addProperty("id", inqueryComment.getId());
            object.addProperty("content", inqueryComment.getContent());
            object.addProperty("reg_date", inqueryComment.getReg_date());
            object.addProperty("comment_re_lev", inqueryComment.getComment_re_lev());
            object.addProperty("comment_re_seq", inqueryComment.getComment_re_seq());
            object.addProperty("comment_re_ref", inqueryComment.getComment_re_ref());
            array.add(object);
        });
        return array;
    }

    @Override
    public int commentReplyUpdate(InqueryComment co) {
        return dao.commentReplyUpdate(co);
    }

    @Override
    @Transactional // Advisor로 구현된 AOP이다.
    public int commentsReply(InqueryComment co) {
        commentReplyUpdate(co);
        // double i = 1/0;
        co.setComment_re_lev(co.getComment_re_lev() + 1);
        co.setComment_re_seq(co.getComment_re_seq() + 1);
        return dao.commentReply(co);
    }

}
