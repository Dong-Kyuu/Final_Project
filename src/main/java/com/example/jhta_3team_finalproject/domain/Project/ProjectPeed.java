package com.example.jhta_3team_finalproject.domain.Project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

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
    private String    projectPeedStartPeriod;
    private String    projectPeedEndPeriod;
    private int     projectPeedCharge;
    private int     projectPeedPriority;
    private Date    projectPeedRegdate;
    private int     projectPeedDisclosure;

    private String writerName;
    private String writerDepartment;
    private String writerPosition;
    private String writerProfile;

    private String chargerName;
    private String chargerPosition;

    private List<ProjectComment> comments;

    public Date getProjectPeedStartPeriodDate() {
        return projectPeedStartPeriod == null ? null : new Date(projectPeedStartPeriod);
    }

    public Date getProjectPeedEndPeriodDate() {
        return projectPeedEndPeriod == null ? null : new Date(projectPeedEndPeriod);
    }
}
