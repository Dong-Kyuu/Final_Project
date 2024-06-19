package com.example.jhta_3team_finalproject.domain.Project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ProjectPeed {

    private int     projectPeedNum;
    private int     projectNum;
    private int     projectMemberNum;
    private String  projectPeedTitle;
    private String  projectPeedContent;
    private int     projectPeedType;
    private Date    projectPeedStartPeriod;
    private Date    projectPeedEndPeriod;
    private int     projectPeedCharge;
    private int     projectPeedPriority;
    private Date    projectPeedRegdate;

}
