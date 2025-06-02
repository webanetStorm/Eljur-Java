package com.example.eljur.ui.schedule;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.eljur.adapter.ScheduleAdapter;
import com.example.eljur.databinding.FragmentScheduleBinding;
import com.example.eljur.model.Grade;
import com.example.eljur.model.Schedule;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;


public class ScheduleFragment extends Fragment
{

    private FragmentScheduleBinding binding;

    private RecyclerView recyclerView;

    private LinearLayout emptyView;

    private ViewPager2 weekPager;

    private DatabaseReference dbRef;

    private String currentUserId;

    private String classId;

    private LocalDate selectedDate = LocalDate.now();


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentScheduleBinding.inflate( inflater, container, false );

        recyclerView = binding.rvSchedule;
        emptyView = binding.tvEmpty;
        weekPager = binding.vpWeeks;

        dbRef = FirebaseDatabase.getInstance().getReference();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        weekPager.setAdapter( new WeekPagerAdapter( this ) );
        weekPager.setCurrentItem( 1000, false );

        dbRef.child( "users" ).child( currentUserId ).child( "classId" ).get().addOnSuccessListener( snapshot ->
        {
            classId = snapshot.getValue( String.class );
            if ( classId != null )
            {
                loadSchedule( selectedDate );
            }
        } );

