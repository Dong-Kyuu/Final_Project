package com.example.jhta_3team_finalproject.mybatis.mapper.Project;

import com.example.jhta_3team_finalproject.domain.Project.Project;
import com.example.jhta_3team_finalproject.domain.Project.ProjectComment;
import com.example.jhta_3team_finalproject.domain.Project.ProjectMember;
import com.example.jhta_3team_finalproject.domain.Project.ProjectPeed;
import com.example.jhta_3team_finalproject.domain.User.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {
    List<String> getAllDepartment();

    List<User> getAllUser(int userNum);

    void createProject(Project project);

    String getMemberName(int userNum);

    String getMemberProfile(int userNum);

    String getMemberDepartment(int userNum);

    String getMemberPosition(int userNum);

    void insertMember(ProjectMember projectMember);

    List<Project> getMyProject(int loginNum);

    Project getProject(int projectNum);

    List<ProjectMember> getProjectMember(int projectNum);

    List<String> getProjectDepartment(int projectNum, int userNum);

    List<ProjectMember> searchProjectMember(int projectNum, String searchWord);

    ProjectMember masterMember(int loginNum);

    void insertPeed(ProjectPeed projectPeed);

    List<ProjectPeed> getProjectPeed(int projectNum);

    int insertComment(ProjectComment projectComment);

    List<ProjectComment> getPeedComment(int projectPeedNum, int projectNum);

    int changeType(int type, int peedNum, int projectNum);

    ProjectPeed getOneProjectPeed(int peedNum, int projectNum);

    ProjectComment getInsertComment(int commentNum);
}
