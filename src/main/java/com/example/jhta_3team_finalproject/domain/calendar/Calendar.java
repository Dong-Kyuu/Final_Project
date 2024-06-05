package com.example.jhta_3team_finalproject.domain.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
public class Calendar implements Serializable {
    private int _id;
    transient private int id;
    private String title;
    private String description;
    private String start;
    private String end;
    private String type;
    private String backgroundColor;
    private String textColor;
    private boolean allDay;
    private String username;

    public void set_id(){
        this._id = id;
    }
    public int get_id(){
        return id;
    }
}
