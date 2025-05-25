package com.example.eljur.model;


import java.util.List;


public class Schedule
{

    private final String subjectName, teacherName, classroom, startTime, endTime, homework;

    private final List<Grade> grades;

    private final int lessonNumber;


    public Schedule( String subjectName, String teacherName, String classroom, String startTime, String endTime, String homework, List<Grade> grades, int lessonNumber )
    {
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.classroom = classroom;
        this.startTime = startTime;
        this.endTime = endTime;
        this.homework = homework;
        this.grades = grades;
        this.lessonNumber = lessonNumber;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public String getTeacherName()
    {
        return teacherName;
    }

    public String getClassroom()
    {
        return classroom;
    }

    public String getHomework()
    {
        return homework;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public List<Grade> getGrades()
    {
        return grades;
    }

    public int getLessonNumber()
    {
        return lessonNumber;
    }

}
