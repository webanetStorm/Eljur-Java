package com.example.eljur;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.eljur.databinding.ActivityMainBinding;
import com.example.eljur.model.User;
import com.example.eljur.util.UserManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;


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


        MaterialToolbar toolbar = findViewById( R.id.toolbar );
        TextView userNameText = toolbar.findViewById( R.id.user_name );
        ShapeableImageView avatarImage = toolbar.findViewById( R.id.avatar );

        User currentUser = UserManager.getCurrentUser( this );

        if ( currentUser != null )
        {
            userNameText.setText( currentUser.fullName );

            if ( currentUser.avatar != null && !currentUser.avatar.isEmpty() )
            {
                Glide.with( this ).load( "file:///android_asset/" + currentUser.avatar ).circleCrop().into( avatarImage );
            }
        }
    }


}
