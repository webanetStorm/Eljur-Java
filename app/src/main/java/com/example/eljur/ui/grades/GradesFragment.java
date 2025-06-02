package com.example.eljur.ui.grades;


import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eljur.R;
import com.example.eljur.adapter.GradesByDateAdapter;
import com.example.eljur.adapter.GradesBySubjectAdapter;
import com.example.eljur.databinding.FragmentGradesBinding;
import com.example.eljur.model.Grade;
import com.example.eljur.model.Term;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;


public class GradesFragment extends Fragment
{

    private FragmentGradesBinding binding;

    private final List<Pair<Integer, Term>> termList = new ArrayList<>();

    private int selectedTermIndex = -1;

    private boolean isByDateView = true;

    private final List<String> dates = new ArrayList<>();

    private final Map<String, List<Pair<String, Grade>>> gradesByDate = new HashMap<>();

    private final Map<String, List<Grade>> gradesBySubject = new HashMap<>();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() );


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentGradesBinding.inflate( inflater, container, false );

        setupViewModeSelector();
        setupPeriodSelector();

        return binding.getRoot();
    }

    private void setupViewModeSelector()
    {
        binding.viewModeSelector.setOnCheckedChangeListener( ( group, checkedId ) ->
        {
            isByDateView = checkedId == R.id.rbByDate;
            updateGradesView();
        } );
    }

    private void setupPeriodSelector()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "terms" );

        ref.addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot )
            {
                RadioGroup group = binding.periodSelector;
                group.removeAllViews();
                termList.clear();

                LayoutInflater inflater = LayoutInflater.from( requireContext() );
                String today = dateFormat.format( new Date() );

                int currentTermIndex = -1;
                int index = 0;

                for ( DataSnapshot child : snapshot.getChildren() )
                {
                    if ( !child.exists() || child.getValue() == null )
                    {
                        continue;
                    }

                    Term term = child.getValue( Term.class );
                    termList.add( new Pair<>( index, term ) );

                    RadioButton rb = (RadioButton)inflater.inflate( R.layout.radio_button_item, group, false );
                    rb.setText( term.term_number + " триместр" );
                    rb.setId( View.generateViewId() );

                    int finalIndex = index;
                    rb.setOnClickListener( v ->
                    {
                        selectedTermIndex = finalIndex;
                        loadGrades();
                    } );

                    if ( today.compareTo( term.start_date ) >= 0 && today.compareTo( term.end_date ) <= 0 )
                    {
                        currentTermIndex = index;
                    }

                    group.addView( rb );
                    index++;
                }

                RadioButton rbAll = (RadioButton)inflater.inflate( R.layout.radio_button_item, group, false );
                rbAll.setText( "Год" );
                rbAll.setId( View.generateViewId() );
                rbAll.setOnClickListener( v ->
                {
                    selectedTermIndex = -1;
                    loadGrades();
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

            @Override
            public void onCancelled( @NonNull DatabaseError error )
            {
            }
        } );
    }

    private void loadGrades()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( user == null )
        {
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "grades" ).child( user.getUid() );

        ref.addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot )
            {
                dates.clear();
                gradesByDate.clear();
                gradesBySubject.clear();

                for ( DataSnapshot dateSnap : snapshot.getChildren() )
                {
                    String date = dateSnap.getKey();
                    if ( !isInSelectedTerm( date ) )
                    {
                        continue;
                    }

                    for ( DataSnapshot gradeSnap : dateSnap.getChildren() )
                    {
                        Grade grade = gradeSnap.getValue( Grade.class );
                        if ( grade == null || grade.subject == null )
                        {
                            continue;
                        }

                        gradesByDate.computeIfAbsent( date, k -> new ArrayList<>() ).add( new Pair<>( grade.subject, grade ) );
                        gradesBySubject.computeIfAbsent( grade.subject, k -> new ArrayList<>() ).add( grade );
                    }

                    if ( !dates.contains( date ) )
                    {
                        dates.add( date );
                    }
                }

                Collections.sort( dates, Collections.reverseOrder() );
                updateGradesView();
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error )
            {
            }
        } );
    }

    private boolean isInSelectedTerm( String date )
    {
        if ( selectedTermIndex == -1 || selectedTermIndex >= termList.size() )
        {
            return true;
        }

        Term term = termList.get( selectedTermIndex ).second;
        return date.compareTo( term.start_date ) >= 0 && date.compareTo( term.end_date ) <= 0;
    }

    private void updateGradesView()
    {
        if ( isByDateView )
        {
            GradesByDateAdapter adapter = new GradesByDateAdapter( dates, gradesByDate );
            binding.rvGrades.setLayoutManager( new LinearLayoutManager( requireContext() ) );
            binding.rvGrades.setAdapter( adapter );
        }
        else
        {
            List<String> subjects = new ArrayList<>( gradesBySubject.keySet() );
            GradesBySubjectAdapter adapter = new GradesBySubjectAdapter( subjects, gradesBySubject );
            binding.rvGrades.setLayoutManager( new LinearLayoutManager( requireContext() ) );
            binding.rvGrades.setAdapter( adapter );
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
