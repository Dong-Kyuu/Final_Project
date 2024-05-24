package com.example.jhta_3team_finalproject.domain.board;

import org.springframework.web.multipart.MultipartFile;

public class Board {

    private String BOARD_NAME;
    private String BOARD_PASS;
    private String BOARD_SUBJECT;
    private String BOARD_CONTENT;
    private String BOARD_FILE;
    private int BOARD_NUM;
    private int BOARD_RE_REF;
    private int BOARD_RE_LEV;
    private int BOARD_RE_SEQ;
    private int BOARD_READCOUNT;
    private MultipartFile uploadfile;

    private String BOARD_ORIGINAL;
    private String BOARD_DATE;

    private int CNT;

    public String getBOARD_NAME() {
        return BOARD_NAME;
    }

    public void setBOARD_NAME(String BOARD_NAME) {
        this.BOARD_NAME = BOARD_NAME;
    }

    public String getBOARD_PASS() {
        return BOARD_PASS;
    }

    public void setBOARD_PASS(String BOARD_PASS) {
        this.BOARD_PASS = BOARD_PASS;
    }

    public String getBOARD_SUBJECT() {
        return BOARD_SUBJECT;
    }

    public void setBOARD_SUBJECT(String BOARD_SUBJECT) {
        this.BOARD_SUBJECT = BOARD_SUBJECT;
    }

    public String getBOARD_CONTENT() {
        return BOARD_CONTENT;
    }

    public void setBOARD_CONTENT(String BOARD_CONTENT) {
        this.BOARD_CONTENT = BOARD_CONTENT;
    }

    public String getBOARD_FILE() {
        return BOARD_FILE;
    }

    public void setBOARD_FILE(String BOARD_FILE) {
        this.BOARD_FILE = BOARD_FILE;
    }

    public int getBOARD_NUM() {
        return BOARD_NUM;
    }

    public void setBOARD_NUM(int BOARD_NUM) {
        this.BOARD_NUM = BOARD_NUM;
    }

    public int getBOARD_RE_REF() {
        return BOARD_RE_REF;
    }

    public void setBOARD_RE_REF(int BOARD_RE_REF) {
        this.BOARD_RE_REF = BOARD_RE_REF;
    }

    public int getBOARD_RE_LEV() {
        return BOARD_RE_LEV;
    }

    public void setBOARD_RE_LEV(int BOARD_RE_LEV) {
        this.BOARD_RE_LEV = BOARD_RE_LEV;
    }

    public int getBOARD_RE_SEQ() {
        return BOARD_RE_SEQ;
    }

    public void setBOARD_RE_SEQ(int BOARD_RE_SEQ) {
        this.BOARD_RE_SEQ = BOARD_RE_SEQ;
    }

    public int getBOARD_READCOUNT() {
        return BOARD_READCOUNT;
    }

    public void setBOARD_READCOUNT(int BOARD_READCOUNT) {
        this.BOARD_READCOUNT = BOARD_READCOUNT;
    }

    public MultipartFile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }

    public String getBOARD_ORIGINAL() {
        return BOARD_ORIGINAL;
    }

    public void setBOARD_ORIGINAL(String BOARD_ORIGINAL) {
        this.BOARD_ORIGINAL = BOARD_ORIGINAL;
    }

    public String getBOARD_DATE() {
        return BOARD_DATE;
    }

    public void setBOARD_DATE(String BOARD_DATE) {
        this.BOARD_DATE = BOARD_DATE;
    }

    public int getCNT() {
        return CNT;
    }

    public void setCNT(int CNT) {
        this.CNT = CNT;
    }

    @Override
    public String toString() {
        return "Freeboard{" +
                "BOARD_NAME='" + BOARD_NAME + '\'' +
                ", BOARD_PASS='" + BOARD_PASS + '\'' +
                ", BOARD_SUBJECT='" + BOARD_SUBJECT + '\'' +
                ", BOARD_CONTENT='" + BOARD_CONTENT + '\'' +
                ", BOARD_FILE='" + BOARD_FILE + '\'' +
                ", BOARD_NUM=" + BOARD_NUM +
                ", BOARD_RE_REF=" + BOARD_RE_REF +
                ", BOARD_RE_LEV=" + BOARD_RE_LEV +
                ", BOARD_RE_SEQ=" + BOARD_RE_SEQ +
                ", BOARD_READCOUNT=" + BOARD_READCOUNT +
                ", uploadfile=" + uploadfile +
                ", BOARD_ORIGINAL='" + BOARD_ORIGINAL + '\'' +
                ", BOARD_DATE='" + BOARD_DATE + '\'' +
                ", CNT=" + CNT +
                '}';
    }
}
