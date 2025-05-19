package com.example.eljur;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.navigation.fragment.NavHostFragment;

import com.example.eljur.databinding.ActivityMainBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity
{

    private ActivityMainBinding binding;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        BottomNavigationView navView = findViewById( R.id.bottom_navigation );

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder( R.id.navigation_schedule, R.id.navigation_grades, R.id.navigation_assignments, R.id.navigation_profile ).build();
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment_activity_main );
        NavigationUI.setupWithNavController( binding.bottomNavigation, navController );
    }

}
