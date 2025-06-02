package com.example.eljur.ui.profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eljur.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.example.eljur.R;


public class ProfileFragment extends Fragment
{

    private FragmentProfileBinding binding;

    private DatabaseReference db;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentProfileBinding.inflate( inflater, container, false );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( user == null )
        {
            return binding.getRoot();
        }

        db = FirebaseDatabase.getInstance().getReference( "users" ).child( user.getUid() );

        db.addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot )
            {
                String fullName = snapshot.child( "fullName" ).getValue( String.class );
                String gender = snapshot.child( "gender" ).getValue( String.class );
                int age = snapshot.child( "age" ).getValue( Integer.class );
                String classId = snapshot.child( "classId" ).getValue( String.class );
                String teacher = snapshot.child( "teacher" ).getValue( String.class );
                String avatar = snapshot.child( "avatar" ).getValue( String.class );

                binding.tvFullName.setText( fullName );
                binding.tvRole.setText( "Ученик" );

                setRowValue( 0, "Пол", gender );
                setRowValue( 1, "Возраст", String.valueOf( age ) );
                setRowValue( 2, "Класс", classId );
                setRowValue( 3, "Классный руководитель", teacher );
                setRowValue( 4, "Средний балл", "-" );

                if ( avatar != null && !avatar.isEmpty() )
                {
                    Glide.with( requireContext() ).load( avatar ).circleCrop().into( binding.ivAvatar );
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error )
            {
            }
        } );

        binding.btnLogout.setOnClickListener( v ->
        {
            FirebaseAuth.getInstance().signOut();
            requireActivity().recreate();
        } );

        return binding.getRoot();
    }

    private void setRowValue( int index, String label, String value )
    {
        ViewGroup container = binding.llProfileRows;

        if ( index < container.getChildCount() )
        {
            View row = container.getChildAt( index );

            TextView tvLabel = row.findViewById( R.id.tvLabel );
            TextView tvValue = row.findViewById( R.id.tvValue );

            if ( tvLabel != null )
            {
                tvLabel.setText( label );
            }
            if ( tvValue != null )
            {
                tvValue.setText( value );
            }
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
