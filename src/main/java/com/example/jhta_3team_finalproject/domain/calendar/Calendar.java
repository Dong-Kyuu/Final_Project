package com.example.jhta_3team_finalproject.domain.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
public class Calendar  {
    private int _id;
    private int id;
    private String title;
    private String description;
    private String start;
    private String end;
    private String type;
    private String backgroundColor;
    private String textColor;
    private boolean allDay;
    private String username;

    public void set_id(int id){
        this._id = id;
        this.id= id;
    }
    public int get_id(){
        return id;
    }

    public void setId(int id){
        this._id = id;
        this.id = id;
    }
}
