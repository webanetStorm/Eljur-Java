package com.example.eljur.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.Grade;
import com.example.eljur.model.Schedule;

import java.util.List;


public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int TYPE_LESSON = 0;

    private static final int TYPE_BREAK = 1;

    private final List<Object> items;


    public ScheduleAdapter( List<Object> items )
    {
        this.items = items;
    }

    @Override
    public int getItemViewType( int position )
    {
        return ( items.get( position ) instanceof Schedule ) ? TYPE_LESSON : TYPE_BREAK;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType )
    {
        if ( viewType == TYPE_LESSON )
        {
            return new LessonViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_schedule_lesson, parent, false ) );
        }
        else
        {
            return new BreakViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_schedule_break, parent, false ) );
        }
    }

    @Override
    public void onBindViewHolder( @NonNull RecyclerView.ViewHolder holder, int position )
    {
        if ( holder.getItemViewType() == TYPE_LESSON )
        {
            Schedule item = (Schedule)items.get( position );
            LessonViewHolder lessonHolder = (LessonViewHolder)holder;

            lessonHolder.time.setText( String.format( "%s - %s", item.getStartTime(), item.getEndTime() ) );
            lessonHolder.classroom.setText( item.getClassroom() );
            lessonHolder.subjectName.setText( item.getSubjectName() );
            lessonHolder.teacherName.setText( item.getTeacherName() );

            if ( item.getHomework() != null && !item.getHomework().isEmpty() )
            {
                lessonHolder.homework.setText( item.getHomework() );
                lessonHolder.homework.setVisibility( View.VISIBLE );
            }
            else
            {
                lessonHolder.homework.setVisibility( View.GONE );
            }

            lessonHolder.grades.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from( holder.itemView.getContext() );

            for ( Grade grade : item.getGrades() )
            {
                View gradeBlock = inflater.inflate( R.layout.item_grade_block, lessonHolder.grades, false );

                TextView tvValue = gradeBlock.findViewById( R.id.tvGradeValue );
                TextView tvWeight = gradeBlock.findViewById( R.id.tvGradeWeight );

                tvValue.setText( String.valueOf( grade.getValue() ) );
                tvWeight.setText( String.valueOf( grade.getWeight() ) );

                lessonHolder.grades.addView( gradeBlock );
            }

            lessonHolder.grades.setVisibility( item.getGrades().isEmpty() ? View.GONE : View.VISIBLE );
        }
        else
        {
            ( (BreakViewHolder)holder ).breakText.setText( (String)items.get( position ) );
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }


    public static class LessonViewHolder extends RecyclerView.ViewHolder
    {

        public final LinearLayout grades;

        public final TextView subjectName, teacherName, classroom, time, homework;


        public LessonViewHolder( View itemView )
        {
            super( itemView );
            subjectName = itemView.findViewById( R.id.tvSubject );
            teacherName = itemView.findViewById( R.id.tvTeacher );
            classroom = itemView.findViewById( R.id.tvRoom );
            time = itemView.findViewById( R.id.tvTime );
            grades = itemView.findViewById( R.id.llGrades );
            homework = itemView.findViewById( R.id.tvHomework );
        }

    }


    public static class BreakViewHolder extends RecyclerView.ViewHolder
    {

        public final TextView breakText;


        public BreakViewHolder( View itemView )
        {
            super( itemView );
            breakText = itemView.findViewById( R.id.tvBreak );
        }

    }

}
