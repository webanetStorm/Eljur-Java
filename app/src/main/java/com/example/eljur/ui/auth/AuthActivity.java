package com.example.eljur.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eljur.MainActivity;
import com.example.eljur.R;
import com.example.eljur.model.User;
import com.example.eljur.util.UserManager;

import java.util.List;


public class AuthActivity extends AppCompatActivity
{

    private EditText etLogin, etPassword;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );

        etLogin = findViewById( R.id.etLogin );
        etPassword = findViewById( R.id.etPassword );
        Button btnLogin = findViewById( R.id.btnLogin );

        btnLogin.setOnClickListener( v -> attemptLogin() );
    }

    private void attemptLogin()
    {
        String login = etLogin.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        List<User> users = UserManager.loadUsers( this );
        for ( User user : users )
        {
            if ( user.login.equals( login ) && user.password.equals( password ) )
            {
                UserManager.saveCurrentUser( this, user );
                startActivity( new Intent( this, MainActivity.class ) );
                finish();
                return;
            }
        }

        Toast.makeText( this, "Неверный логин или пароль", Toast.LENGTH_SHORT ).show();
    }

}
