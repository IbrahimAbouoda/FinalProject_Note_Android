package com.example.staynoted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.staynoted.R;
import com.example.staynoted.adapters.NotesAdapter;
import com.example.staynoted.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore mFirebaseFirestore;
    FirebaseAuth mFirebaseAuth;
    ImageView ivAddNote;
    RecyclerView rvNotes;
    ArrayList<Note> noteArrayList;
    NotesAdapter notesAdapter;
    ProgressBar pBNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        ivAddNote.setOnClickListener((view) -> {
            startActivity(new Intent(getApplicationContext(), CreateNoteActivity.class));
        });

        noteArrayList = new ArrayList<>();

        rvNotes.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        notesAdapter = new NotesAdapter(noteArrayList);

        rvNotes.setAdapter(notesAdapter);

        updateRecyclerViewData();

    }

    private void initView() {
        ivAddNote = findViewById(R.id.ivAddNote);
        rvNotes = findViewById(R.id.rvNotes);
        rvNotes = findViewById(R.id.rvNotes);
        pBNotes = findViewById(R.id.pbNotes);
    }

    private void updateRecyclerViewData() {
        mFirebaseFirestore.collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("notes")
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        pBNotes.setVisibility(View.GONE);
                        noteArrayList.clear();
                        if (value != null) {
                            for (QueryDocumentSnapshot snapshot : value) {
                                System.out.println(snapshot.getData().get("text"));
                                Note note = snapshot.toObject(Note.class);
                                System.out.println(note.getDate());
                                note.setId(snapshot.getId());
                                noteArrayList.add(0, note);
                            }
                            notesAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}