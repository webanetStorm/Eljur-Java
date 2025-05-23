package com.example.eljur.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "eljur.db";

    private static final int DATABASE_VERSION = 4;


    public DatabaseHelper( Context context )
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        this.createTables( db );
        this.fillTables( db );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        db.execSQL( "DROP TABLE IF EXISTS holiday" );
        db.execSQL( "DROP TABLE IF EXISTS homework" );
        db.execSQL( "DROP TABLE IF EXISTS grade" );
        db.execSQL( "DROP TABLE IF EXISTS schedule_entry" );
        db.execSQL( "DROP TABLE IF EXISTS schedule_time" );
        db.execSQL( "DROP TABLE IF EXISTS subject" );
        db.execSQL( "DROP TABLE IF EXISTS teacher" );
        db.execSQL( "DROP TABLE IF EXISTS term" );
        db.execSQL( "DROP TABLE IF EXISTS academic_year" );
        onCreate( db );
    }

    private void createTables( SQLiteDatabase db )
    {
        db.execSQL( "CREATE TABLE academic_year ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "start_date TEXT NOT NULL,"
                + "end_date TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE term ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "year_id INTEGER NOT NULL,"
                + "term_number INTEGER NOT NULL,"
                + "start_date TEXT NOT NULL,"
                + "end_date TEXT NOT NULL,"
                + "FOREIGN KEY(year_id) REFERENCES academic_year(year_id))" );

        db.execSQL( "CREATE TABLE teacher ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE subject ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE schedule_time ("
                + "lesson_number INTEGER PRIMARY KEY,"
                + "start_time TEXT NOT NULL,"
                + "end_time TEXT NOT NULL)" );

        db.execSQL( "CREATE TABLE schedule_entry ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "subject_id INTEGER NOT NULL,"
                + "teacher_id INTEGER NOT NULL,"
                + "room TEXT,"
                + "day_of_week INTEGER NOT NULL,"
                + "lesson_number INTEGER NOT NULL,"
                + "term_id INTEGER NOT NULL,"
                + "FOREIGN KEY(subject_id) REFERENCES subject(subject_id),"
                + "FOREIGN KEY(teacher_id) REFERENCES teacher(teacher_id),"
                + "FOREIGN KEY(lesson_number) REFERENCES schedule_time(lesson_number),"
                + "FOREIGN KEY(term_id) REFERENCES term(term_id))" );

        db.execSQL( "CREATE TABLE grade ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "subject_id INTEGER NOT NULL,"
                + "term_id INTEGER NOT NULL,"
                + "date TEXT NOT NULL,"
                + "value INTEGER NOT NULL,"
                + "weight INTEGER NOT NULL,"
                + "FOREIGN KEY(subject_id) REFERENCES subject(subject_id),"
                + "FOREIGN KEY(term_id) REFERENCES term(term_id))" );

        db.execSQL( "CREATE TABLE homework ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "subject_id INTEGER NOT NULL,"
                + "term_id INTEGER NOT NULL,"
                + "assigned_date TEXT NOT NULL,"
                + "due_date TEXT,"
                + "description TEXT,"
                + "FOREIGN KEY(subject_id) REFERENCES subject(subject_id),"
                + "FOREIGN KEY(term_id) REFERENCES term(term_id))" );

        db.execSQL( "CREATE TABLE holiday ("
                + "holiday_date TEXT PRIMARY KEY)" );
    }

    private void fillTables( SQLiteDatabase db )
    {
        db.execSQL( "INSERT INTO schedule_time VALUES "
                + "(1,'08:30','09:15'),"
                + "(2,'09:25','10:10'),"
                + "(3,'10:30','11:15'),"
                + "(4,'11:25','12:10'),"
                + "(5,'12:20','13:05'),"
                + "(6,'13:15','14:00'),"
                + "(7,'14:20','15:05')" );

        db.execSQL( "INSERT INTO academic_year(start_date, end_date) VALUES" + "('2024-09-01', '2025-05-31')" );

        db.execSQL( "INSERT INTO term(year_id, term_number, start_date, end_date) VALUES" + "(1, 1, '2024-09-01', '2024-11-30')," + "(1, 2, '2024-12-01', '2025-02-28')," + "(1, 3, '2025-03-01', '2025-05-31')" );

        db.execSQL( "INSERT INTO subject(name) VALUES" + "('Математика')," + "('Русский язык')," + "('Информатика')," + "('Физика')," + "('Химия')" );

        db.execSQL( "INSERT INTO teacher(name) VALUES" + "('Кода Л.И.')," + "('Пантюхина А.Н.')," + "('Шеин Д.С.')," + "('Иванов П.П.')," + "('Петрова А.А.')" );

        db.execSQL( "INSERT INTO schedule_entry(subject_id, teacher_id, room, day_of_week, lesson_number, term_id) VALUES" +
                "(1, 1, 'Г-209', 1, 1, 1)," + "(2, 2, 'Б-211', 1, 2, 1)," + "(3, 3, 'И-8', 1, 3, 1)," + "(4, 4, 'Ф-101', 1, 4, 1)," +
                "(5, 5, 'Х-303', 2, 5, 1)," + "(2, 2, 'Б-211', 2, 1, 1)," + "(3, 3, 'И-8', 2, 2, 1)," + "(4, 4, 'Ф-101', 2, 3, 1)," + "(5, 5, 'Х-303', 2, 4, 1)," + "(1, 1, 'Г-209', 2, 5, 1)," +
                "(3, 3, 'И-8', 3, 1, 1)," + "(4, 4, 'Ф-101', 3, 2, 1)," + "(5, 5, 'Х-303', 3, 3, 1)," + "(1, 1, 'Г-209', 3, 4, 1)," + "(2, 2, 'Б-211', 3, 5, 1)," +
                "(4, 4, 'Ф-101', 4, 1, 1)," + "(5, 5, 'Х-303', 4, 2, 1)," + "(1, 1, 'Г-209', 4, 3, 1)," + "(3, 3, 'И-8', 4, 5, 1)," +
                "(5, 5, 'Х-303', 5, 1, 1)," + "(1, 1, 'Г-209', 5, 2, 1)," + "(2, 2, 'Б-211', 5, 3, 1)," + "(3, 3, 'И-8', 5, 4, 1)," + "(4, 4, 'Ф-101', 5, 5, 1)" );

        db.execSQL( "INSERT INTO grade(subject_id, term_id, date, value, weight) VALUES" + "(1, 1, '2024-09-01', 5, 1)," + "(1, 1, '2024-09-01', 4, 1)," + "(2, 1, '2024-09-02', 5, 2)" );

        db.execSQL( "INSERT INTO homework(subject_id, term_id, assigned_date, due_date, description) VALUES" + "(1, 1, '2024-09-01', '2024-09-02', 'Параграф 14')" );
    }

}
