package com.example.eljur.model;


import java.time.LocalDate;


public class Day
{

    private final LocalDate date;

    private int lessonCount;


    public Day( LocalDate date, int lessonCount )
    {
        this.date = date;
        this.lessonCount = lessonCount;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public int getLessonCount()
    {
        return lessonCount;
    }

    public void setLessonCount( int count )
    {
        lessonCount = count;
    }

}
