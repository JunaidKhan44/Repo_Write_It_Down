package com.theswiftvision.writeitdown.ModelClasses;

public class TaskList {

    String mTask;
    int mAddTask, mDeleteTask, count;

    public TaskList(String mTask, int mAddTask, int mDeleteTask) {
        this.mTask = mTask;
        this.mAddTask = mAddTask;
        this.mDeleteTask = mDeleteTask;
    }

    public TaskList(int count) {
        this.count = count;
    }

    public TaskList(String mTask) {
        this.mTask = mTask;
    }

    public String getmTask() {
        return mTask;
    }

    public TaskList(String mTask, int count) {
        this.mTask = mTask;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setmTask(String mTask) {
        this.mTask = mTask;
    }

    public int getmAddTask() {
        return mAddTask;
    }

    public void setmAddTask(int mAddTask) {
        this.mAddTask = mAddTask;
    }

    public int getmDeleteTask() {
        return mDeleteTask;
    }

    public void setmDeleteTask(int mDeleteTask) {
        this.mDeleteTask = mDeleteTask;
    }
}


