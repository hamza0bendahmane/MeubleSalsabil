package com.createch.meublessalsabil.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.createch.meublessalsabil.Activity.Application;
import com.createch.meublessalsabil.Activity.Login;
import com.createch.meublessalsabil.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SettingFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("Users").document(user.getUid());
    ConstraintLayout logout_card, lang_card, infos_card;
    SimpleDraweeView imageView;
    MaterialButton about_app;
    TextView fullName, emailTextView, phoneTextView;
    Uri image_prof;
    ImageView edit_avatar_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        // ((BottomNavigationView)getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_settings);
        fullName = root.findViewById(R.id.full_name);
        emailTextView = root.findViewById(R.id.email);
        phoneTextView = root.findViewById(R.id.phone);
        imageView = root.findViewById(R.id.avatar_img);
        about_app = root.findViewById(R.id.aboutheapp);
        edit_avatar_img = root.findViewById(R.id.edit_avatar_img);
        logout_card = root.findViewById(R.id.logout_card);
        lang_card = root.findViewById(R.id.lang_card);
        infos_card = root.findViewById(R.id.infos_card);


        edit_avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage();
            }
        });

        infos_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment, new UserInfos())
                        .commit();
            }
        });
        lang_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(getContext());
                d.setContentView(R.layout.language);
                ImageView fr = d.findViewById(R.id.imagefr);
                ImageView ar = d.findViewById(R.id.imagear);
                if (Application.getCurrentLang(getContext()).equals("ar")) {
                    fr.setImageResource(R.drawable.check_off);
                    ar.setImageResource(R.drawable.check_on);

                } else {
                    fr.setImageResource(R.drawable.check_on);
                    ar.setImageResource(R.drawable.check_off);
                }
                d.findViewById(R.id.arabic_card).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Application.changeLang("ar", getActivity(), false);
                        refresh();
                        d.dismiss();
                    }
                });
                d.findViewById(R.id.francais_card).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Application.changeLang("fr", getActivity(), false);
                        refresh();
                        d.dismiss();
                    }
                });
                d.show();
            }
        });

        logout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setMessage("Are u sure want to logout ?");
                dialog.setButton(Dialog.BUTTON_POSITIVE, "logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), Login.class));
                        getActivity().finish();
                    }
                });
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        fetchInfos();
        about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment, new AboutApp()).addToBackStack("app")
                        .commit();
            }
        });
    }

    private void refresh() {
        FragmentManager fragmentManager = getParentFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragmentManager.beginTransaction().detach(this).commitNow();
            fragmentManager.beginTransaction().attach(this).commitNow();
        } else {
            fragmentManager.beginTransaction().detach(this).attach(this).commit();
        }
    }

    private void fetchInfos() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firestore Doc", "DocumentSnapshot data: " + document.getData());

                        String firstName = document.getString("fname");
                        String lastName = document.getString("lname");
                        String email = document.getString("email");
                        String phone = document.getString("phone");
                        String imageUrl = document.getString("photo");
                        imageView.setImageURI(imageUrl);
                        if (firstName != null && lastName != null)
                            fullName.setText(getString(R.string.full_name, firstName, lastName));
                        if (email != null)
                            emailTextView.setText(getString(R.string.email_display, email));
                        if (phone != null)
                            phoneTextView.setText(getString(R.string.phone_display, phone));
                    }
                }
            }
        });
    }

    private void CropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView prof_pic = getView().findViewById(R.id.avatar_img);
        prof_pic.setImageURI(data.getData());
        image_prof = data.getData();
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        image_prof = result.getUri();
        CheckAgain(prof_pic);

    }

    private void CheckAgain(ImageView prof_pic) {
        prof_pic.setImageURI(image_prof);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setMessage("Do u want to save the image");
        dialog.setButton(Dialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadPhotos(new ProgressDialog(getContext()));
            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                fetchInfos();
            }
        });
        dialog.show();
    }


    void uploadPhotos(ProgressDialog progressDialog0) {

        progressDialog0.setMessage("Uploading .... ");
        progressDialog0.show();

        byte[] data = null;

        // image compression
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_prof); // getting image from gallery
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 10, baos);
            data = baos.toByteArray();
        } catch (Exception e) {

        }

        final StorageReference childRef = FirebaseStorage.getInstance().getReference()
                .child("Images/" + user.getUid());
        final UploadTask uploadTask = childRef.putBytes(data);

        // Progress dialog box implemented
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int current_progress = (int) progress;
                progressDialog0.setProgress(current_progress);
            }
        });


        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return childRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String mUri = downloadUri.toString();
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("photo", mUri);

                                FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                                        .update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), R.string.succ, Toast.LENGTH_LONG).show();

                                        } else {
                                            String errMsg = task.getException().getMessage();
                                            Toast.makeText(getContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                                        }
                                        progressDialog0.dismiss();
                                    }
                                });
                            } else {
                                //Log.w("LOGIN", "signInWithCredential:failure", task.getException());
                                progressDialog0.dismiss();
                                String errMsg = task.getException().getMessage();
                                Toast.makeText(getContext(), "?????? ?????? ?????????? " + errMsg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    String errMsg = task.getException().getMessage();
                    Toast.makeText(getContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "?????? ?????????? " + e, Toast.LENGTH_LONG).show();
            }
        });

    }
}
