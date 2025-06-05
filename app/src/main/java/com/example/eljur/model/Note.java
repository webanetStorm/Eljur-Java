package com.example.eljur.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity( tableName = "notes" )
public class Note
{

    @PrimaryKey( autoGenerate = true )
    public int id;

    public String text;

    public long timestamp;


    public Note( String text, long timestamp )
    {
        this.text = text;
        this.timestamp = timestamp;
    }

}
