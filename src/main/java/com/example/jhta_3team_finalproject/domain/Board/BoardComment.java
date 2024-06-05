package com.example.jhta_3team_finalproject.domain.Board;

public class BoardComment {

    private int commNum;
    private String commContent;
    private String commRegdate;
    private int commLevel;
    private int userNum;
    private int boardNum;

    private String commWriter;
    private String writerDepartment;
    private String writerProfilePicture;


    public int getCommNum() {
        return commNum;
    }

    public void setCommNum(int commNum) {
        this.commNum = commNum;
    }

    public String getCommContent() {
        return commContent;
    }

    public void setCommContent(String commContent) {
        this.commContent = commContent;
    }

    public String getCommRegdate() {
        return commRegdate;
    }

    public void setCommRegdate(String commRegdate) {
        this.commRegdate = commRegdate;
    }

    public int getCommLevel() {
        return commLevel;
    }

    public void setCommLevel(int commLevel) {
        this.commLevel = commLevel;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(int boardNum) {
        this.boardNum = boardNum;
    }

    public String getCommWriter() {
        return commWriter;
    }

    public void setUserName(String commWriter) {
        this.commWriter = commWriter;
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

    public void setUserProfilePicture(String writerProfilePicture) {
        this.writerProfilePicture = writerProfilePicture;
    }

    @Override
    public String toString() {
        return "BoardComment{" +
                "commNum=" + commNum +
                ", commContent='" + commContent + '\'' +
                ", commRegdate='" + commRegdate + '\'' +
                ", commLevel=" + commLevel +
                ", userNum=" + userNum +
                ", boardNum=" + boardNum +
                ", commWriter='" + commWriter + '\'' +
                ", writerDepartment='" + writerDepartment + '\'' +
                ", writerProfilePicture='" + writerProfilePicture + '\'' +
                '}';
    }

}
