package com.example.eljur.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eljur.MainActivity;
import com.example.eljur.databinding.ActivityAuthBinding;
import com.google.firebase.auth.FirebaseAuth;


public class AuthActivity extends AppCompatActivity
{

    private ActivityAuthBinding binding;

    private FirebaseAuth auth;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        binding = ActivityAuthBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener( v ->
        {
            String email = binding.etLogin.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if ( email.isEmpty() || password.isEmpty() )
            {
                Toast.makeText( this, "Введите логин и пароль", Toast.LENGTH_SHORT ).show();
                return;
            }

            auth.signInWithEmailAndPassword( email, password ).addOnSuccessListener( result ->
            {
                startActivity( new Intent( this, MainActivity.class ) );
                finish();
            } ).addOnFailureListener( e -> Toast.makeText( this, "Ошибка авторизации", Toast.LENGTH_LONG ).show() );
        } );
    }

}
