package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import com.example.jhta_3team_finalproject.service.calendar.CalendarService;
import com.example.jhta_3team_finalproject.service.table.TableCommentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/cal")
public class CalendarController {

    private CalendarService cs;

    @Autowired
    public CalendarController(CalendarService cs) {
        this.cs = cs;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String logout() {
        return "calendar/blank-page";
    }

    @ResponseBody
    @RequestMapping(value = "/getjson", method = RequestMethod.GET)
    public List<Calendar> json() {

        return  cs.getlist();
    }


    @RequestMapping(value = "/mycalendar", method = RequestMethod.GET)
    public String test() {
        return "calendar/mycalendar";
    }

}
