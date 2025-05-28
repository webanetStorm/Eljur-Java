package com.example.eljur.ui.grades;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.eljur.databinding.FragmentGradesBinding;
import com.example.eljur.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.eljur.R;


public class GradesFragment extends Fragment
{

    private FragmentGradesBinding binding;

    private DatabaseHelper dbHelper;

    private final List<Integer> termIds = new ArrayList<>();

    private int selectedTermIndex = -1;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentGradesBinding.inflate( inflater, container, false );
        dbHelper = new DatabaseHelper( requireContext() );

        populatePeriods();
        
        return binding.getRoot();
    }

    private void populatePeriods()
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
            rb.setOnClickListener( v -> selectedTermIndex = termIds.size() );

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
        rbAll.setOnClickListener( v -> selectedTermIndex = -1 );
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

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}