package com.createch.meublessalsabil.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.createch.meublessalsabil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class SettingFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("Users").document(user.getUid());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        final TextView fullName = (TextView) root.findViewById(R.id.full_name);
        final TextView emailTextView = (TextView) root.findViewById(R.id.email);
        final TextView phoneTextView = (TextView) root.findViewById(R.id.phone);
        final ImageView imageView = (ImageView) root.findViewById(R.id.avatar_img);

        //fetch user data
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Log.d("Firestore Doc","DocumentSnapshot data: "+ document.getData());

                        String firstName = document.getString("first");
                        String lastName = document.getString("last");
                        String email = document.getString("email");
                        String phone = document.getString("phone");
                        String imageUrl = document.getString("imageUrl");

                        if (firstName != null && lastName != null) fullName.setText(getString(R.string.full_name,firstName,lastName));
                        if (email != null) emailTextView.setText(getString(R.string.email_display,email));
                        if (phone != null) phoneTextView.setText(getString(R.string.phone_display,phone));
                    }
                }
            }
        });

        /*TODO*/
        //fetch imageUrl
        //modify image
        //navigate to personal info
        //navigate to lang
        //logout

        return root;
    }
}