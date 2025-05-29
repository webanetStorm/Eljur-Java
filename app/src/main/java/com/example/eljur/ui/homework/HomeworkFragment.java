package com.example.eljur.ui.homework;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eljur.R;
import com.example.eljur.adapter.HomeworkAdapter;
import com.example.eljur.databinding.FragmentHomeworkBinding;
import com.example.eljur.db.DatabaseHelper;
import com.example.eljur.model.Homework;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeworkFragment extends Fragment
{

    private FragmentHomeworkBinding binding;

    private DatabaseHelper dbHelper;

    private boolean showUpcoming = true;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentHomeworkBinding.inflate( inflater, container, false );
        dbHelper = new DatabaseHelper( requireContext() );

        setupTabSelector();
        loadData();

        return binding.getRoot();
    }

    private void setupTabSelector()
    {
        binding.viewModeSelector.setOnCheckedChangeListener( ( group, checkedId ) ->
        {
            showUpcoming = checkedId == R.id.rbUpcoming;
            loadData();
        } );
    }

    private void loadData()
    {
        Map<String, List<Homework>> map = new LinkedHashMap<>();

        String today = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() ).format( new Date() );
        String filter = showUpcoming ? ">= ?" : "< ?";

        String query = "SELECT due_date, s.name, h.description FROM homework h " + "INNER JOIN subject s ON h.subject_id = s.id " + "WHERE due_date " + filter + " ORDER BY due_date";

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery( query, new String[] { today } );
        while ( cursor.moveToNext() )
        {
            String date = cursor.getString( 0 );
            String subject = cursor.getString( 1 );
            String desc = cursor.getString( 2 );

            map.computeIfAbsent( date, k -> new ArrayList<>() ).add( new Homework( subject, desc ) );
        }
        cursor.close();

        List<String> sortedDates = new ArrayList<>( map.keySet() );

        HomeworkAdapter adapter = new HomeworkAdapter( sortedDates, map );
        binding.rvHomeworks.setLayoutManager( new LinearLayoutManager( requireContext() ) );
        binding.rvHomeworks.setAdapter( adapter );
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
