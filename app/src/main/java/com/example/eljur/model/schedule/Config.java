package com.example.eljur.model.schedule;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Config
{

    private static final String[] LESSON_TIMES = {
        "8:30 – 9:15", "9:25 – 10:10", "10:30 – 11:15",
        "11:35 – 12:20", "12:30 – 13:15"
    };

    private static final String[] BREAK_DURATIONS = {
        "10 мин", "15 мин", "10 мин", "15 мин"
    };

    private static final LessonTemplate[] TEMPLATES = {
        new LessonTemplate( "Математика", "Кода Лилия Ивановна", "Б-109", "Пар. 12, №3-10" ),
        new LessonTemplate( "Русский язык", "Пантюхина Анна Николаевна", "Б-212", "Упр. 112" ),
        new LessonTemplate( "Информатика", "Шеин Дмитрий Сергеевич", "И-8", "Демовариант №2053" ),
        new LessonTemplate( "Физика", "Снимщикова Елена Александровна", "Б-326", "" ),
        new LessonTemplate( "Химия", "Коляскина Анна Александровна", "Г-418", "" )
    };

    public static List<Day> getWeekSchedule()
    {
        List<Day> week = new ArrayList<>();

        LocalDate today = LocalDate.now();
        int dow = today.getDayOfWeek().getValue();
        LocalDate monday = today.minusDays( dow - DayOfWeek.MONDAY.getValue() );

        DateTimeFormatter dfDay = DateTimeFormatter.ofPattern( "dd" );
        DateTimeFormatter dfDoW = DateTimeFormatter.ofPattern( "E" );

        for ( int i = 0; i < 7; i++ )
        {
            LocalDate date = monday.plusDays( i );
            String dayName = date.format( dfDoW );
            String dayNum = date.format( dfDay );

            List<Schedule> items = new ArrayList<>();

            if ( date.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue() )
            {
                for ( int j = 0; j < TEMPLATES.length; j++ )
                {
                    LessonTemplate tpl = TEMPLATES[j];
                    items.add( new Lesson( LESSON_TIMES[j], tpl.room, tpl.subject, tpl.teacher, new ArrayList<>(), tpl.homework ) );

                    if ( j < BREAK_DURATIONS.length )
                    {
                        items.add( new Break( "Перемена " + BREAK_DURATIONS[j] ) );
                    }
                }
            }

            week.add( new Day( dayName, dayNum, items, date ) );
        }

        return week;
    }


    private static class LessonTemplate
    {

        public final String subject, teacher, room, homework;

        public LessonTemplate( String subject, String teacher, String room, String homework )
        {
            this.subject = subject;
            this.teacher = teacher;
            this.room = room;
            this.homework = homework;
        }

    }

}
