package com.example.eljur;


import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.eljur.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment_activity_main );
        NavigationUI.setupWithNavController( binding.bottomNavigation, navController );
    }

}
