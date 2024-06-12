package com.example.jhta_3team_finalproject.mybatis.mapper.calendar;

import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CalendarMapper {

    public List<Calendar> getlist();

    int insert(Calendar calendar);

    int update(Calendar calendar);

    int resize(Calendar calendar);

    int delete(int cal_id);
}
