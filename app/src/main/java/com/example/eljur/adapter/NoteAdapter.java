package com.example.eljur.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eljur.R;
import com.example.eljur.model.AppDatabase;
import com.example.eljur.model.Note;

import java.util.List;
import java.util.Locale;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH>
{

    private final List<Note> notes;

    private final Context context;

    public NoteAdapter( Context ctx, List<Note> notes )
    {
        this.context = ctx;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteVH onCreateViewHolder( @NonNull ViewGroup parent, int viewType )
    {
        View v = LayoutInflater.from( context ).inflate( R.layout.item_note, parent, false );
        return new NoteVH( v );
    }

    @Override
    public void onBindViewHolder( @NonNull NoteVH holder, int position )
    {
        Note note = notes.get( position );
        holder.tvText.setText( note.text );
        holder.tvTime.setText( String.format( Locale.getDefault(), "%tF %tR", note.timestamp, note.timestamp ) );

        holder.itemView.setOnClickListener( v -> showEditDialog( note, position ) );
    }

    @Override
    public int getItemCount()
    {
        return notes.size();
    }

    private void showEditDialog( Note note, int position )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( "Редактировать заметку" );

        final EditText input = new EditText( context );
        input.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE );
        input.setText( note.text );
        builder.setView( input );

        builder.setPositiveButton( "Сохранить", ( dialog, which ) ->
        {
            String newText = input.getText().toString().trim();
            if ( newText.isEmpty() )
            {
                Toast.makeText( context, "Пустая заметка", Toast.LENGTH_SHORT ).show();
                return;
            }
            note.text = newText;
            note.timestamp = System.currentTimeMillis();
            AppDatabase.getInstance( context ).noteDao().update( note );
            notes.set( position, note );
            notifyItemChanged( position );
        } );
        builder.setNeutralButton( "Удалить", ( dialog, which ) ->
        {
            AppDatabase.getInstance( context ).noteDao().delete( note );
            notes.remove( position );
            notifyItemRemoved( position );
        } );
        builder.setNegativeButton( "Отмена", null );
        builder.show();
    }

    static class NoteVH extends RecyclerView.ViewHolder
    {

        TextView tvText, tvTime;

        NoteVH( @NonNull View itemView )
        {
            super( itemView );
            tvText = itemView.findViewById( R.id.tvNoteText );
            tvTime = itemView.findViewById( R.id.tvNoteTime );
        }

    }

}
