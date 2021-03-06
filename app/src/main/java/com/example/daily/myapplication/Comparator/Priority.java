package com.example.daily.myapplication.Comparator;

import com.example.daily.myapplication.EntityClass.Task;

import java.util.Comparator;

public class Priority implements Comparator<Task> {

    private boolean method;

    public Priority(boolean method) {
        this.method = method;
    }

    @Override
    public int compare(Task o1, Task o2) {
        if(method) {
            return o1.getPriority() - o2.getPriority();
        } else {
            return -(o1.getPriority() - o2.getPriority());
        }
    }
}
