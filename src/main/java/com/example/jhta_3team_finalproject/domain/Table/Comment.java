package com.example.jhta_3team_finalproject.domain.Table;

public class Comment {

    private int comm_num;

    private String comm_id;
    private String comm_name;
    private String comm_profile;

    private String comm_content;
    private String comm_regdate;
    private int board_num;

    public int getComm_num() {
        return comm_num;
    }

    public void setComm_num(int comm_num) {
        this.comm_num = comm_num;
    }

    public String getComm_id() {
        return comm_id;
    }

    public void setComm_id(String comm_id) {
        this.comm_id = comm_id;
    }

    public String getComm_name() {
        return comm_name;
    }

    public void setComm_name(String comm_name) {
        this.comm_name = comm_name;
    }

    public String getComm_profile() {
        return comm_profile;
    }

    public void setComm_profile(String comm_profile) {
        this.comm_profile = comm_profile;
    }

    public String getComm_content() {
        return comm_content;
    }

    public void setComm_content(String comm_content) {
        this.comm_content = comm_content;
    }

    public String getComm_regdate() {
        return comm_regdate;
    }

    public void setComm_regdate(String comm_regdate) {
        this.comm_regdate = comm_regdate;
    }

    public int getBoard_num() {
        return board_num;
    }

    public void setBoard_num(int board_num) {
        this.board_num = board_num;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comm_num=" + comm_num +
                ", comm_id='" + comm_id + '\'' +
                ", comm_name='" + comm_name + '\'' +
                ", comm_profile='" + comm_profile + '\'' +
                ", comm_content='" + comm_content + '\'' +
                ", comm_regdate='" + comm_regdate + '\'' +
                ", board_num=" + board_num +
                '}';
    }
}
