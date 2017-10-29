package com.example.daily.myapplication.EntityClass;

import java.io.Serializable;

/**
 * String title, String content, String setTime, String deadLineTime, int priority, int doneFlag
 */
public class Task implements Serializable {
    private String title, content;
    private String setTime, deadLineTime;
    private int priority;
    private int doneFlag;
    private int hashCode = 0;

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", setTime='" + setTime + '\'' +
                ", deadLineTime='" + deadLineTime + '\'' +
                ", priority=" + priority +
                ", doneFlag=" + doneFlag +
                ", hashCode=" + hashCode +
                '}';
    }

    public Task() {
    }

    public Task(String title, String content, String setTime, String deadLineTime, int priority,
                int doneFlag) {
        this.title = title;
        this.content = content;
        this.setTime = setTime;
        this.deadLineTime = deadLineTime;
        this.priority = priority;
        this.doneFlag = doneFlag;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getdeadLineTime() {
        return deadLineTime;
    }

    public void setdeadLineTime(String deadLineTime) {
        this.deadLineTime = deadLineTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDoneFlag() {
        return doneFlag;
    }

    public void setDoneFlag(int doneFlag) {
        this.doneFlag = doneFlag;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }
}
