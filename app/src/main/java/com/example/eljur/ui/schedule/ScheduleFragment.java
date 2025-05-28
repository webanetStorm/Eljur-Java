package com.example.eljur.ui.schedule;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.eljur.adapter.ScheduleAdapter;
import com.example.eljur.databinding.FragmentScheduleBinding;
import com.example.eljur.db.DatabaseHelper;
import com.example.eljur.model.Grade;
import com.example.eljur.model.Schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.time.format.TextStyle;


public class ScheduleFragment extends Fragment
{

    private FragmentScheduleBinding binding;

    private RecyclerView recyclerView;

    private LinearLayout emptyView;

    private ViewPager2 weekPager;

    private DatabaseHelper dbHelper;

    private LocalDate selectedDate = LocalDate.now();

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentScheduleBinding.inflate( inflater, container, false );

        dbHelper = new DatabaseHelper( requireContext() );

        recyclerView = binding.rvSchedule;
        emptyView = binding.tvEmpty;
        weekPager = binding.vpWeeks;

        weekPager.setAdapter( new WeekPagerAdapter( this ) );
        weekPager.setCurrentItem( 1000, false );

        loadSchedule( 1, selectedDate );

        return binding.getRoot();
    }

    public void updateDateIndicator( LocalDate anyDateOfWeek )
    {
        String month = anyDateOfWeek.getMonth().getDisplayName( TextStyle.FULL_STANDALONE, new Locale( "ru" ) );
        int year = anyDateOfWeek.getYear();
        String trimester = getTermNameForDate( anyDateOfWeek );

        String result = capitalizeFirst( month ) + " " + year + " · " + trimester;
        binding.tvDateIndicator.setText( result );
    }

    private String capitalizeFirst( String input )
    {
        if ( input == null || input.isEmpty() )
        {
            return input;
        }
        return input.substring( 0, 1 ).toUpperCase() + input.substring( 1 );
    }

    private String getTermNameForDate( LocalDate date )
    {
        String query = "SELECT term_number FROM term WHERE ? BETWEEN start_date AND end_date";

        try ( Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, new String[] { date.toString() } ) )
        {
            if ( cursor.moveToFirst() )
            {
                return cursor.getInt( 0 ) + " триместр";
            }
        }

        return "Неучебный период";
    }

    public boolean isDateAllowed( LocalDate date )
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String yearCheck = "SELECT 1 FROM academic_year WHERE ? BETWEEN start_date AND end_date";
        try ( Cursor cursor = db.rawQuery( yearCheck, new String[] { date.toString() } ) )
        {
            if ( !cursor.moveToFirst() )
            {
                return false;
            }
        }

        String holidayCheck = "SELECT 1 FROM holiday WHERE date = ?";
        try ( Cursor cursor = db.rawQuery( holidayCheck, new String[] { date.toString() } ) )
        {
            if ( cursor.moveToFirst() )
            {
                return false;
            }
        }

        return true;
    }

    public int getLessonCountForDate( LocalDate date )
    {
        if ( !isDateAllowed( date ) )
        {
            return 0;
        }

        int count = 0;
        int dayOfWeek = date.getDayOfWeek().getValue();

        String query = "SELECT COUNT(*) FROM schedule WHERE year_id = 1 AND day_of_week = ?";
        try ( Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, new String[] { String.valueOf( dayOfWeek ) } ) )
        {
            if ( cursor.moveToFirst() )
            {
                count = cursor.getInt( 0 );
            }
        }

        return count;
    }

    public void loadSchedule( int yearId, LocalDate date )
    {
        selectedDate = date;

        if ( !isDateAllowed( date ) )
        {
            updateUI( new ArrayList<>() );
            return;
        }

        List<Schedule> scheduleItems = new ArrayList<>();
        int dayOfWeek = date.getDayOfWeek().getValue();

        String query = "SELECT sch.lesson_number, sch.classroom, subj.name AS subject_name, " + "t.name AS teacher_name, l.start_time, l.end_time, sch.subject_id " + "FROM schedule sch " + "INNER JOIN subject subj ON sch.subject_id = subj.id " + "INNER JOIN teacher t ON sch.teacher_id = t.id " + "INNER JOIN lesson l ON sch.lesson_number = l.lesson_number " + "WHERE sch.year_id = ? AND sch.day_of_week = ? " + "ORDER BY sch.lesson_number ASC";

        try ( Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, new String[] { String.valueOf( yearId ),
                String.valueOf( dayOfWeek ) } ) )
        {
            if ( cursor.moveToFirst() )
            {
                do
                {
                    int lessonNumber = cursor.getInt( cursor.getColumnIndexOrThrow( "lesson_number" ) );
                    int subjectId = cursor.getInt( cursor.getColumnIndexOrThrow( "subject_id" ) );

                    String homework = getHomeworkForLesson( subjectId, date );
                    List<Grade> grades = getGradesForLesson( subjectId, date );

                    scheduleItems.add( new Schedule( cursor.getString( cursor.getColumnIndexOrThrow( "subject_name" ) ), cursor.getString( cursor.getColumnIndexOrThrow( "teacher_name" ) ), cursor.getString( cursor.getColumnIndexOrThrow( "classroom" ) ), cursor.getString( cursor.getColumnIndexOrThrow( "start_time" ) ), cursor.getString( cursor.getColumnIndexOrThrow( "end_time" ) ), homework, grades, lessonNumber ) );
                }
                while ( cursor.moveToNext() );
            }
        }

        updateUI( scheduleItems );
    }

    private String getHomeworkForLesson( int subjectId, LocalDate date )
    {
        String query = "SELECT description FROM homework WHERE subject_id = ? AND due_date = ?";
        try ( Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, new String[] { String.valueOf( subjectId ),
                date.toString() } ) )
        {
            if ( cursor.moveToFirst() )
            {
                return cursor.getString( 0 );
            }
        }
        return null;
    }

    private List<Grade> getGradesForLesson( int subjectId, LocalDate date )
    {
        List<Grade> grades = new ArrayList<>();
        String query = "SELECT value, weight FROM grade WHERE subject_id = ? AND date = ?";

        try ( Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, new String[] { String.valueOf( subjectId ),
                date.toString() } ) )
        {
            while ( cursor.moveToNext() )
            {
                grades.add( new Grade( cursor.getInt( 0 ), cursor.getInt( 1 ) ) );
            }
        }
        return grades;
    }

    private int getBreakDuration( String endTime, String nextStartTime )
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "HH:mm" );
        LocalTime end = LocalTime.parse( endTime, formatter );
        LocalTime nextStart = LocalTime.parse( nextStartTime, formatter );
        return (int)Duration.between( end, nextStart ).toMinutes();
    }

    private void updateUI( List<Schedule> lessons )
    {
        List<Object> mixedList = new ArrayList<>();

        for ( int i = 0; i < lessons.size(); i++ )
        {
            Schedule current = lessons.get( i );
            mixedList.add( current );

            if ( i < lessons.size() - 1 )
            {
                Schedule next = lessons.get( i + 1 );
                int breakMinutes = getBreakDuration( current.getEndTime(), next.getStartTime() );
                if ( breakMinutes > 0 )
                {
                    mixedList.add( "Перемена " + breakMinutes + " мин" );
                }
            }
        }

        if ( mixedList.isEmpty() )
        {
            recyclerView.setVisibility( View.GONE );
            emptyView.setVisibility( View.VISIBLE );
        }
        else
        {
            recyclerView.setVisibility( View.VISIBLE );
            emptyView.setVisibility( View.GONE );
            recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
            recyclerView.setAdapter( new ScheduleAdapter( mixedList ) );
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
