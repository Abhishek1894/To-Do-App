package com.example.myapplication;

import java.io.Serializable;

public class Task implements Serializable
{
    String task;
    String time;
    int priority;

    boolean isCompleted;

    public Task()
    {
    }

    public Task(String task)
    {
        this.task = task;
        this.time = "";
        this.isCompleted = false;
    }

    public Task(String task, String time)
    {
        this.task = task;
        this.time = time;
        this.isCompleted = false;
        this.priority = 0;
    }

    public Task(String task, String time, int priority)
    {
        this.task = task;
        this.time = time;
        this.isCompleted = false;
        this.priority = priority;
    }

}
