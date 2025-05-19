package com.example.eljur.config;


import java.time.LocalDate;
import java.util.Set;


public class AcademicCalendar
{

    public static final LocalDate START = LocalDate.of( 2025, 9, 1 );

    public static final LocalDate END = LocalDate.of( 2026, 5, 30 );

    public static final Set<LocalDate> HOLIDAYS = Set.of(
        LocalDate.of( 2025, 12, 25 ),
        LocalDate.of( 2026, 1, 1 )
    );

}
