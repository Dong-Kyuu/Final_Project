package com.example.jhta_3team_finalproject.service.calendar;

import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import java.util.List;

public interface CalendarService {

    public List<Calendar> getlist();


    int insert(Calendar calendar);

    int update(Calendar calendar);
}
