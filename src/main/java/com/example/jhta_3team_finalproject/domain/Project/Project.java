package com.example.jhta_3team_finalproject.domain.Project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Project {

    private int projectNum;
    private String projectTitle;
    private String projectIntroduce;
    private int masterUserNum;
    private Date projectRegdate;
    private String projectStartPeriod;
    private String projectEndPeriod;

}
