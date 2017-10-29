package com.example.daily.myapplication.Comparator;

import com.example.daily.myapplication.EntityClass.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Time implements Comparator<Task> {

    //setOrDead > 0 means setTime
    private int setOrDead = 0;
    private boolean method;

    public Time(int setOrDead, boolean method) {
        this.setOrDead = setOrDead;
        this.method = method;
    }

    @Override
    public int compare(Task o1, Task o2) {
        if (setOrDead > 0) {
            if (method) {
                return getDate(o1.getSetTime()).compareTo(getDate(o2.getSetTime()));
            } else {
                return -(getDate(o1.getSetTime()).compareTo(getDate(o2.getSetTime())));
            }
        } else {
            if (method) {
                return getDate(o1.getdeadLineTime()).compareTo(getDate(o2.getdeadLineTime()));
            } else {
                return -(getDate(o1.getdeadLineTime()).compareTo(getDate(o2.getdeadLineTime())));
            }
        }
    }

    private Date getDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
