package com.example.eljur.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String INIT_SQL_FILE = "init.sql";

    private static final String DATABASE_NAME = "eljur.db";

    private static final int DATABASE_VERSION = 17;

    private final Context context;


    public DatabaseHelper( Context context )
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        createTables( db );
        executeSqlFromAssets( db, INIT_SQL_FILE );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        this.dropTables( db );
        this.onCreate( db );

        Log.d( "Database", "init.sql выполнен успешно" );
    }

    private void executeSqlFromAssets( SQLiteDatabase db, String fileName )
    {
        try
        {
            BufferedReader reader = new BufferedReader( new InputStreamReader( context.getAssets().open( fileName ) ) );
            StringBuilder statement = new StringBuilder();
            String line;

            while ( ( line = reader.readLine() ) != null )
            {
                line = line.trim();

                if ( line.isEmpty() || line.startsWith( "--" ) )
                {
                    continue;
                }

                statement.append( line );

                if ( line.endsWith( ";" ) )
                {
                    db.execSQL( statement.toString() );
                    statement.setLength( 0 );
                }
            }

            reader.close();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Ошибка чтения init.sql", e );
        }
    }

    private void createTables( SQLiteDatabase db )
    {
        db.execSQL( "CREATE TABLE academic_year (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "start_date TEXT NOT NULL," + "end_date TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE term (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "year_id INTEGER NOT NULL," + "term_number INTEGER NOT NULL," + "start_date TEXT NOT NULL," + "end_date TEXT NOT NULL," + "FOREIGN KEY(year_id) REFERENCES academic_year(id))" );

        db.execSQL( "CREATE TABLE teacher (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE subject (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE lesson (" + "lesson_number INTEGER PRIMARY KEY," + "start_time TEXT NOT NULL," + "end_time TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE schedule (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "year_id INTEGER NOT NULL," + "subject_id INTEGER NOT NULL," + "teacher_id INTEGER NOT NULL," + "classroom TEXT," + "day_of_week INTEGER NOT NULL," + "lesson_number INTEGER NOT NULL," + "FOREIGN KEY(subject_id) REFERENCES subject(id)," + "FOREIGN KEY(teacher_id) REFERENCES teacher(id)," + "FOREIGN KEY(lesson_number) REFERENCES lesson(lesson_number)," + "FOREIGN KEY(year_id) REFERENCES year(id))" );

        db.execSQL( "CREATE TABLE grade (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "subject_id INTEGER NOT NULL," + "term_id INTEGER NOT NULL," + "date TEXT NOT NULL," + "value INTEGER NOT NULL," + "weight INTEGER NOT NULL," + "FOREIGN KEY(subject_id) REFERENCES subject(id)," + "FOREIGN KEY(term_id) REFERENCES term(id))" );

        db.execSQL( "CREATE TABLE homework (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "subject_id INTEGER NOT NULL," + "term_id INTEGER NOT NULL," + "due_date TEXT NOT NULL," + "description TEXT," + "FOREIGN KEY(subject_id) REFERENCES subject(id)," + "FOREIGN KEY(term_id) REFERENCES term(id))" );

        db.execSQL( "CREATE TABLE holiday (date TEXT PRIMARY KEY)" );
    }

    private void dropTables( SQLiteDatabase db )
    {
        db.execSQL( "DROP TABLE IF EXISTS holiday" );
        db.execSQL( "DROP TABLE IF EXISTS homework" );
        db.execSQL( "DROP TABLE IF EXISTS grade" );
        db.execSQL( "DROP TABLE IF EXISTS schedule" );
        db.execSQL( "DROP TABLE IF EXISTS lesson" );
        db.execSQL( "DROP TABLE IF EXISTS subject" );
        db.execSQL( "DROP TABLE IF EXISTS teacher" );
        db.execSQL( "DROP TABLE IF EXISTS term" );
        db.execSQL( "DROP TABLE IF EXISTS academic_year" );
    }

}
