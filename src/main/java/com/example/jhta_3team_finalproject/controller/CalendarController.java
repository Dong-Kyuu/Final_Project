package com.example.jhta_3team_finalproject.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cal")
public class CalendarController {

    private final HttpServletResponse httpServletResponse;

    public CalendarController(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String logout() {
        return "calendar/blank-page";
    }

    @ResponseBody
    @RequestMapping(value = "/getjson", method = RequestMethod.GET)
    public String json() {
        return "[\n" +
                "  {\n" +
                "    \"_id\": 1,\n" +
                "    \"title\": \"그리스 여행\",\n" +
                "    \"description\": \"그리스 여행은 즐거워\",\n" +
                "    \"start\": \"2024-04-01T09:30\",\n" +
                "    \"end\": \"2024-04-07T15:00\",\n" +
                "    \"type\": \"ISTP\",\n" +
                "    \"username\": \"여행 종료\",\n" +
                "    \"backgroundColor\": \"#D25565\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 2,\n" +
                "    \"title\": \"일본 여행\",\n" +
                "    \"description\": \"일본 여행은 가격이 저렴하다!\",\n" +
                "    \"start\": \"2024-04-01T12:30\",\n" +
                "    \"end\": \"2024-04-01T15:30\",\n" +
                "    \"type\": \"ENTJ\",\n" +
                "    \"username\": \"여행 종료\",\n" +
                "    \"backgroundColor\": \"#9775fa\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 3,\n" +
                "    \"title\": \"제주도 여행\",\n" +
                "    \"description\": \"제주도는 감귤이 맛있지\",\n" +
                "    \"start\": \"2024-04-12\",\n" +
                "    \"end\": \"2024-04-12\",\n" +
                "    \"type\": \"ESFJ\",\n" +
                "    \"username\": \"계획 완료\",\n" +
                "    \"backgroundColor\": \"#ffa94d\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 4,\n" +
                "    \"title\": \"프랑스 여행\",\n" +
                "    \"description\": \"디즈니랜드\",\n" +
                "    \"start\": \"2024-04-06\",\n" +
                "    \"end\": \"2024-04-06\",\n" +
                "    \"type\": \"INFP\",\n" +
                "    \"username\": \"여행 종료\",\n" +
                "    \"backgroundColor\": \"#74c0fc\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 5,\n" +
                "    \"title\": \"이탈리아 여행\",\n" +
                "    \"description\": \"시장투어with쿠킹클래스\",\n" +
                "    \"start\": \"2024-04-08\",\n" +
                "    \"end\": \"2024-04-08\",\n" +
                "    \"type\": \"ESFJ\",\n" +
                "    \"username\": \"계획 중\",\n" +
                "    \"backgroundColor\": \"#f06595\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 6,\n" +
                "    \"title\": \"독일 여행\",\n" +
                "    \"description\": \"콜로세움,포로로마노 역사투어\",\n" +
                "    \"start\": \"2024-04-14\",\n" +
                "    \"end\": \"2024-04-14\",\n" +
                "    \"type\": \"ENTJ\",\n" +
                "    \"username\": \"계획 완료\",\n" +
                "    \"backgroundColor\": \"#63e6be\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 7,\n" +
                "    \"title\": \"휴가 여행\",\n" +
                "    \"description\": \"무라노&부라노섬 투어\",\n" +
                "    \"start\": \"2024-04-18\",\n" +
                "    \"end\": \"2024-04-20\",\n" +
                "    \"type\": \"INFP\",\n" +
                "    \"username\": \"계획 중\",\n" +
                "    \"backgroundColor\": \"#a9e34b\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 8,\n" +
                "    \"title\": \"헝가리 여행\",\n" +
                "    \"description\": \"융프라우요흐 투어\",\n" +
                "    \"start\": \"2024-04-24T09:00\",\n" +
                "    \"end\": \"2024-04-24T10:00\",\n" +
                "    \"type\": \"ESFJ\",\n" +
                "    \"username\": \"계획 완료\",\n" +
                "    \"backgroundColor\": \"#4d638c\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 9,\n" +
                "    \"title\": \"스위스 여행\",\n" +
                "    \"description\": \"패러글라이딩\",\n" +
                "    \"start\": \"2024-04-28\",\n" +
                "    \"end\": \"2024-04-31\",\n" +
                "    \"type\": \"ESFJ\",\n" +
                "    \"username\": \"계획 중\",\n" +
                "    \"backgroundColor\": \"#495057\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"_id\": 10,\n" +
                "    \"title\": \"미국 여행\",\n" +
                "    \"description\": \"아메리카 드림\",\n" +
                "    \"start\": \"2024-04-15\",\n" +
                "    \"end\": \"2024-04-22\",\n" +
                "    \"type\": \"INFP\",\n" +
                "    \"username\": \"계획 중\",\n" +
                "    \"backgroundColor\": \"#9775fa\",\n" +
                "    \"textColor\": \"#ffffff\",\n" +
                "    \"allDay\": true\n" +
                "  }\n" +
                "]";
    }



}

