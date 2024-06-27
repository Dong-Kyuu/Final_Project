package com.example.jhta_3team_finalproject.domain.Board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ViewCheck {

    private int announceBoardNum;
    private int projectNum;
    private int projectPeedNum;
    private int userNum;
    private int view;
    private int check;

    private String userName;
}
