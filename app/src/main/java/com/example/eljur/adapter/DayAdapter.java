package com.example.eljur.adapter;


import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.schedule.Day;

import java.util.List;


public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayVH>
{

    public interface OnDayClick
    {

        void onClick( int weekIndex, int dayIndex );

    }

    private final List<List<Day>> weeks;

    private final OnDayClick cb;

    private final int itemW;

    private int selectedWeek, selectedDay;


    public DayAdapter( List<List<Day>> w, OnDayClick cb, ViewGroup parent )
    {
        this.weeks = w;
        this.cb = cb;
        DisplayMetrics dm = parent.getContext().getResources().getDisplayMetrics();
        itemW = dm.widthPixels / 7;
    }

    @Override
    public DayVH onCreateViewHolder( ViewGroup p, int viewType )
    {
        View v2 = LayoutInflater.from( p.getContext() ).inflate( R.layout.item_day, p, false );
        v2.getLayoutParams().width = itemW;
        return new DayVH( v2 );
    }

    @Override
    public void onBindViewHolder( DayVH h, int pos )
    {
        Day d = weeks.get( selectedWeek ).get( pos );
        h.tvDate.setText( d.date );
        int cnt = 0;
        for ( var s : d.items ) if ( s instanceof com.example.eljur.model.schedule.Lesson ) cnt++;
        h.tvLessonCount.setText( "●".repeat( cnt ) );
        h.itemView.setAlpha( pos == selectedDay ? 1f : 0.6f );
        h.itemView.setOnClickListener( v ->
        {
            int adapterPos = h.getAdapterPosition();
            if ( adapterPos == RecyclerView.NO_POSITION ) return;
            selectedDay = adapterPos;
            notifyDataSetChanged();
            cb.onClick( selectedWeek, selectedDay );
        } );
    }

    @Override
    public int getItemCount()
    {
        return 7;
    }

    public void setWeek( int w )
    {
        selectedWeek = w;
        selectedDay = 0;
        notifyDataSetChanged();
    }

    static class DayVH extends RecyclerView.ViewHolder
    {

        TextView tvDate;

        TextView tvLessonCount;

        DayVH( View v )
        {
            super( v );
            tvDate = v.findViewById( R.id.tvDayDate );
            tvLessonCount = v.findViewById( R.id.tvLessonCount );
        }

    }

}
