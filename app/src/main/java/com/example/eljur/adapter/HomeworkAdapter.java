package com.example.eljur.adapter;


import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.Homework;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder>
{

    private final List<String> dates;

    private final Map<String, List<Homework>> data;


    public HomeworkAdapter( List<String> dates, Map<String, List<Homework>> data )
    {
        this.dates = dates;
        this.data = data;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder( @NotNull ViewGroup parent, int viewType )
    {
        return new ViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_homework_day, parent, false ) );
    }

    @Override
    public void onBindViewHolder( @NotNull ViewHolder holder, int position )
    {
        String date = dates.get( position );
        holder.dateView.setText( formatDate( date ) );
        holder.homeworkLayout.removeAllViews();

        List<Homework> hwList = data.get( date );
        if ( hwList == null )
        {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from( holder.itemView.getContext() );

        for ( Homework hw : hwList )
        {
            TextView subject = new TextView( holder.itemView.getContext() );
            subject.setPadding( 0, 30, 0, 0 );
            subject.setText( hw.getSubject() );
            subject.setTextSize( 16 );
            subject.setTypeface( Typeface.DEFAULT_BOLD );
            subject.setTextColor( ContextCompat.getColor( holder.itemView.getContext(), R.color.white ) );

            TextView desc = new TextView( holder.itemView.getContext() );
            desc.setText( hw.getDescription() );
            desc.setTextColor( ContextCompat.getColor( holder.itemView.getContext(), R.color.white ) );

            holder.homeworkLayout.addView( subject );
            holder.homeworkLayout.addView( desc );
        }
    }

    @Override
    public int getItemCount()
    {
        return dates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        private final TextView dateView;

        private final LinearLayout homeworkLayout;


        private ViewHolder( View itemView )
        {
            super( itemView );
            dateView = itemView.findViewById( R.id.tvDate );
            homeworkLayout = itemView.findViewById( R.id.llHomeworks );
        }

    }

    private String formatDate( String rawDate )
    {
        try
        {
            SimpleDateFormat db = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() );
            Date parsed = db.parse( rawDate );

            return new SimpleDateFormat( "d MMMM yyyy", new Locale( "ru" ) ).format( parsed );
        }
        catch ( Exception e )
        {
            return rawDate;
        }
    }

}

