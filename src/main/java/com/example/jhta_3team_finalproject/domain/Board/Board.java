package com.example.jhta_3team_finalproject.domain.Board;

import java.util.List;

public class Board {

    private int boardNum;
    private int userNum;
    private String boardTitle;
    private String boardContent;
    private String boardDepartment;
    private String boardReadcount;
    private String boardDate;

    private int Cnt;
    private List<BoardUpfiles> files; // 파일 목록
    private String boardWriter;
    private String writerDepartment;
    private String writerProfilePicture;


    public int getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(int boardNum) {
        this.boardNum = boardNum;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getBoardDepartment() {
        return boardDepartment;
    }

    public void setBoardDepartment(String boardDepartment) {
        this.boardDepartment = boardDepartment;
    }

    public String getBoardReadcount() {
        return boardReadcount;
    }

    public void setBoardReadcount(String boardReadcount) {
        this.boardReadcount = boardReadcount;
    }

    public String getBoardDate() {
        return boardDate;
    }

    public void setBoardDate(String boardDate) {
        this.boardDate = boardDate;
    }

    public int getCnt() {
        return Cnt;
    }

    public void setCnt(int Cnt) {
        this.Cnt = Cnt;
    }

    public List<BoardUpfiles> getFiles() {
        return files;
    }

    public void setFiles(List<BoardUpfiles> files) {
        this.files = files;
    }

    public String getBoardWriter() {
        return boardWriter;
    }

    public void setUserName(String boardWriter) {
        this.boardWriter = boardWriter;
    }

    public void setBoardWriter(String boardWriter) {
        this.boardWriter = boardWriter;
    }

    public String getWriterDepartment() {
        return writerDepartment;
    }

    public void setUserDepartment(String writerDepartment) {
        this.writerDepartment = writerDepartment;
    }

    public String getWriterProfilePicture() {
        return writerProfilePicture;
    }

    public void setUserProfilePicture (String writerProfilePicture) {
        this.writerProfilePicture = writerProfilePicture;
    }

    @Override
    public String toString() {
        return "Board{" +
                "boardNum=" + boardNum +
                ", userNum=" + userNum +
                ", boardTitle='" + boardTitle + '\'' +
                ", boardContent='" + boardContent + '\'' +
                ", boardDepartment='" + boardDepartment + '\'' +
                ", boardReadcount='" + boardReadcount + '\'' +
                ", boardDate='" + boardDate + '\'' +
                ", Cnt=" + Cnt +
                ", files=" + files +
                ", userName='" + boardWriter + '\'' +
                '}';
    }
}
