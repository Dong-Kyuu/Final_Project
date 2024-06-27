package com.example.jhta_3team_finalproject.service.calendar;

import com.example.jhta_3team_finalproject.domain.calendar.Calendar;

import java.util.HashMap;
import java.util.List;

public interface CalendarService {

    public List<Calendar> getlist(HashMap<String,Object> map);

    int insert(Calendar calendar);

    int update(Calendar calendar);

    int resize(Calendar calendar);

    int delete(int cal_id,String user_name);
}
