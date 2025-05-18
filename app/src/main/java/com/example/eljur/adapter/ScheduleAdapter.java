package com.example.eljur.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.schedule.Break;
import com.example.eljur.model.schedule.Lesson;
import com.example.eljur.model.schedule.Schedule;

import java.util.List;


public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int LES = 1, BR = 2;

    private final List<Schedule> data;


    public ScheduleAdapter( List<Schedule> d )
    {
        data = d;
    }

    @Override
    public int getItemViewType( int p )
    {
        return data.get( p ) instanceof Lesson ? LES : BR;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup p, int v )
    {
        var i = LayoutInflater.from( p.getContext() );
        if ( v == LES ) return new LessonVH( i.inflate( R.layout.item_schedule_lesson, p, false ) );
        return new BreakVH( i.inflate( R.layout.item_schedule_break, p, false ) );
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder vh, int p )
    {
        if ( vh instanceof LessonVH ) ( (LessonVH) vh ).bind( (Lesson) data.get( p ) );
        else ( (BreakVH) vh ).bind( (Break) data.get( p ) );
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    static class LessonVH extends RecyclerView.ViewHolder
    {

        TextView tvTime, tvRoom, tvSubject, tvTeacher, tvHomework;

        LinearLayout llGrades;

        LessonVH( View v )
        {
            super( v );
            tvTime = v.findViewById( R.id.tvTime );
            tvRoom = v.findViewById( R.id.tvRoom );
            tvSubject = v.findViewById( R.id.tvSubject );
            tvTeacher = v.findViewById( R.id.tvTeacher );
            tvHomework = v.findViewById( R.id.tvHomework );
            llGrades = v.findViewById( R.id.llGrades );
        }

        void bind( Lesson L )
        {
            tvTime.setText( L.time );
            tvRoom.setText( L.room );
            tvSubject.setText( L.subject );
            tvTeacher.setText( L.teacher );
            tvHomework.setText( L.homework );
            llGrades.removeAllViews();
            for ( String g : L.grades )
            {
                TextView t = new TextView( itemView.getContext() );
                t.setText( g );
                t.setPadding( 16, 8, 16, 8 );
                t.setBackgroundResource( R.drawable.bg_grade_block );
                t.setTypeface( null, android.graphics.Typeface.BOLD );
                llGrades.addView( t );
            }
        }

    }

    static class BreakVH extends RecyclerView.ViewHolder
    {

        TextView tvBreak;

        BreakVH( View v )
        {
            super( v );
            tvBreak = v.findViewById( R.id.tvBreak );
        }

        void bind( Break b )
        {
            tvBreak.setText( b.duration );
        }

    }

}
