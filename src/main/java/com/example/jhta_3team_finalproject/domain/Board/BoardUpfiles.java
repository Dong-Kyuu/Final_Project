package com.example.jhta_3team_finalproject.domain.Board;

public class BoardUpfiles {

    private int boardNum;
    private int announBoardNum;
    private String upfilesOriginalFileName;
    private String upfilesFileName;
    private String upfilesUploadDate;

    public int getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(int boardNum) {
        this.boardNum = boardNum;
    }

    public int getAnnounBoardNum() {
        return announBoardNum;
    }

    public void setAnnounBoardNum(int announBoardNum) {
        this.announBoardNum = announBoardNum;
    }

    public String getUpfilesOriginalFileName() {
        return upfilesOriginalFileName;
    }

    public void setUpfilesOriginalFileName(String upfilesOriginalFileName) {
        this.upfilesOriginalFileName = upfilesOriginalFileName;
    }

    public String getUpfilesFileName() {
        return upfilesFileName;
    }

    public void setUpfilesFileName(String upfilesFileName) {
        this.upfilesFileName = upfilesFileName;
    }

    public String getUpfilesUploadDate() {
        return upfilesUploadDate;
    }

    public void setUpfilesUploadDate(String upfilesUploadDate) {
        this.upfilesUploadDate = upfilesUploadDate;
    }

    @Override
    public String toString() {
        return "BoardUpfiles{" +
                "boardNum=" + boardNum +
                ", announBoardNum=" + announBoardNum +
                ", upfilesOriginalFileName='" + upfilesOriginalFileName + '\'' +
                ", upfilesFileName='" + upfilesFileName + '\'' +
                ", upfilesUploadDate='" + upfilesUploadDate + '\'' +
                '}';
    }

}
