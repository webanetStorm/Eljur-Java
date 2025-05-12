package com.example.eljur.ui.assignments;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class AssignmentsViewModel extends ViewModel
{

    private final MutableLiveData<String> mText;


    public AssignmentsViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue( "Домашние задания" );
    }

    public LiveData<String> getText()
    {
        return mText;
    }

}
