package com.example.jhta_3team_finalproject.domain.inquery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class InqueryComment implements Comparable<InqueryComment> {
    private int commNum;
    private String commId;
    private String commContent;
    private String regDate;
    private int commBoardNum;
    private int commReReferer;
    private int commReLevel;
    private int commReSequence;

    // 2024-05-09 답글 등록순, 최신순 처리
    @Override
    public int compareTo(InqueryComment o) {
        return this.commNum - o.commNum;
    }
}
