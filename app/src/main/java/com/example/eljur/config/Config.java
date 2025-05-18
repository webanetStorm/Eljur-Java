package com.example.eljur.config;


import com.example.eljur.model.schedule.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


public class Config
{

    private static final String[] TIMES = {
        "8:30–9:15", "9:25–10:10", "10:30–11:15",
        "11:25–12:10", "12:20–13:05"
    };

    private static final String[] BREAKS = {
        "10 мин", "15 мин", "10 мин", "15 мин"
    };

    private static final LessonTemplate[] TMPL = {
        new LessonTemplate( "Математика", "Кода Л.И.", "Г-209", "Параграф 14" ),
        new LessonTemplate( "Русский язык", "Пантюхина А.Н.", "Б-211", "Упр.114" ),
        new LessonTemplate( "Информатика", "Шеин Д.С.", "И-8", "Вар.2053" ),
        new LessonTemplate( "Физика", "Иванов П.П.", "Ф-101", "" ),
        new LessonTemplate( "Химия", "Петрова А.А.", "Х-303", "" )
    };


    public static List<List<Day>> getAllWeeks()
    {
        List<List<Day>> res = new ArrayList<>();
        LocalDate cur = AcademicCalendar.START.with( TemporalAdjusters.previousOrSame( DayOfWeek.MONDAY ) );

        while ( !cur.isAfter( AcademicCalendar.END ) )
        {
            List<Day> week = new ArrayList<>();

            for ( int i = 0; i < 7; i++ )
            {
                LocalDate d = cur.plusDays( i );
                String[] names = { "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС" };
                String name = names[d.getDayOfWeek().getValue() - 1];
                String date = String.valueOf( d.getDayOfMonth() );
                List<Schedule> items = new ArrayList<>();

                if ( d.getDayOfWeek().getValue() <= 5 && !AcademicCalendar.HOLIDAYS.contains( d ) )
                {
                    for ( int j = 0; j < TMPL.length; j++ )
                    {
                        LessonTemplate t = TMPL[j];
                        items.add( new Lesson( TIMES[j], t.room, t.subject, t.teacher, Grades.get( d, t.subject ), t.homework ) );

                        if ( j < BREAKS.length )
                        {
                            items.add( new Break( "Перемена " + BREAKS[j] ) );
                        }
                    }
                }

                week.add( new Day( name, date, items, d ) );
            }

            res.add( week );
            cur = cur.plusWeeks( 1 );
        }

        return res;
    }

    private static class LessonTemplate
    {

        String subject, teacher, room, homework;

        LessonTemplate( String s, String t, String r, String h )
        {
            subject = s;
            teacher = t;
            room = r;
            homework = h;
        }

    }

}
