package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.BoardComment;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;
import com.example.jhta_3team_finalproject.domain.Project.Project;
import com.example.jhta_3team_finalproject.domain.Project.ProjectComment;
import com.example.jhta_3team_finalproject.domain.Project.ProjectMember;
import com.example.jhta_3team_finalproject.domain.Project.ProjectPeed;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import com.example.jhta_3team_finalproject.service.Project.ProjectService;
import com.example.jhta_3team_finalproject.service.S3.S3Service;
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
    private S3Service s3Service;

    @Autowired
    public ProjectController(ProjectService projectService, SseService sseService, S3Service s3Service) {
        this.projectService = projectService;
        this.sseService = sseService;
        this.s3Service = s3Service;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginuser = (User)authentication.getPrincipal();

        if (userNums == null) {
            userNums = new ArrayList<>();
        }
        userNums.add(loginuser.getUserNum());

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

            if(userNum != loginuser.getUserNum()) {
                sseService.sendNotification(userNum, project.getMasterUserNum(), projectService.getUserName(project.getMasterUserNum()),
                        "",
                        "프로젝트 \"" + project.getProjectTitle() + "\"에 초대하셨습니다.");
            }
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
        List<ProjectPeed> projectPeeds = projectService.getProjectPeed(projectNum);

        for (ProjectPeed projectPeed : projectPeeds) {
            List<ProjectComment> comments = projectService.getPeedComment(projectPeed.getProjectPeedNum(), projectPeed.getProjectNum());
            projectPeed.setComments(comments);
        }

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
            mv.addObject("projectPeeds", projectPeeds);

        }
        return mv;
    }

    @PostMapping("/searchCharge")
    @ResponseBody
    public Map<String, Object> searchCharge(int loginNum, int projectNum, String searchWord) {

        Map<String, Object> response = new HashMap<>();
        List<ProjectMember> members = projectService.searchProjectMember(projectNum, searchWord);

        response.put("status", "success");
        response.put("members", members);

        return response;
    }

    // 작성 글 DB 등록
    @PostMapping("/addPeed")
    public String add(ProjectPeed projectPeed,
                      @RequestParam("uploadfile[]") MultipartFile[] uploadfiles)
            throws Exception {

        if (projectPeed.getProjectPeedStartPeriod() != null && projectPeed.getProjectPeedStartPeriod().isEmpty()) {
            projectPeed.setProjectPeedStartPeriod(null);
        }
        if (projectPeed.getProjectPeedEndPeriod() != null && projectPeed.getProjectPeedEndPeriod().isEmpty()) {
            projectPeed.setProjectPeedEndPeriod(null);
        }
        // Board 객체를 먼저 저장하고, BOARD_NUM을 받아옵니다.
        projectService.insertPeed(projectPeed); // Board 객체 저장
        int ProjectPeedNum = projectPeed.getProjectPeedNum(); // 저장된 BOARD_NUM 가져오기

        List<BoardUpfiles> files = new ArrayList<>();

        for (MultipartFile uploadfile : uploadfiles) {
            if (!uploadfile.isEmpty()) {

                String fileUrl = s3Service.uploadFile(uploadfile);
                logger.info("Uploaded file URL: " + fileUrl);

                BoardUpfiles file = new BoardUpfiles();
                file.setUpfilesOriginalFileName(uploadfile.getOriginalFilename());
                file.setUpfilesFileName(fileUrl); // S3 URL로 설정
                files.add(file);
            }
        }

        logger.info("프로젝트 피드 넘버 = " + ProjectPeedNum);

        projectService.insertFile(projectPeed.getProjectNum(), ProjectPeedNum, files); // 저장메서드 호출

        return "redirect:mainProject?projectNum="+projectPeed.getProjectNum();

    }

    @ResponseBody
    @PostMapping(value = "/commentAdd")
    public int CommentAdd(ProjectComment projectComment, int peedWriter) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginuser = (User)authentication.getPrincipal();

        sseService.sendNotification(peedWriter, loginuser.getUserNum(), loginuser.getUsername(),
                "http://localhost:9000/project/mainProject?projectNum=" + projectComment.getProjectNum() + "#" + projectComment.getProjectPeedNum(),
                "No." + projectComment.getProjectPeedNum() + "피드에 댓글을 남겼어요.");

        return projectService.commentsInsert(projectComment);
    }

    @GetMapping("/t")
    public String test_table() {
        return "/Project/projectpeed";
    }

}
