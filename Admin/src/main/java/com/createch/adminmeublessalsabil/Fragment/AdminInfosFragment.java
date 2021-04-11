package com.createch.adminmeublessalsabil.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminInfosFragment extends Fragment {
    MaterialCardView pass, name, phone, adr, workh;
    MaterialButton submitname, submitadr, submitworkh, submitphone, submitpass;
    MaterialButton cancelname, canceladr, cancelworkh, cancelphone, cancelpass;
    TextInputEditText nameedit, adredit, passedit, vpassedit, ppassedit, workhedit, phoneedit;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("AdminInfos/");

    public AdminInfosFragment() {
        // Required empty public constructor
    }

    private void updateData(TextInputEditText editText, String reference) {
        String ss = editText.getText().toString().trim();
        if (ss.isEmpty())
            editText.setError(getString(R.string.shouldnt_beempty));
        else
            dbRef.child(reference).setValue(ss).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editText.setText("");
                }
            });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        pass = view.findViewById(R.id.pass_card);
        name = view.findViewById(R.id.name_card);
        phone = view.findViewById(R.id.phone_card);
        adr = view.findViewById(R.id.adr_card);
        workh = view.findViewById(R.id.workh_card);

        passedit = view.findViewById(R.id.edittext_pass);
        ppassedit = view.findViewById(R.id.edittext_opass);
        vpassedit = view.findViewById(R.id.edittext_vpass);
        nameedit = view.findViewById(R.id.edittext_name);
        phoneedit = view.findViewById(R.id.edittext_phone);
        adredit = view.findViewById(R.id.edittext_adr);
        workhedit = view.findViewById(R.id.edittext_workh);

        submitadr = view.findViewById(R.id.submit_adr);
        submitname = view.findViewById(R.id.submit_name);
        submitpass = view.findViewById(R.id.submit_pass);
        submitphone = view.findViewById(R.id.submit_phone);
        submitworkh = view.findViewById(R.id.submit_workh);

        canceladr = view.findViewById(R.id.cancel_adr);
        cancelname = view.findViewById(R.id.cancel_name);
        cancelpass = view.findViewById(R.id.cancel_pass);
        cancelphone = view.findViewById(R.id.cancel_phone);
        cancelworkh = view.findViewById(R.id.cancel_workh);

// update data ...................


        submitadr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateData(adredit, "adresse");
                // canceladr.performClick();
            }
        });

        submitname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(nameedit, "name");
                //  cancelname.performClick();

            }
        });

        submitpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = passedit.getText().toString().trim();
                String vp = vpassedit.getText().toString().trim();
                String op = ppassedit.getText().toString().trim();

                if (p.isEmpty())
                    passedit.setError(getString(R.string.shouldnt_beempty));
                else if (vp.isEmpty())
                    vpassedit.setError(getString(R.string.shouldnt_beempty));
                else if (op.isEmpty())
                    ppassedit.setError(getString(R.string.shouldnt_beempty));
                else if (!p.equals(vp)) {
                    passedit.setError(getString(R.string.shouldbe_same));
                    vpassedit.setError(getString(R.string.shouldbe_same));
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), op);

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Snackbar.make(getView(), getString(R.string.pass_updated), Snackbar.LENGTH_LONG).show();
                                                    ppassedit.setText("");
                                                    passedit.setText("");
                                                    vpassedit.setText("");
                                                    cancelpass.performClick();
                                                } else {
                                                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        ppassedit.setError(getString(R.string.pass_wrong));
                                    }
                                }
                            });
                }

            }
        });

        submitphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(phoneedit, "phone");
                //     cancelphone.performClick();

            }
        });

        submitworkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(workhedit, "start_wh");
                //    cancelworkh.performClick();

            }
        });


        // ...........

        canceladr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.adr).setVisibility(View.VISIBLE);
                view.findViewById(R.id.edit_adr_layout).setVisibility(View.GONE);
                view.findViewById(R.id.im4).setRotation(0);

            }
        });

        cancelname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.full_name).setVisibility(View.VISIBLE);
                view.findViewById(R.id.edit_name_layout).setVisibility(View.GONE);
                view.findViewById(R.id.im3).setRotation(0);

            }
        });

        cancelpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.password).setVisibility(View.VISIBLE);
                view.findViewById(R.id.edit_pass_layout).setVisibility(View.GONE);
                view.findViewById(R.id.im1).setRotation(0);

            }
        });

        cancelphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.phone).setVisibility(View.VISIBLE);
                view.findViewById(R.id.edit_phone_layout).setVisibility(View.GONE);
                view.findViewById(R.id.im2).setRotation(0);

            }
        });

        cancelworkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.workh).setVisibility(View.VISIBLE);
                view.findViewById(R.id.edit_workh_layout).setVisibility(View.GONE);
                view.findViewById(R.id.im5).setRotation(0);

            }
        });


        // ..............................................................................
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (view.findViewById(R.id.edit_pass_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.password).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_pass_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im1).setRotation(0);

                } else {
                    view.findViewById(R.id.password).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_pass_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im1).setRotation(270);

                }

            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_name_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.full_name).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_name_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im3).setRotation(0);

                } else {
                    view.findViewById(R.id.full_name).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_name_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im3).setRotation(270);

                }


            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_phone_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.phone).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_phone_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im2).setRotation(0);

                } else {
                    view.findViewById(R.id.phone).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_phone_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im2).setRotation(270);

                }

            }
        });
        adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_adr_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.adr).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_adr_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im4).setRotation(0);

                } else {
                    view.findViewById(R.id.adr).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_adr_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im4).setRotation(270);

                }

            }
        });
        workh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_workh_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.workh).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_workh_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im5).setRotation(0);

                } else {
                    view.findViewById(R.id.workh).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_workh_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im5).setRotation(270);

                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_infos, container, false);
    }

    private void updateInfos() {
        View thisv = getView();
        dbRef.child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ((TextView) thisv.findViewById(R.id.full_name)).setText(dataSnapshot.getValue().toString());
            }
        });
        dbRef.child("phone").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ((TextView) thisv.findViewById(R.id.phone)).setText(dataSnapshot.getValue().toString());
            }
        });
        dbRef.child("start_wh").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ((TextView) thisv.findViewById(R.id.workh)).setText(dataSnapshot.getValue().toString());
            }
        });
        dbRef.child("adresse").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ((TextView) thisv.findViewById(R.id.adr)).setText(dataSnapshot.getValue().toString());
            }
        });
        dbRef.child("email").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ((TextView) thisv.findViewById(R.id.email)).setText(dataSnapshot.getValue().toString());

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        updateInfos();
    }
}