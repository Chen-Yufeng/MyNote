package com.example.daily.myapplication.Comparator;

import com.example.daily.myapplication.entityClass.Task;

import java.util.Comparator;

public class byPriority implements Comparator {

    private int method = 0;

    public byPriority(int method) {
        this.method = method;
    }


    @Override
    public int compare(Object o1, Object o2) {
        Task task1 = (Task)o1;
        Task task2 = (Task)o2;

        if(method>0) {
            return task1.getPriority() - task2.getPriority();
        } else {
            return -(task1.getPriority() - task2.getPriority());
        }
    }
}
