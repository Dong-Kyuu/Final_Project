package com.example.jhta_3team_finalproject.domain.inquery;

import org.springframework.web.multipart.MultipartFile;

public class InqueryBoard {
    private int inq_num; // 글번호
    private String inq_name; // 글 작성자
    private String inq_pass; // 글 비밀번호
    private boolean inq_pass_exist; // 글 비밀번호가 있는지 2024-04-04, 글 비밀번호가 있다면 true
    private String inq_subject; // 글 제목
    private String inq_content; // 글 내용
    private String inq_file; // 첨부될 파일의 이름
    private int inq_re_ref; // 답변 글 작성 시 참조되는 글의 번호
    private int inq_re_lev; // 답변 글의 깊이
    private int inq_re_seq; // 답변 글의 순서
    private int inq_readcount; // 글의 조회수

    private MultipartFile uploadfile;

    private String inq_original; // 첨부될 파일의 이름
    private String inq_date;

    private int cnt;

    public String getInq_original() {
        return inq_original;
    }

    public void setInq_original(String inq_original) {
        this.inq_original = inq_original;
    }

    public MultipartFile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }
// 이곳에서 오른쪽 마우스 버튼 클릭 후 -> Soruce
    // -> Generate Getters and Setters (alt + shift + s)

    public boolean isInq_pass_exist() {
        return inq_pass_exist;
    }

    public void setInq_pass_exist(boolean inq_pass_exist) {
        this.inq_pass_exist = inq_pass_exist;
    }

    public int getInq_num() {
        return inq_num;
    }

    public void setInq_num(int inq_num) {
        this.inq_num = inq_num;
    }

    public String getInq_name() {
        return inq_name;
    }

    public void setInq_name(String inq_name) {
        this.inq_name = inq_name;
    }

    public String getInq_pass() {
        return inq_pass;
    }

    public void setInq_pass(String inq_pass) {
        this.inq_pass = inq_pass;
    }

    public String getInq_subject() {
        return inq_subject;
    }

    public void setInq_subject(String inq_subject) {
        this.inq_subject = inq_subject;
    }

    public String getInq_content() {
        return inq_content;
    }

    public void setInq_content(String inq_content) {
        this.inq_content = inq_content;
    }

    public String getInq_file() {
        return inq_file;
    }

    public void setInq_file(String inq_file) {
        this.inq_file = inq_file;
    }

    public int getInq_re_ref() {
        return inq_re_ref;
    }

    public void setInq_re_ref(int inq_re_ref) {
        this.inq_re_ref = inq_re_ref;
    }

    public int getInq_re_lev() {
        return inq_re_lev;
    }

    public void setInq_re_lev(int inq_re_lev) {
        this.inq_re_lev = inq_re_lev;
    }

    public int getInq_re_seq() {
        return inq_re_seq;
    }

    public void setInq_re_seq(int inq_re_seq) {
        this.inq_re_seq = inq_re_seq;
    }

    public int getInq_readcount() {
        return inq_readcount;
    }

    public void setInq_readcount(int inq_readcount) {
        this.inq_readcount = inq_readcount;
    }

    public String getInq_date() {
        return inq_date;
    }

    public void setInq_date(String inq_date) {
        this.inq_date = inq_date;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "InqueryBoard{" +
                "inq_num=" + inq_num +
                ", inq_name='" + inq_name + '\'' +
                ", inq_pass='" + inq_pass + '\'' +
                ", inq_pass_exist=" + inq_pass_exist +
                ", inq_subject='" + inq_subject + '\'' +
                ", inq_content='" + inq_content + '\'' +
                ", inq_file='" + inq_file + '\'' +
                ", inq_re_ref=" + inq_re_ref +
                ", inq_re_lev=" + inq_re_lev +
                ", inq_re_seq=" + inq_re_seq +
                ", inq_readcount=" + inq_readcount +
                ", uploadfile=" + uploadfile +
                ", inq_original='" + inq_original + '\'' +
                ", inq_date='" + inq_date + '\'' +
                ", cnt=" + cnt +
                '}';
    }
}
