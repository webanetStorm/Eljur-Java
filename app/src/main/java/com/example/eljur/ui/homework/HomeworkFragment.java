package com.example.eljur.ui.homework;


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
import com.example.eljur.model.Homework;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;


public class HomeworkFragment extends Fragment
{

    private FragmentHomeworkBinding binding;

    private boolean showUpcoming = true;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentHomeworkBinding.inflate( inflater, container, false );

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
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference( "users" ).child( uid );

        userRef.child( "classId" ).addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot )
            {
                String classId = snapshot.getValue( String.class );
                if ( classId != null && !classId.isEmpty() )
                {
                    loadHomeworkByClass( classId );
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error )
            {
            }
        } );
    }

    private void loadHomeworkByClass( String classId )
    {
        DatabaseReference hwRef = FirebaseDatabase.getInstance().getReference( "homeworks" ).child( classId );
        String today = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() ).format( new Date() );

        hwRef.addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot )
            {
                Map<String, List<Homework>> map = new LinkedHashMap<>();

                for ( DataSnapshot dateSnap : snapshot.getChildren() )
                {
                    String date = dateSnap.getKey();

                    if ( date == null )
                    {
                        continue;
                    }

                    boolean isAfter = date.compareTo( today ) >= 0;
                    if ( ( showUpcoming && !isAfter ) || ( !showUpcoming && isAfter ) )
                    {
                        continue;
                    }

                    List<Homework> hwList = new ArrayList<>();
                    for ( DataSnapshot hwSnap : dateSnap.getChildren() )
                    {
                        Homework hw = hwSnap.getValue( Homework.class );
                        if ( hw != null )
                        {
                            hwList.add( hw );
                        }
                    }

                    if ( !hwList.isEmpty() )
                    {
                        map.put( date, hwList );
                    }
                }

                List<String> sortedDates = new ArrayList<>( map.keySet() );
                Collections.sort( sortedDates );

                if ( !showUpcoming )
                {
                    Collections.reverse( sortedDates );
                }

                HomeworkAdapter adapter = new HomeworkAdapter( sortedDates, map );
                binding.rvHomeworks.setLayoutManager( new LinearLayoutManager( requireContext() ) );
                binding.rvHomeworks.setAdapter( adapter );
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error )
            {
            }
        } );
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
