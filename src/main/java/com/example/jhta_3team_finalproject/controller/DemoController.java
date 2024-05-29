package com.example.jhta_3team_finalproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DemoController {

    @GetMapping("/1")
    public String mainpage1() {
        return "/pages/charts/chartjs";
    }

    @GetMapping("/2")
    public String mainpage2() {
        return "/pages/forms/basic_elements";
    }

    @GetMapping("/3")
    public String mainpage3() {
        return "/pages/icons/mdi";
    }

    @GetMapping("/4")
    public String mainpage4() {
        return "/pages/samples/blank-page";
    }

    @GetMapping("/5")
    public String mainpage5() {
        return "/pages/samples/login";
    }

    @GetMapping("/6")
    public String mainpage6() {
        return "/pages/samples/register";
    }

    @GetMapping("/7")
    public String mainpage7() {
        return "/pages/tables/basic-table";
    }

    @GetMapping("/8")
    public String mainpage8() {
        return "/partials/_navbar";
    }

    @GetMapping("/9")
    public String mainpage9() {
        return "/pages/ui-features/buttons";
    }


    @GetMapping("error/403error")
    public String error_403() {
        return "error/403error";
    }
}