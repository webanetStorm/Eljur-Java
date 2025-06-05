package com.example.eljur.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface NoteDao
{

    @Insert
    long insert( Note note );

    @Update
    void update( Note note );

    @Delete
    void delete( Note note );

    @Query( "SELECT * FROM notes ORDER BY timestamp DESC" )
    List<Note> getAllNotes();

}
