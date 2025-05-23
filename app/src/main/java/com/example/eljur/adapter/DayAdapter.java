package com.example.eljur.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.schedule.Day;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder>
{

    private final List<Day> days;


    public DayAdapter( List<Day> days )
    {
        this.days = days;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType )
    {
        return new DayViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_day, parent, false ) );
    }

    @Override
    public void onBindViewHolder( @NonNull DayViewHolder holder, int position )
    {
        Day item = days.get( position );
        LocalDate date = item.getDate();

        holder.tvDayName.setText( date.getDayOfWeek().getDisplayName( TextStyle.SHORT_STANDALONE, new Locale( "ru" ) ).toUpperCase() );
        holder.tvDayDate.setText( String.valueOf( date.getDayOfMonth() ) );

        StringBuilder dots = new StringBuilder();

        for ( int i = 0; i < item.getLessonCount(); i++ )
        {
            dots.append( "●" );
        }

        holder.tvLessonCount.setText( dots.toString() );

        if ( date.equals( LocalDate.now() ) )
        {
            holder.vDateOverlay.setVisibility( View.VISIBLE );
        }
        else
        {
            holder.vDateOverlay.setVisibility( View.GONE );
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder
    {

        public TextView tvDayName, tvDayDate, tvLessonCount;

        public View vDateOverlay;


        public DayViewHolder( @NonNull View itemView )
        {
            super( itemView );
            tvDayName = itemView.findViewById( R.id.tvDayName );
            tvDayDate = itemView.findViewById( R.id.tvDayDate );
            tvLessonCount = itemView.findViewById( R.id.tvLessonCount );
            vDateOverlay = itemView.findViewById( R.id.vDateOverlay );
        }

    }

}
