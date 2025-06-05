package com.example.eljur.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database( entities = { Note.class }, version = 1 )
public abstract class AppDatabase extends RoomDatabase
{

    private static final String DB_NAME = "eljur_notes_db";

    private static AppDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized AppDatabase getInstance( Context context )
    {
        if ( instance == null )
        {
            instance = Room.databaseBuilder( context.getApplicationContext(), AppDatabase.class, DB_NAME ).allowMainThreadQueries()    // для простоты, чтобы не писать AsyncTask
                    .build();
        }
        return instance;
    }

}
