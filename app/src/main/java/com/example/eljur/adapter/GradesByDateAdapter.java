package com.example.eljur.adapter;


import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.Grade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class GradesByDateAdapter extends RecyclerView.Adapter<GradesByDateAdapter.ViewHolder>
{

    private final List<String> dates;

    private final Map<String, List<Pair<String, Grade>>> gradeMap;

    public GradesByDateAdapter( List<String> dates, Map<String, List<Pair<String, Grade>>> gradeMap )
    {
        this.dates = dates;
        this.gradeMap = gradeMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_grades_date, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position )
    {
        String date = dates.get( position );
        holder.dateView.setText( formatDate( date ) );

        holder.gradesContainer.removeAllViews();
        List<Pair<String, Grade>> grades = gradeMap.get( date );
        if ( grades == null )
        {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from( holder.itemView.getContext() );

        for ( Pair<String, Grade> pair : grades )
        {
            String subject = pair.first;
            Grade grade = pair.second;

            View gradeRow = inflater.inflate( R.layout.item_grade_row, holder.gradesContainer, false );

            TextView tvValue = gradeRow.findViewById( R.id.tvGradeValue );
            TextView tvWeight = gradeRow.findViewById( R.id.tvGradeWeight );
            TextView tvSubject = gradeRow.findViewById( R.id.tvSubjectName );

            tvValue.setText( String.valueOf( grade.getValue() ) );
            tvWeight.setText( String.valueOf( grade.getWeight() ) );
            tvSubject.setText( subject );

            holder.gradesContainer.addView( gradeRow );
        }
    }

    private String formatDate( String rawDate )
    {
        try
        {
            SimpleDateFormat dbFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() );
            Date date = dbFormat.parse( rawDate );
            SimpleDateFormat viewFormat = new SimpleDateFormat( "d MMMM yyyy", new Locale( "ru" ) );

            return viewFormat.format( date );
        }
        catch ( Exception e )
        {
            return rawDate;
        }
    }


    @Override
    public int getItemCount()
    {
        return dates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView dateView;

        LinearLayout gradesContainer;

        ViewHolder( View itemView )
        {
            super( itemView );
            dateView = itemView.findViewById( R.id.tvDate );
            gradesContainer = itemView.findViewById( R.id.llGrades );
        }

    }

}

