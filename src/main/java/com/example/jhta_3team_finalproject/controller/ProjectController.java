package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.Project.Project;
import com.example.jhta_3team_finalproject.domain.Project.ProjectMember;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import com.example.jhta_3team_finalproject.service.Project.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/project")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private ProjectService projectService;
    private SseService sseService;

    @Autowired
    public ProjectController(ProjectService projectService, SseService sseService) {
        this.projectService = projectService;
        this.sseService = sseService;
    }

    @GetMapping("/createproject")
    public ModelAndView createForm(ModelAndView mv) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginuser = (User)authentication.getPrincipal();
        List<String> department =  projectService.getAllDepartment();
        List<User> Users = projectService.getAllUser(loginuser.getUserNum());

        mv.setViewName("Project/projectcreate");
        mv.addObject("department", department);
        mv.addObject("Users", Users);
        return mv;
    }

    @PostMapping("/add")
    public String createProject(Project project,
                                @RequestParam(required = false) List<Integer> userNums, HttpServletRequest request) {

        if (project.getProjectEndPeriod() != null && project.getProjectEndPeriod().isEmpty()) {
            project.setProjectEndPeriod(null);
        }

        projectService.createProject(project);
        int projectNum = project.getProjectNum();
        System.out.println("Generated Project Num: " + projectNum);

        List<ProjectMember> Members = new ArrayList<>();

        for(int userNum : userNums) {
            String memberName = projectService.getUserName(userNum);
            String memberProfile = projectService.getUserProfile(userNum);
            String memberDepartment = projectService.getUserDepartment(userNum);
            String memberPosition = projectService.getUserPosition(userNum);

            ProjectMember member = new ProjectMember();
            member.setProjectNum(projectNum);
            member.setProjectMemberNum(userNum);
            member.setProjectMemberName(memberName);
            member.setProjectMemberProfile(memberProfile);
            member.setProjectMemberDepartment(memberDepartment);
            member.setProjectMemberPosition(memberPosition);
            Members.add(member);

            sseService.sendNotification(userNum, project.getMasterUserNum(), projectService.getUserName(project.getMasterUserNum()),
                    "",
                    "프로젝트 \"" + project.getProjectTitle() + "\"에 초대하셨습니다.");
        }

        projectService.insertMember(Members);

        return "redirect:mainProject?projectNum="+projectNum;
    }

    @PostMapping("/myproject")
    @ResponseBody
    public Map<String, Object> myProject(int loginNum) {

        Map<String, Object> response = new HashMap<>();

        List<Project> projects = projectService.getMyProject(loginNum);
        response.put("status", "success");
        response.put("projects", projects);

        return response;
    }

    @GetMapping("/mainProject")
    public ModelAndView Detail(
            ModelAndView mv, int projectNum,
            HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginuser = (User)authentication.getPrincipal();

        Project project = projectService.getProject(projectNum);
        List<ProjectMember> members = projectService.getProjectMember(projectNum);
        List<String> department =  projectService.getProjectDepartment(projectNum, loginuser.getUserNum());

        if (project == null) {
            logger.info("상세보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "실패하였습니다.");
        } else {
            logger.info("상세보기 성공");

            mv.setViewName("Project/projectpeed");

            mv.addObject("project", project);
            mv.addObject("members", members);
            mv.addObject("department", department);

        }
        return mv;
    }

    @GetMapping("/t")
    public String test_table() {
        return "/Project/projectpeed";
    }

}
