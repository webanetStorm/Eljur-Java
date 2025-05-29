package com.example.eljur.model;


public class Homework
{

    private final String subject, description;


    public Homework( String subject, String description )
    {
        this.subject = subject;
        this.description = description;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getDescription()
    {
        return description;
    }

}
