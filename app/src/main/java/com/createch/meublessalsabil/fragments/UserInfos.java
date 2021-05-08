package com.createch.meublessalsabil.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfos extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("Users").document(user.getUid());
    TextView fullName, emailTextView, phoneTextView;
    RelativeLayout block_the_account;
    MaterialCardView pass, name, phone, adr, workh;
    MaterialButton submitname, submitadr, submitworkh, submitphone, submitpass;
    MaterialButton cancelname, canceladr, cancelworkh, cancelphone, cancelpass;
    TextInputEditText nameedit, adredit, passedit, vpassedit, ppassedit, workhedit, phoneedit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_infos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fullName = view.findViewById(R.id.full_name);
        emailTextView = view.findViewById(R.id.emailTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);
        block_the_account = view.findViewById(R.id.block_the_account);
        pass = view.findViewById(R.id.pass_card);
        name = view.findViewById(R.id.name_card);
        phone = view.findViewById(R.id.phone_card);
        adr = view.findViewById(R.id.adr_card);

        passedit = view.findViewById(R.id.edittext_pass);
        ppassedit = view.findViewById(R.id.edittext_opass);
        vpassedit = view.findViewById(R.id.edittext_vpass);
        nameedit = view.findViewById(R.id.edittext_name);
        phoneedit = view.findViewById(R.id.edittext_phone);
        adredit = view.findViewById(R.id.edittext_adr);

        submitadr = view.findViewById(R.id.submit_adr);
        submitname = view.findViewById(R.id.submit_name);
        submitpass = view.findViewById(R.id.submit_pass);
        submitphone = view.findViewById(R.id.submit_phone);

        canceladr = view.findViewById(R.id.cancel_adr);
        cancelname = view.findViewById(R.id.cancel_name);
        cancelpass = view.findViewById(R.id.cancel_pass);
        cancelphone = view.findViewById(R.id.cancel_phone);
        fetchInfos();


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
                    passedit.setError(getString(R.string.empty));
                else if (vp.isEmpty())
                    vpassedit.setError(getString(R.string.empty));
                else if (op.isEmpty())
                    ppassedit.setError(getString(R.string.empty));
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
                                                    Snackbar.make(getView(), getString(R.string.updated), Snackbar.LENGTH_LONG).show();
                                                    ppassedit.setText("");
                                                    passedit.setText("");
                                                    vpassedit.setText("");
                                                    cancelpass.performClick();
                                                } else {
                                                    Toast.makeText(getContext(), getString(R.string.password_error), Toast.LENGTH_LONG).show();
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


        // ...........

        canceladr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.emailTextView).setVisibility(View.VISIBLE);
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
                    view.findViewById(R.id.emailTextView).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_adr_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im4).setRotation(0);

                } else {
                    view.findViewById(R.id.emailTextView).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_adr_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im4).setRotation(270);

                }

            }
        });


        block_the_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setMessage("Are u sure want to Deactivate your Account ?");
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Yes , Iam sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeactivateHim();
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
    }

    private void DeactivateHim() {
    /*    UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(user.getUid())
                .setDisabled(true);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(),Login.class));
        try {
            FirebaseAuth.getInstance().updateUser(request);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }*/
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
                        fullName.setText(getString(R.string.full_name, firstName, lastName));
                        emailTextView.setText(getString(R.string.email_display, email));
                        phoneTextView.setText(getString(R.string.phone_display, phone));
                    }
                }
            }
        });
    }

    private void updateData(TextInputEditText editText, String reference) {
        String ss = editText.getText().toString().trim();
        if (ss.isEmpty())
            editText.setError(getString(R.string.empty));

        else
            documentReference.update(reference, ss).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editText.setText("");
                }
            });


    }
}