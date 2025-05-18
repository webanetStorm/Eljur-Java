package com.example.eljur.ui.schedule;


import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eljur.adapter.DayAdapter;
import com.example.eljur.adapter.ScheduleAdapter;
import com.example.eljur.config.Config;
import com.example.eljur.model.schedule.Day;
import com.example.eljur.databinding.FragmentScheduleBinding;

import java.util.List;


public class ScheduleFragment extends Fragment
{

    private FragmentScheduleBinding b;

    private List<List<Day>> weeks;

    private int currentWeek = 0, currentDay = 0;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup c, Bundle s )
    {
        b = FragmentScheduleBinding.inflate( inflater, c, false );
        weeks = Config.getAllWeeks();
        var dayAdapter = new DayAdapter( weeks, ( w, d ) ->
        {
            currentWeek = w;
            currentDay = d;
            load();
        }, b.rvDays );
        b.rvDays.setLayoutManager( new LinearLayoutManager( requireContext(), LinearLayoutManager.HORIZONTAL, false ) );
        b.rvDays.setAdapter( dayAdapter );
        b.rvSchedule.setLayoutManager( new LinearLayoutManager( requireContext() ) );
        dayAdapter.setWeek( currentWeek );
        load();
        var gd = new GestureDetector( requireContext(), new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onFling( MotionEvent e1, MotionEvent e2, float vx, float vy )
            {
                if ( Math.abs( vx ) > 1000 )
                {
                    if ( vx < 0 && currentWeek < weeks.size() - 1 ) currentWeek++;
                    if ( vx > 0 && currentWeek > 0 ) currentWeek--;
                    dayAdapter.setWeek( currentWeek );
                    load();
                    return true;
                }
                return false;
            }
        } );
        b.getRoot().setOnTouchListener( ( v, e ) -> gd.onTouchEvent( e ) );
        return b.getRoot();
    }

    private void load()
    {
        b.rvSchedule.setAdapter( new ScheduleAdapter( weeks.get( currentWeek ).get( currentDay ).items ) );
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        b = null;
    }

}
