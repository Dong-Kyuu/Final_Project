package com.example.jhta_3team_finalproject.service.Project;

import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.Project.Project;
import com.example.jhta_3team_finalproject.domain.Project.ProjectComment;
import com.example.jhta_3team_finalproject.domain.Project.ProjectMember;
import com.example.jhta_3team_finalproject.domain.Project.ProjectPeed;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.Project.ProjectMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.UpfilesMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.AttendenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final UpfilesMapper upfilesMapper;


    @Autowired
    public ProjectServiceImpl(ProjectMapper projectMapper, UpfilesMapper upfilesMapper) {
        this.projectMapper = projectMapper;
        this.upfilesMapper = upfilesMapper;
    }

    @Override
    public List<String> getAllDepartment() {

        List<String> department = projectMapper.getAllDepartment();
        return department;
    }

    @Override
    public List<User> getAllUser(int userNum) {

        return projectMapper.getAllUser(userNum);
    }

    @Transactional
    @Override
    public void createProject(Project project) {
        projectMapper.createProject(project);
    }

    @Override
    public String getUserName(int userNum) {
        return projectMapper.getMemberName(userNum);
    }

    @Override
    public String getUserProfile(int userNum) {
        return projectMapper.getMemberProfile(userNum);
    }

    @Override
    public String getUserDepartment(int userNum) {
        return projectMapper.getMemberDepartment(userNum);
    }

    @Override
    public String getUserPosition(int userNum) {
        return projectMapper.getMemberPosition(userNum);
    }

    @Transactional
    @Override
    public void insertMember(List<ProjectMember> members) {
        for(ProjectMember projectMember : members){
            projectMapper.insertMember(projectMember);
        }
    }

    @Override
    public List<Project> getMyProject(int loginNum) {
        return projectMapper.getMyProject(loginNum);
    }

    @Override
    public Project getProject(int projectNum) {
        return projectMapper.getProject(projectNum);
    }

    @Override
    public List<ProjectMember> getProjectMember(int projectNum) {
        return projectMapper.getProjectMember(projectNum);
    }

    @Override
    public List<String> getProjectDepartment(int projectNum, int userNum) {
        return projectMapper.getProjectDepartment(projectNum, userNum);
    }

    @Override
    public List<ProjectMember> searchProjectMember(int projectNum, String searchWord) {
        return projectMapper.searchProjectMember(projectNum, searchWord);
    }

    @Override
    public ProjectMember MasterMember(int loginNum) {
        return projectMapper.masterMember(loginNum);
    }

    @Override
    public void insertPeed(ProjectPeed projectPeed) {
        projectMapper.insertPeed(projectPeed);
    }

    @Override
    public void insertFile(int projectNum, int projectPeedNum, List<BoardUpfiles> files) {
        for (BoardUpfiles file : files) {
            file.setProjectNum(projectNum);
            file.setProjectPeedNum(projectPeedNum);
            upfilesMapper.insertProjectFile(file);
        }
    }

    @Override
    public List<ProjectPeed> getProjectPeed(int projectNum) {
        return projectMapper.getProjectPeed(projectNum);
    }

    @Override
    public int commentsInsert(ProjectComment projectComment) {
        return projectMapper.insertComment(projectComment);
    }

    @Override
    public List<ProjectComment> getPeedComment(int projectPeedNum, int projectNum) {
        return projectMapper.getPeedComment(projectPeedNum, projectNum);
    }
}
