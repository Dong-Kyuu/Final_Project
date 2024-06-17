package com.example.jhta_3team_finalproject.domain.Board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@Setter
@Getter
public class AnnounceBoard {

    private int annboardNum;
    private int userNum;
    private String annboardTitle;
    private String annboardContent;
    private String annboardDepartment;
    private String annboardReadcount;
    private Date annboardDate;
    private int annboardImportance;
    private int annboardFix;
    private int annboardRequest;

    private String boardWriter;
    private String writerDepartment;
    private String writerProfilePicture;
    private String writerPosition;



    public void setUserName(String boardWriter) {
        this.boardWriter = boardWriter;
    }

    public void setUserDepartment(String writerDepartment) {
        this.writerDepartment = writerDepartment;
    }

    public void setUserProfilePicture (String writerProfilePicture) {
        this.writerProfilePicture = writerProfilePicture;
    }

    public void setUserPosition (String writerPosition) {
        this.writerPosition = writerPosition;
    }
}
