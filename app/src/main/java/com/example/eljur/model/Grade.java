package com.example.eljur.model;


public class Grade
{

    public String subject;

    public int value, weight;


    public Grade()
    {
    }

    public Grade( String subject, int value, int weight )
    {
        this.subject = subject;
        this.value = value;
        this.weight = weight;
    }

    public int getValue()
    {
        return value;
    }

    public int getWeight()
    {
        return weight;
    }

}
