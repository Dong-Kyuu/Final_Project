package com.example.jhta_3team_finalproject.domain.Table;

import java.util.List;

public class Board {

    private String BOARD_NAME;
    private String BOARD_SUBJECT;
    private String BOARD_CONTENT;
    private int BOARD_NUM;
    private int BOARD_RE_REF;
    private int BOARD_RE_LEV;
    private int BOARD_RE_SEQ;
    private int BOARD_READCOUNT;
    private String BOARD_DATE;
    private int CNT;

    private List<Table_Files> files; // 파일 목록

    public String getBOARD_NAME() {
        return BOARD_NAME;
    }

    public void setBOARD_NAME(String BOARD_NAME) {
        this.BOARD_NAME = BOARD_NAME;
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

    public List<Table_Files> getFiles() {
        return files;
    }

    public void setFiles(List<Table_Files> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "Board{" +
                "BOARD_NAME='" + BOARD_NAME + '\'' +
                ", BOARD_SUBJECT='" + BOARD_SUBJECT + '\'' +
                ", BOARD_CONTENT='" + BOARD_CONTENT + '\'' +
                ", BOARD_NUM=" + BOARD_NUM +
                ", BOARD_RE_REF=" + BOARD_RE_REF +
                ", BOARD_RE_LEV=" + BOARD_RE_LEV +
                ", BOARD_RE_SEQ=" + BOARD_RE_SEQ +
                ", BOARD_READCOUNT=" + BOARD_READCOUNT +
                ", BOARD_DATE='" + BOARD_DATE + '\'' +
                ", CNT=" + CNT +
                ", files=" + files +
                '}';
    }
}
