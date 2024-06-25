package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import com.example.jhta_3team_finalproject.service.calendar.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        return "calendar/calendar-page";
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

    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public int insert(Calendar calendar, @AuthenticationPrincipal User user) {

        calendar.setUsername(user.getUsername());

        return cs.insert(calendar);
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public int update(Calendar calendar, @AuthenticationPrincipal User user) {

        calendar.setUsername(user.getUsername());

        return cs.update(calendar);
    }

    @ResponseBody
    @RequestMapping(value = "/resize", method = RequestMethod.GET)
    public int resize(Calendar calendar, @AuthenticationPrincipal User user) {

        calendar.setUsername(user.getUsername());

        return cs.resize(calendar);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public int delete(int cal_id, @AuthenticationPrincipal User user) {

        String user_name = user.getUsername();

        return cs.delete(cal_id,user_name);
    }

    







}
