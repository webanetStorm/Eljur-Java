package com.example.eljur.config;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class Grades
{

    private static final Map<LocalDate, Map<String, List<String>>> grades = Map.of(
        LocalDate.of( 2025, 9, 1 ),
        Map.of( "Математика", List.of( "5", "4" ), "Русский язык", List.of( "5" ) )
    );


    public static List<String> get( LocalDate date, String subject )
    {
        if ( !grades.containsKey( date ) )
        {
            return List.of();
        }

        return grades.get( date ).getOrDefault( subject, List.of() );
    }

}
