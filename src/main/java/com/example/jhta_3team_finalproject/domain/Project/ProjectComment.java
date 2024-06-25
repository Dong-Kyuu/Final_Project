package com.example.jhta_3team_finalproject.domain.Project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectComment {

    private int projectCommentNum;
    private int projectNum;
    private int projectPeedNum;
    private int projectMemberNum;
    private String projectCommentContent;
    private String projectCommentRegdate;

    private String commWriter;
    private String commWriterProfile;
    private String commWriterDepartment;
    private String commWriterPosition;

}
