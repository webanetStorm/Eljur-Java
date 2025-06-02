package com.example.eljur;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;


public class Eljur extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled( true );
    }

}
