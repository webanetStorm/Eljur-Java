package com.example.eljur.ui.assignments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eljur.databinding.FragmentAssignmentsBinding;


public class AssignmentsFragment extends Fragment
{

    private FragmentAssignmentsBinding binding;


    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        AssignmentsViewModel homeViewModel = new ViewModelProvider( this ).get( AssignmentsViewModel.class );

        binding = FragmentAssignmentsBinding.inflate( inflater, container, false );
        View root = binding.getRoot();

        final TextView textView = binding.textAssignments;
        homeViewModel.getText().observe( getViewLifecycleOwner(), textView::setText );

        return root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
