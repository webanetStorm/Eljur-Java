package com.example.eljur.ui.schedule;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.adapter.DayAdapter;
import com.example.eljur.model.schedule.Day;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class WeekFragment extends Fragment
{

    private static final String ARG_WEEK_OFFSET = "week_offset";


    public static WeekFragment newInstance( int weekOffset )
    {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();

        args.putInt( ARG_WEEK_OFFSET, weekOffset );
        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        RecyclerView recyclerView = new RecyclerView( requireContext() );
        recyclerView.setLayoutManager( new GridLayoutManager( getContext(), 7 ) );

        int weekOffset = getArguments() != null ? getArguments().getInt( ARG_WEEK_OFFSET ) : 0;
        LocalDate baseMonday = LocalDate.now().with( DayOfWeek.MONDAY ).plusWeeks( weekOffset );

        Fragment parent = getParentFragment();
        ScheduleFragment scheduleFragment = parent instanceof ScheduleFragment ? (ScheduleFragment)parent : null;

        if ( scheduleFragment != null )
        {
            scheduleFragment.updateDateIndicator( baseMonday );
        }

        List<Day> days = new ArrayList<>();
        for ( int i = 0; i < 7; i++ )
        {
            LocalDate date = baseMonday.plusDays( i );
            int count = 0;

            if ( scheduleFragment != null && scheduleFragment.isDateAllowed( date ) )
            {
                count = scheduleFragment.getLessonCountForDate( date );
            }

            days.add( new Day( date, count ) );
        }

        DayAdapter adapter = new DayAdapter( days, selectedDate ->
        {
            if ( scheduleFragment != null )
            {
                scheduleFragment.loadSchedule( 1, selectedDate );
            }
        } );

        adapter.setSelectedDate( LocalDate.now() );
        recyclerView.setAdapter( adapter );

        return recyclerView;
    }

}
