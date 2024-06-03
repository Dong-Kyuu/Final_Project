package com.example.jhta_3team_finalproject.domain.inquery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Getter
@Setter
public class InqueryBoard {
    private int inqNum; // 글번호
    private String inqName; // 글 작성자
    private String inqPass; // 글 비밀번호
    private boolean inqPassExist; // 글 비밀번호가 있는지 2024-04-04, 글 비밀번호가 있다면 true
    private String inqSubject; // 글 제목
    private String inqContent; // 글 내용
    private String inqFile; // 첨부될 파일의 이름
    private int inqReRef; // 답변 글 작성 시 참조되는 글의 번호
    private int inqReLev; // 답변 글의 깊이
    private int inqReSeq; // 답변 글의 순서
    private int inqReadCount; // 글의 조회수

    private MultipartFile uploadfile;

    private String inqOriginal; // 첨부될 파일의 이름
    private String inqDate;

    private int cnt;

}
