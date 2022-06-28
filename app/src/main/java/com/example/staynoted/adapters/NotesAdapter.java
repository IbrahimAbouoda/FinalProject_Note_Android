package com.example.staynoted.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.staynoted.R;
import com.example.staynoted.model.Note;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    ArrayList<Note> noteArrayList;


    public NotesAdapter(ArrayList<Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_note, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.tvNoteText.setText(noteArrayList.get(position).getNoteText());
        holder.tvNoteTitle.setText(noteArrayList.get(position).getNoteTitle());
        holder.tvNoteDate.setText(simpleDateFormat.format(new Timestamp(noteArrayList.get(position).getDate()).getTime()));
        GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutNote.getBackground();
        gradientDrawable.setColor(Color.parseColor(noteArrayList.get(position).getNoteColor()));
        ImagesAdapter imagesAdapter = new ImagesAdapter(noteArrayList.get(position).getImgUrls());
        holder.rvImages.setAdapter(imagesAdapter);
        if (noteArrayList.get(position).getImgUrls().size() > 0) {
            holder.rvImages.setLayoutManager(
                    new StaggeredGridLayoutManager(
                            noteArrayList.get(position).getImgUrls().size(),
                            StaggeredGridLayoutManager.VERTICAL)
            );
        }
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoteTitle, tvNoteText, tvNoteDate;
        LinearLayout layoutNote;
        RecyclerView rvImages;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNoteText = itemView.findViewById(R.id.tvNoteText);
            tvNoteDate = itemView.findViewById(R.id.tvNoteDate);
            layoutNote = itemView.findViewById(R.id.llNote);
            rvImages = itemView.findViewById(R.id.rvImages);
        }
    }
}
