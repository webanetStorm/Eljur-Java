package com.example.eljur.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eljur.R;
import com.example.eljur.databinding.FragmentProfileBinding;
import com.example.eljur.model.User;
import com.example.eljur.ui.auth.AuthActivity;
import com.example.eljur.util.UserManager;

import java.util.List;


public class ProfileFragment extends Fragment
{

    private FragmentProfileBinding binding;

    private User currentUser;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentProfileBinding.inflate( inflater, container, false );


        UserManager.logout( requireContext() );

        List<User> users = UserManager.loadUsers( requireContext() );
        User freshUser = users.get( 0 );

        UserManager.saveCurrentUser( requireContext(), freshUser );


        currentUser = UserManager.getCurrentUser( requireContext() );

        if ( currentUser == null )
        {
            startActivity( new Intent( requireContext(), AuthActivity.class ) );
            requireActivity().finish();

            return binding.getRoot();
        }

        setupProfile();

        binding.btnLogout.setOnClickListener( v ->
        {
            UserManager.logout( requireContext() );
            Toast.makeText( getContext(), "Выход из аккаунта", Toast.LENGTH_SHORT ).show();
            startActivity( new Intent( requireContext(), AuthActivity.class ) );
            requireActivity().finish();
        } );

        return binding.getRoot();
    }

    private void setupProfile()
    {
        binding.tvFullName.setText( currentUser.fullName );
        binding.tvRole.setText( "Ученик" );

        if ( currentUser.avatar != null && !currentUser.avatar.isEmpty() )
        {
            Log.i( "WEBANET", currentUser.avatar );
            Glide.with( requireContext() ).load( "file:///android_asset/" + currentUser.avatar ).circleCrop().into( binding.ivAvatar );
        }

        setRowValue( 0, "Пол", currentUser.gender );
        setRowValue( 1, "Возраст", String.valueOf( currentUser.age ) );
        setRowValue( 2, "Класс", currentUser.className );
        setRowValue( 3, "Классный руководитель", currentUser.classTeacher );
        setRowValue( 4, "Средний балл", "4.78" );
    }

    private void setRowValue( int index, String label, String value )
    {
        LinearLayout rowsContainer = binding.getRoot().findViewById( R.id.llProfileRows );
        View row = rowsContainer.getChildAt( index );

        TextView tvLabel = row.findViewById( R.id.tvLabel );
        TextView tvValue = row.findViewById( R.id.tvValue );

        tvLabel.setText( label );
        tvValue.setText( value );
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
