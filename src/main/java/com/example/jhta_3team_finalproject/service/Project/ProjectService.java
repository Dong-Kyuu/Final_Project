package com.example.jhta_3team_finalproject.service.Project;

import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.Project.Project;
import com.example.jhta_3team_finalproject.domain.Project.ProjectComment;
import com.example.jhta_3team_finalproject.domain.Project.ProjectMember;
import com.example.jhta_3team_finalproject.domain.Project.ProjectPeed;
import com.example.jhta_3team_finalproject.domain.User.User;

import java.util.List;

public interface ProjectService {
    List<String> getAllDepartment();

    List<User> getAllUser(int userNum);

    void createProject(Project project);

    String getUserName(int userNum);

    String getUserProfile(int userNum);

    String getUserDepartment(int userNum);

    String getUserPosition(int userNum);

    void insertMember(List<ProjectMember> members);

    List<Project> getMyProject(int loginNum);

    Project getProject(int projectNum);

    List<ProjectMember> getProjectMember(int projectNum);

    List<String> getProjectDepartment(int projectNum, int userNum);

    List<ProjectMember> searchProjectMember(int projectNum, String searchWord);

    ProjectMember MasterMember(int loginNum);

    void insertPeed(ProjectPeed projectPeed);

    void insertFile(int projectNum, int projectPeedNum, List<BoardUpfiles> files);

    List<ProjectPeed> getProjectPeed(int projectNum);

    int commentsInsert(ProjectComment projectComment);

    List<ProjectComment> getPeedComment(int projectPeedNum, int projectNum);
}
