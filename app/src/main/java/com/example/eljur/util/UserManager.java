package com.example.eljur.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.eljur.model.User;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.List;


public class UserManager
{

    private static final String PREF_NAME = "user_prefs";

    private static final String KEY_CURRENT_USER = "current_user";


    public static List<User> loadUsers( Context context )
    {
        try
        {
            return new Gson().fromJson( new InputStreamReader( context.getAssets().open( "users.json" ) ), new TypeToken<List<User>>(){}.getType() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Ошибка загрузки users.json", e );
        }
    }

    public static void saveCurrentUser( Context context, User user )
    {
        SharedPreferences prefs = context.getSharedPreferences( PREF_NAME, Context.MODE_PRIVATE );
        prefs.edit().putString( KEY_CURRENT_USER, new Gson().toJson( user ) ).apply();
    }

    public static User getCurrentUser( Context context )
    {
        SharedPreferences prefs = context.getSharedPreferences( PREF_NAME, Context.MODE_PRIVATE );
        String json = prefs.getString( KEY_CURRENT_USER, null );

        return json != null ? new Gson().fromJson( json, User.class ) : null;
    }

    public static void logout( Context context )
    {
        SharedPreferences prefs = context.getSharedPreferences( PREF_NAME, Context.MODE_PRIVATE );
        prefs.edit().remove( KEY_CURRENT_USER ).apply();
    }

}

