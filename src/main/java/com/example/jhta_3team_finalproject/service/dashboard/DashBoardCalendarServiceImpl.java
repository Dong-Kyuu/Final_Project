package com.example.jhta_3team_finalproject.service.dashboard;

import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import com.example.jhta_3team_finalproject.mybatis.mapper.dashboard.DashBoardCalendarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DashBoardCalendarServiceImpl implements DashBoardCalendarService {

    private DashBoardCalendarMapper dao;

    @Autowired
    public DashBoardCalendarServiceImpl(DashBoardCalendarMapper dao){
        this.dao= dao;
    }

    @Override
    public List<Calendar> select() {
      return dao.select();
    }
}