        return binding.getRoot();
    }

    public void updateDateIndicator( LocalDate anyDateOfWeek )
    {
        dbRef.child( "terms" ).get().addOnSuccessListener( termsSnapshot ->
        {
            String month = anyDateOfWeek.getMonth().getDisplayName( TextStyle.FULL_STANDALONE, new Locale( "ru" ) );
            int year = anyDateOfWeek.getYear();

            String trimester = "Неучебный период";
            for ( DataSnapshot term : termsSnapshot.getChildren() )
            {
                if ( !term.hasChild( "start_date" ) || !term.hasChild( "end_date" ) )
                {
                    continue;
                }

                LocalDate start = LocalDate.parse( term.child( "start_date" ).getValue( String.class ) );
                LocalDate end = LocalDate.parse( term.child( "end_date" ).getValue( String.class ) );

                if ( ( anyDateOfWeek.isEqual( start ) || anyDateOfWeek.isAfter( start ) ) && ( anyDateOfWeek.isEqual( end ) || anyDateOfWeek.isBefore( end ) ) )
                {
                    trimester = term.child( "term_number" ).getValue( Integer.class ) + " триместр";
                    break;
                }
            }

            String result = capitalizeFirst( month ) + " " + year + " · " + trimester;
            binding.tvDateIndicator.setText( result );
        } );
    }

    private String capitalizeFirst( String input )
    {
        return input == null || input.isEmpty() ? input : input.substring( 0, 1 ).toUpperCase() + input.substring( 1 );
    }

    public boolean isDateAllowed( LocalDate date, DataSnapshot academicSnapshot, DataSnapshot holidaysSnapshot )
    {
        boolean allowed = false;

        for ( DataSnapshot year : academicSnapshot.getChildren() )
        {
            LocalDate start = LocalDate.parse( year.child( "start_date" ).getValue( String.class ) );
            LocalDate end = LocalDate.parse( year.child( "end_date" ).getValue( String.class ) );
            if ( !date.isBefore( start ) && !date.isAfter( end ) )
            {
                allowed = true;
                break;
            }
        }

        if ( holidaysSnapshot.hasChild( date.toString() ) )
        {
            allowed = false;
        }

        return allowed;
    }

    public void loadSchedule( LocalDate date )
    {
        selectedDate = date;

        Task<DataSnapshot> academicTask = dbRef.child( "academic_year" ).get();
        Task<DataSnapshot> holidayTask = dbRef.child( "holidays" ).get();
        Task<DataSnapshot> scheduleTask = dbRef.child( "schedule" ).child( classId ).child( String.valueOf( date.getDayOfWeek().getValue() ) ).get();
        Task<DataSnapshot> lessonsTask = dbRef.child( "lessons" ).get();
        Task<DataSnapshot> homeworksTask = dbRef.child( "homeworks" ).child( classId ).child( date.toString() ).get();
        Task<DataSnapshot> gradesTask = dbRef.child( "grades" ).child( currentUserId ).child( date.toString() ).get();

        Tasks.whenAllSuccess( academicTask, holidayTask, scheduleTask, lessonsTask, homeworksTask, gradesTask ).addOnSuccessListener( results ->
        {

            DataSnapshot academicSnapshot = (DataSnapshot)results.get( 0 );
            DataSnapshot holidaysSnapshot = (DataSnapshot)results.get( 1 );
            DataSnapshot scheduleSnapshot = (DataSnapshot)results.get( 2 );
            DataSnapshot lessonsSnapshot = (DataSnapshot)results.get( 3 );
            DataSnapshot homeworksSnapshot = (DataSnapshot)results.get( 4 );
            DataSnapshot gradesSnapshot = (DataSnapshot)results.get( 5 );

            if ( !isDateAllowed( date, academicSnapshot, holidaysSnapshot ) )
            {
                updateUI( new ArrayList<>() );
                return;
            }

            List<Schedule> scheduleItems = new ArrayList<>();

            for ( DataSnapshot item : scheduleSnapshot.getChildren() )
            {
                int lessonNumber = item.child( "lessonNumber" ).getValue( Integer.class );
                String subject = item.child( "subject" ).getValue( String.class );
                String teacher = item.child( "teacher" ).getValue( String.class );
                String classroom = item.child( "classroom" ).getValue( String.class );

                String startTime = lessonsSnapshot.child( String.valueOf( lessonNumber ) ).child( "start_time" ).getValue( String.class );
                String endTime = lessonsSnapshot.child( String.valueOf( lessonNumber ) ).child( "end_time" ).getValue( String.class );

                String homework = null;
                for ( DataSnapshot hw : homeworksSnapshot.getChildren() )
                {
                    if ( subject.equals( hw.child( "subject" ).getValue( String.class ) ) )
                    {
                        homework = hw.child( "description" ).getValue( String.class );
                        break;
                    }
                }

                List<Grade> grades = new ArrayList<>();
                for ( DataSnapshot g : gradesSnapshot.getChildren() )
                {
                    if ( subject.equals( g.child( "subject" ).getValue( String.class ) ) )
                    {
                        grades.add( new Grade( "-", g.child( "value" ).getValue( Integer.class ), g.child( "weight" ).getValue( Integer.class ) ) );
                    }
                }

                scheduleItems.add( new Schedule( subject, teacher, classroom, startTime, endTime, homework, grades, lessonNumber ) );
            }

            scheduleItems.sort( Comparator.comparingInt( Schedule::getLessonNumber ) );
            updateUI( scheduleItems );
        } );
    }

    private int getBreakDuration( String endTime, String nextStartTime )
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "HH:mm" );
        LocalTime end = LocalTime.parse( endTime, formatter );
        LocalTime nextStart = LocalTime.parse( nextStartTime, formatter );
        return (int)java.time.Duration.between( end, nextStart ).toMinutes();
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

    public void getLessonCountIfAllowed( LocalDate date, OnCountReady callback )
    {
        dbRef.child( "academic_year" ).get().addOnSuccessListener( academicSnapshot ->
        {
            dbRef.child( "holidays" ).get().addOnSuccessListener( holidaysSnapshot ->
            {
                if ( !isDateAllowed( date, academicSnapshot, holidaysSnapshot ) )
                {
                    callback.onCountReady( 0 );
                    return;
                }

                dbRef.child( "schedule" ).child( classId ).child( String.valueOf( date.getDayOfWeek().getValue() ) ).get().addOnSuccessListener( scheduleSnap ->
                {
                    int count = (int)scheduleSnap.getChildrenCount();
                    callback.onCountReady( count );
                } );
            } );
        } );
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }


    public interface OnCountReady
    {

        void onCountReady( int count );

    }

}
