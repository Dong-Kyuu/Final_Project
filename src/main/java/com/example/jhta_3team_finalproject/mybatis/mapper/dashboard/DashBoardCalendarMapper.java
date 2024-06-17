package com.example.jhta_3team_finalproject.mybatis.mapper.dashboard;

import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashBoardCalendarMapper {

   List<Calendar> select();
}
