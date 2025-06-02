package com.example.eljur.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.schedule.Day;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder>
{

    public interface OnDayClickListener
    {

        void onDayClick( LocalDate date );

    }


    private OnDayClickListener listener;

    private LocalDate selectedDate;

    private final List<Day> days;


    public DayAdapter( List<Day> days, OnDayClickListener listener )
    {
        this.days = days;
        this.listener = listener;
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
        int dayOfWeek = date.getDayOfWeek().getValue();

        holder.tvDayName.setText( date.getDayOfWeek().getDisplayName( TextStyle.SHORT_STANDALONE, new Locale( "ru" ) ).toUpperCase() );
        holder.tvDayDate.setText( String.valueOf( date.getDayOfMonth() ) );

        holder.tvDayDate.setText(String.valueOf(date.getDayOfMonth()));

        if ( dayOfWeek == 6 || dayOfWeek == 7 )
        {
            holder.tvDayDate.setTextColor( ContextCompat.getColor( holder.itemView.getContext(), R.color.red ) );
        }
        else
        {
            holder.tvDayDate.setTextColor( ContextCompat.getColor( holder.itemView.getContext(), R.color.white ) );
        }

        StringBuilder dots = new StringBuilder();

        for ( int i = 0; i < item.getLessonCount(); i++ )
        {
            dots.append( "●" );
        }

        holder.tvLessonCount.setText( dots.toString() );

        if ( date.equals( selectedDate ) )
        {
            holder.vDateOverlay.setVisibility( View.VISIBLE );
            holder.tvDayDate.setTextColor( ContextCompat.getColor( holder.itemView.getContext(), R.color.white ) );
        }
        else
        {
            holder.vDateOverlay.setVisibility( View.GONE );
        }

        holder.itemView.setOnClickListener( v ->
        {
            if ( listener != null )
            {
                listener.onDayClick( item.getDate() );
                setSelectedDate( item.getDate() );
            }
        } );
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public void setSelectedDate( LocalDate date )
    {
        this.selectedDate = date;
        notifyDataSetChanged();
    }

    public void updateLessonCount( int index, int count )
    {
        if ( index >= 0 && index < days.size() )
        {
            Day day = days.get( index );
            days.set( index, new Day( day.getDate(), count ) );
            notifyItemChanged( index );
        }
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
