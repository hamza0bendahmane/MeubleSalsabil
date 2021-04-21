package com.createch.meublessalsabil.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("Users").document(user.getUid());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        final TextView fullName = root.findViewById(R.id.full_name);
        final TextView emailTextView = root.findViewById(R.id.email);
        final TextView phoneTextView = root.findViewById(R.id.phone);
        final ImageView imageView = root.findViewById(R.id.avatar_img);
        final MaterialButton about_app = root.findViewById(R.id.aboutheapp);
        //fetch user data
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firestore Doc", "DocumentSnapshot data: " + document.getData());

                        String firstName = document.getString("first");
                        String lastName = document.getString("last");
                        String email = document.getString("email");
                        String phone = document.getString("phone");
                        String imageUrl = document.getString("imageUrl");

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
        about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new AboutApp())
                        .commit();
            }
        });
        /*TODO*/
        //fetch imageUrl
        //modify image
        //navigate to personal info
        //navigate to lang
        //logout
    }
}
