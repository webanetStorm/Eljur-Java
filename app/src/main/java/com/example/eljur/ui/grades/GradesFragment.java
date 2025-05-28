package com.example.eljur.ui.grades;


import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.adapter.GradesByDateAdapter;
import com.example.eljur.adapter.GradesBySubjectAdapter;
import com.example.eljur.databinding.FragmentGradesBinding;
import com.example.eljur.db.DatabaseHelper;
import com.example.eljur.model.Grade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Locale;
import java.util.Map;


public class GradesFragment extends Fragment
{

    private FragmentGradesBinding binding;

    private DatabaseHelper dbHelper;

    private final List<Integer> termIds = new ArrayList<>();

    private int selectedTermIndex = -1;

    private boolean isByDateView = true;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentGradesBinding.inflate( inflater, container, false );
        dbHelper = new DatabaseHelper( requireContext() );

        setupPeriodSelector();
        setupViewModeSelector();
        updateGradesView();

        return binding.getRoot();
    }

    private void setupPeriodSelector()
    {
        RadioGroup group = binding.periodSelector;
        group.removeAllViews();
        termIds.clear();

        LayoutInflater inflater = LayoutInflater.from( requireContext() );
        String currentDate = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() ).format( new Date() );

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery( "SELECT id, term_number, start_date, end_date FROM term ORDER BY term_number", null );
        int currentTermIndex = -1;
        int index = 0;

        while ( cursor.moveToNext() )
        {
            int termId = cursor.getInt( 0 );
            int termNumber = cursor.getInt( 1 );
            String startDate = cursor.getString( 2 );
            String endDate = cursor.getString( 3 );

            RadioButton rb = (RadioButton)inflater.inflate( R.layout.radio_button_item, group, false );
            rb.setText( termNumber + " триместр" );
            rb.setId( View.generateViewId() );
            rb.setOnClickListener( v ->
            {
                selectedTermIndex = termIds.indexOf( termId );
                updateGradesView();
            } );

            termIds.add( termId );
            group.addView( rb );

            if ( currentDate.compareTo( startDate ) >= 0 && currentDate.compareTo( endDate ) <= 0 )
            {
                currentTermIndex = index;
            }

            index++;
        }
        cursor.close();

        RadioButton rbAll = (RadioButton)inflater.inflate( R.layout.radio_button_item, group, false );
        rbAll.setText( "Год" );
        rbAll.setId( View.generateViewId() );
        rbAll.setOnClickListener( v ->
        {
            selectedTermIndex = -1;
            updateGradesView();
        } );
        group.addView( rbAll );

        if ( currentTermIndex != -1 )
        {
            ( (RadioButton)group.getChildAt( currentTermIndex ) ).setChecked( true );
            selectedTermIndex = currentTermIndex;
        }
        else
        {
            rbAll.setChecked( true );
            selectedTermIndex = -1;
        }
    }

    private void setupViewModeSelector()
    {
        binding.viewModeSelector.setOnCheckedChangeListener( ( group, checkedId ) ->
        {
            isByDateView = checkedId == R.id.rbByDate;
            updateGradesView();
        } );
    }

    private void updateGradesView()
    {
        if ( isByDateView )
        {
            showGradesByDate();
        }
        else
        {
            showSubjectsList();
        }
    }

    private void showGradesByDate()
    {
        List<String> dates = new ArrayList<>();
        Map<String, List<Pair<String, Grade>>> gradesMap = new HashMap<>();

        String termFilter = "";
        if ( selectedTermIndex >= 0 && selectedTermIndex < termIds.size() )
        {
            int termId = termIds.get( selectedTermIndex );
            termFilter = "AND g.term_id = " + termId;
        }

        String query = "SELECT g.date, s.name, g.value, g.weight " + "FROM grade g " + "INNER JOIN subject s ON g.subject_id = s.id " + "WHERE 1=1 " + termFilter + " ORDER BY g.date DESC";

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, null );
        while ( cursor.moveToNext() )
        {
            String date = cursor.getString( 0 );
            String subject = cursor.getString( 1 );
            int value = cursor.getInt( 2 );
            int weight = cursor.getInt( 3 );

            if ( !gradesMap.containsKey( date ) )
            {
                gradesMap.put( date, new ArrayList<>() );
                dates.add( date );
            }

            gradesMap.get( date ).add( new Pair<>( subject, new Grade( value, weight ) ) );
        }
        cursor.close();

        GradesByDateAdapter adapter = new GradesByDateAdapter( dates, gradesMap );
        binding.rvGrades.setLayoutManager( new LinearLayoutManager( requireContext() ) );
        binding.rvGrades.setAdapter( adapter );
    }

    private void showSubjectsList()
    {
        List<String> subjectNames = new ArrayList<>();
        Map<String, List<Grade>> gradesMap = new HashMap<>();

        String termFilter = "";
        if ( selectedTermIndex >= 0 && selectedTermIndex < termIds.size() )
        {
            int termId = termIds.get( selectedTermIndex );
            termFilter = "AND g.term_id = " + termId;
        }

        String query = "SELECT s.name, g.value, g.weight " + "FROM grade g " + "INNER JOIN subject s ON g.subject_id = s.id " + "WHERE 1 = 1 " + termFilter + " ORDER BY s.name";

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, null );
        while ( cursor.moveToNext() )
        {
            String subject = cursor.getString( 0 );
            int value = cursor.getInt( 1 );
            int weight = cursor.getInt( 2 );

            if ( !gradesMap.containsKey( subject ) )
            {
                gradesMap.put( subject, new ArrayList<>() );
                subjectNames.add( subject );
            }
            gradesMap.get( subject ).add( new Grade( value, weight ) );
        }
        cursor.close();

        binding.rvGrades.setLayoutManager( new LinearLayoutManager( requireContext() ) );
        binding.rvGrades.setAdapter( new GradesBySubjectAdapter( subjectNames, gradesMap ) );
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
