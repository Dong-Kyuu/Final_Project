package com.example.jhta_3team_finalproject.domain.inquery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class InqueryComment implements Comparable<InqueryComment> {
    private int num;
    private String id;
    private String content;
    private String regDate;
    private int commentBoardNum;
    private int commentReReferer;
    private int commentReLevel;
    private int commentReSequence;

    // 2024-05-09 답글 등록순, 최신순 처리
    @Override
    public int compareTo(InqueryComment o) {
        return this.num - o.num;
    }
}
