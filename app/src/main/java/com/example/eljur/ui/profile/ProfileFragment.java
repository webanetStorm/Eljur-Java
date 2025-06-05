package com.example.eljur.ui.profile;


import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eljur.R;
import com.example.eljur.adapter.NoteAdapter;
import com.example.eljur.databinding.FragmentProfileBinding;
import com.example.eljur.model.AppDatabase;
import com.example.eljur.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.List;
import java.util.Locale;


public class ProfileFragment extends Fragment
{

    private FragmentProfileBinding binding;

    private DatabaseReference dbUser;


    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        binding = FragmentProfileBinding.inflate( inflater, container, false );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( user == null )
        {
            return binding.getRoot();
        }

        dbUser = FirebaseDatabase.getInstance().getReference( "users" ).child( user.getUid() );

        dbUser.addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot )
            {
                String fullName = snapshot.child( "fullName" ).getValue( String.class );
                String gender = snapshot.child( "gender" ).getValue( String.class );
                Integer age = snapshot.child( "age" ).getValue( Integer.class );
                String classId = snapshot.child( "classId" ).getValue( String.class );
                String teacher = snapshot.child( "classTeacher" ).getValue( String.class );
                String avatarUrl = snapshot.child( "avatar" ).getValue( String.class );

                binding.tvFullName.setText( fullName );
                binding.tvRole.setText( "Ученик" );

                setRowValue( 0, "Пол", gender );
                setRowValue( 1, "Возраст", age != null ? age.toString() : "-" );
                setRowValue( 2, "Класс", classId );
                setRowValue( 3, "Классный руководитель", teacher );
                loadAverageMark( user.getUid() );
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

        List<Note> allNotes = AppDatabase.getInstance( requireContext() ).noteDao().getAllNotes();
        NoteAdapter adapter = new NoteAdapter( requireContext(), allNotes );
        binding.rvNotes.setLayoutManager( new LinearLayoutManager( requireContext() ) );
        binding.rvNotes.setAdapter( adapter );

        binding.btnAddNote.setOnClickListener( v ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder( requireContext() );
            builder.setTitle( "Новая заметка" );

            EditText input = new EditText( requireContext() );
            input.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE );
            builder.setView( input );

            builder.setPositiveButton( "Сохранить", ( dialog, which ) ->
            {
                String text = input.getText().toString().trim();
                if ( text.isEmpty() )
                {
                    Toast.makeText( requireContext(), "Пустая заметка", Toast.LENGTH_SHORT ).show();
                    return;
                }
                long now = System.currentTimeMillis();
                Note note = new Note( text, now );
                long newId = AppDatabase.getInstance( requireContext() ).noteDao().insert( note );
                note.id = (int)newId;
                allNotes.add( 0, note );
                adapter.notifyItemInserted( 0 );
                binding.rvNotes.scrollToPosition( 0 );
            } );
            builder.setNegativeButton( "Отмена", null );
            builder.show();
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

    private void loadAverageMark( String uid )
    {
        DatabaseReference gradesRef = FirebaseDatabase.getInstance().getReference( "grades" ).child( uid );

        gradesRef.addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange( @NonNull DataSnapshot snap )
            {
                int sum = 0, weightSum = 0;
                for ( DataSnapshot dateNode : snap.getChildren() )
                {
                    for ( DataSnapshot gradeNode : dateNode.getChildren() )
                    {
                        Integer v = gradeNode.child( "value" ).getValue( Integer.class );
                        Integer w = gradeNode.child( "weight" ).getValue( Integer.class );
                        if ( v != null && w != null )
                        {
                            sum += v * w;
                            weightSum += w;
                        }
                    }
                }
                String avg = ( weightSum == 0 ) ? "-" : String.format( Locale.getDefault(), "%.2f", (double)sum / weightSum );
                setRowValue( 4, "Средний балл", avg );
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error )
            {
            }
        } );
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}
