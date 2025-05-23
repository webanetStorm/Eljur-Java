package com.example.eljur.ui.schedule;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.eljur.adapter.DayAdapter;
import com.example.eljur.databinding.FragmentScheduleBinding;
import com.example.eljur.model.schedule.Day;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


public class ScheduleFragment extends Fragment
{

    private FragmentScheduleBinding binding;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentScheduleBinding.inflate( inflater, container, false );

        setupWeekCalendar();

        return binding.getRoot();
    }

    private void setupWeekCalendar()
    {
        binding.rvDays.setLayoutManager( new GridLayoutManager( getContext(), 7 ) );
        binding.rvDays.setAdapter( new DayAdapter( generateCurrentWeek() ) );
    }

    private List<Day> generateCurrentWeek()
    {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with( TemporalAdjusters.previousOrSame( DayOfWeek.MONDAY ) );
        List<Day> list = new ArrayList<>();

        for ( int i = 0; i < 7; i++ )
        {
            LocalDate date = monday.plusDays( i );
            list.add( new Day( date, fetchLessonCount( date ) ) );
        }

        return list;
    }

    private int fetchLessonCount( LocalDate date )
    {
        return 0;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
