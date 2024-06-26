package com.example.jhta_3team_finalproject.domain.Board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class BoardUpfiles {

    private int boardNum;
    private int announBoardNum;
    private String upfilesOriginalFileName;
    private String upfilesFileName;
    private String upfilesUploadDate;
    private int projectNum;
    private int projectPeedNum;

}
