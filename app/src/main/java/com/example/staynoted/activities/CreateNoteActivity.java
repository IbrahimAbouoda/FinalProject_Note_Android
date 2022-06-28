package com.example.staynoted.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.staynoted.R;
import com.example.staynoted.adapters.ImagesAdapter;
import com.example.staynoted.model.Note;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class CreateNoteActivity extends AppCompatActivity {

    FirebaseFirestore mFirebaseFirestore;
    FirebaseAuth mFirebaseAuth;
    EditText etNoteTitle, etNoteText;
    ImageButton ivSave;
    private String defaultNoteColor;
    private LinearLayout llAddImage;
    private ActivityResultLauncher<Intent> uploadPhotoLauncher;
    private FirebaseStorage mFirebaseStorage;
    private DocumentReference currentNoteReference;
    private ArrayList<String> imgUrls;
    private RecyclerView rvImages;
    private ImagesAdapter imagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        currentNoteReference = mFirebaseFirestore.collection("users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("notes")
                .document();
        imgUrls = new ArrayList<>();
        rvImages = findViewById(R.id.rvImages);
        imagesAdapter = new ImagesAdapter(imgUrls);
        rvImages.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        rvImages.setAdapter(imagesAdapter);

        intiMiscellaneous();
        defaultNoteColor = "#333333";

        ivSave.setOnClickListener((view) -> {
            if (etNoteTitle.getText().toString().trim().length() > 0) {
                Note newNote = new Note();
                newNote.setNoteText(etNoteText.getText().toString());

                newNote.setId(currentNoteReference.getId());

                newNote.setDate(System.currentTimeMillis());

                newNote.setNoteTitle(etNoteTitle.getText().toString());

                newNote.setNoteColor(defaultNoteColor);

                newNote.setImgUrls(imgUrls);

                currentNoteReference.set(newNote);

                finish();
            } else {
                Toast.makeText(this, "Must at least set title", Toast.LENGTH_SHORT).show();
            }


        });

        llAddImage.setOnClickListener(v -> {
            Intent openPhotoPicker = new Intent();
            openPhotoPicker.setAction(Intent.ACTION_GET_CONTENT);
            openPhotoPicker.setType("image/jpeg");
            openPhotoPicker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            uploadPhotoLauncher.launch(openPhotoPicker);
        });

        uploadPhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri selectedImageUri = result.getData().getData();

                        StorageReference photoRef =
                                mFirebaseStorage.getReference()
                                        .child("users")
                                        .child(mFirebaseAuth.getUid())
                                        .child("notes")
                                        .child(currentNoteReference.getId())
                                        .child(selectedImageUri.getLastPathSegment());

                        UploadTask uploadTask = photoRef.putFile(selectedImageUri);
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return photoRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    imgUrls.add(downloadUri.toString());
                                    imagesAdapter.notifyDataSetChanged();
                                } else {
                                    System.out.println(task.getException().toString());
                                }
                            }
                        });
                    }
                }
        );

    }

    private void initViews() {
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteText = findViewById(R.id.etNote);
        ivSave = findViewById(R.id.ivSave);
        llAddImage = findViewById(R.id.llAddImage);
    }

    private void intiMiscellaneous() {
        final LinearLayout miscellaneousLinearLayout = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior =
                BottomSheetBehavior.from(miscellaneousLinearLayout);
        miscellaneousLinearLayout.findViewById(R.id.tvTextMiscellaneous)
                .setOnClickListener(view -> {
                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    else
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });

        final ImageView imgColor1 = miscellaneousLinearLayout.findViewById(R.id.img_Color1);
        final ImageView imgColor2 = miscellaneousLinearLayout.findViewById(R.id.img_Color2);
        final ImageView imgColor3 = miscellaneousLinearLayout.findViewById(R.id.img_Color3);
        final ImageView imgColor4 = miscellaneousLinearLayout.findViewById(R.id.img_Color4);
        final ImageView imgColor5 = miscellaneousLinearLayout.findViewById(R.id.img_Color5);

        miscellaneousLinearLayout.findViewById(R.id.view_Color1).setOnClickListener(
                view -> {
                    defaultNoteColor = "#333333";
                    imgColor1.setImageResource(R.drawable.ic_done);
                    imgColor2.setImageResource(0);
                    imgColor3.setImageResource(0);
                    imgColor4.setImageResource(0);
                    imgColor5.setImageResource(0);
                }
        );

        miscellaneousLinearLayout.findViewById(R.id.view_Color2).setOnClickListener(
                view -> {
                    defaultNoteColor = "#FDBE3B";
                    imgColor1.setImageResource(0);
                    imgColor2.setImageResource(R.drawable.ic_done);
                    imgColor3.setImageResource(0);
                    imgColor4.setImageResource(0);
                    imgColor5.setImageResource(0);
                }
        );

        miscellaneousLinearLayout.findViewById(R.id.view_Color3).setOnClickListener(
                view -> {
                    defaultNoteColor = "#FF4842";
                    imgColor1.setImageResource(0);
                    imgColor2.setImageResource(0);
                    imgColor3.setImageResource(R.drawable.ic_done);
                    imgColor4.setImageResource(0);
                    imgColor5.setImageResource(0);
                }
        );

        miscellaneousLinearLayout.findViewById(R.id.view_Color4).setOnClickListener(
                view -> {
                    defaultNoteColor = "#3A52Fc";
                    imgColor1.setImageResource(0);
                    imgColor2.setImageResource(0);
                    imgColor3.setImageResource(0);
                    imgColor4.setImageResource(R.drawable.ic_done);
                    imgColor5.setImageResource(0);
                }
        );

        miscellaneousLinearLayout.findViewById(R.id.view_Color5).setOnClickListener(
                view -> {
                    defaultNoteColor = "#000000";
                    imgColor1.setImageResource(0);
                    imgColor2.setImageResource(0);
                    imgColor3.setImageResource(0);
                    imgColor4.setImageResource(0);
                    imgColor5.setImageResource(R.drawable.ic_done);
                }
        );
    }
}