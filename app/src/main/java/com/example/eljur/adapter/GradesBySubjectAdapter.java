package com.example.eljur.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.Grade;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;
import java.util.Locale;


public class GradesBySubjectAdapter extends RecyclerView.Adapter<GradesBySubjectAdapter.ViewHolder>
{

    private final List<String> subjects;

    private final java.util.Map<String, List<Grade>> gradesMap;

    public GradesBySubjectAdapter( List<String> subjects, java.util.Map<String, List<Grade>> gradesMap )
    {
        this.subjects = subjects;
        this.gradesMap = gradesMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_grades_subject, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position )
    {
        String subject = subjects.get( position );
        holder.subjectName.setText( subject );
        holder.gradesLayout.removeAllViews();

        List<Grade> grades = gradesMap.get( subject );
        if ( grades == null || grades.isEmpty() )
        {
            holder.averageView.setText( "-" );
            return;
        }

        int total = 0;
        int weightSum = 0;
        for ( Grade g : grades )
        {
            total += g.getValue() * g.getWeight();
            weightSum += g.getWeight();
        }

        double average = ( weightSum > 0 ) ? (double)total / weightSum : 0;
        holder.averageView.setText( String.format( Locale.getDefault(), "%.2f", average ) );

        LayoutInflater inflater = LayoutInflater.from( holder.itemView.getContext() );

        for ( Grade grade : grades )
        {
            View gradeBlock = inflater.inflate( R.layout.item_grade_block, holder.gradesLayout, false );

            TextView tvValue = gradeBlock.findViewById( R.id.tvGradeValue );
            TextView tvWeight = gradeBlock.findViewById( R.id.tvGradeWeight );

            tvValue.setText( String.valueOf( grade.getValue() ) );
            tvWeight.setText( String.valueOf( grade.getWeight() ) );

            holder.gradesLayout.addView( gradeBlock );
        }
    }

    @Override
    public int getItemCount()
    {
        return subjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView subjectName;

        TextView averageView;

        FlexboxLayout gradesLayout;

        ViewHolder( View itemView )
        {
            super( itemView );
            subjectName = itemView.findViewById( R.id.tvSubjectName );
            averageView = itemView.findViewById( R.id.tvAverage );
            gradesLayout = itemView.findViewById( R.id.llSubjectGrades );
        }

    }

}
