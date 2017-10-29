package com.example.daily.myapplication.Comparator;

import com.example.daily.myapplication.EntityClass.Task;

import java.util.Comparator;

public class DoneFlag implements Comparator<Task> {

    private boolean method;

    public DoneFlag(boolean method) {
        this.method = method;
    }

    @Override
    public int compare(Task o1, Task o2) {
        if(method) {
            return o1.getDoneFlag() - o2.getDoneFlag();
        } else {
            return -(o1.getDoneFlag() - o2.getDoneFlag());
        }
    }
}
