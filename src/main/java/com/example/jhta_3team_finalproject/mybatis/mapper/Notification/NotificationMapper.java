package com.example.jhta_3team_finalproject.mybatis.mapper.Notification;

import com.example.jhta_3team_finalproject.domain.Notification.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationMapper {
    List<Notification> getList(int userNum);

    void insert(Notification alarm);

    int readAction(int notifiNum);
}
