package com.example.jhta_3team_finalproject.service.customer;


import com.example.jhta_3team_finalproject.domain.inquery.InqueryComment;
import com.example.jhta_3team_finalproject.mybatis.mapper.customer.InqCommentMapper;
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
    public int getListCount(int boardNum) {
        return dao.getListCount(boardNum);
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
    public JsonArray getCommentJsonList(int commentBoardNum, int state) {
        final int ASC = 2;

        JsonArray array = new JsonArray();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commBoardNum", commentBoardNum);
        map.put("sort", "asc");

        List<InqueryComment> list = dao.getCommentList(map);
        if (state == ASC) // 최신순
            Collections.sort(list, Collections.reverseOrder());
        else // 등록순
            Collections.sort(list);

        list.forEach(inqueryComment -> {
            JsonObject object = new JsonObject();
            object.addProperty("num", inqueryComment.getCommNum());
            object.addProperty("id", inqueryComment.getCommId());
            object.addProperty("content", inqueryComment.getCommContent());
            object.addProperty("reg_date", inqueryComment.getRegDate());
            object.addProperty("comment_board_num", inqueryComment.getCommBoardNum());
            object.addProperty("comment_re_lev", inqueryComment.getCommReLevel());
            object.addProperty("comment_re_seq", inqueryComment.getCommReSequence());
            object.addProperty("comment_re_ref", inqueryComment.getCommReReferer());
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
        co.setCommReLevel(co.getCommReLevel() + 1);
        co.setCommReSequence(co.getCommReSequence() + 1);
        return dao.commentReply(co);
    }

}
