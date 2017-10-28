package com.example.daily.myapplication.Comparator;

import com.example.daily.myapplication.entityClass.Task;

import java.util.Comparator;

public class byPriority implements Comparator<Task> {

    private int method = 0;

    public byPriority(int method) {
        this.method = method;
    }

    @Override
    public int compare(Task o1, Task o2) {
        if(method>0) {
            return o1.getPriority() - o2.getPriority();
        } else {
            return -(o1.getPriority() - o2.getPriority());
        }
    }
}
