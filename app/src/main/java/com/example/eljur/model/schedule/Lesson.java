package com.example.eljur.model.schedule;


import java.util.List;


public class Lesson extends Schedule
{

    public String time;

    public String room;

    public String subject;

    public String teacher;

    public List<String> grades;

    public String homework;


    public Lesson( String time, String room, String subject, String teacher, List<String> grades, String homework )
    {
        this.time = time;
        this.room = room;
        this.subject = subject;
        this.teacher = teacher;
        this.grades = grades;
        this.homework = homework;
    }

}
