package com.example.eljur.model.schedule;


import java.time.LocalDate;
import java.util.List;


public class Day
{

    public final String name, date;

    public final List<Schedule> items;

    public final LocalDate realDate;


    public Day( String name, String date, List<Schedule> items, LocalDate rd )
    {
        this.name = name;
        this.date = date;
        this.items = items;
        this.realDate = rd;
    }

}
