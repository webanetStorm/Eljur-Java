package com.example.eljur.model;


public class Grade
{

    private final int value, weight;


    public Grade( int value, int weight )
    {
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
