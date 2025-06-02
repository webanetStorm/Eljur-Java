package com.example.eljur;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.eljur.databinding.ActivityMainBinding;
import com.example.eljur.ui.auth.AuthActivity;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        if ( FirebaseAuth.getInstance().getCurrentUser() == null )
        {
            startActivity( new Intent( this, AuthActivity.class ) );
            finish();
            return;
        }

        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment_activity_main );
        NavigationUI.setupWithNavController( binding.bottomNavigation, navController );
    }

}
