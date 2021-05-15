package com.createch.meublessalsabil.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.Adapter.WilayaAdapter;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Adresse;
import com.createch.meublessalsabil.models.User;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfos extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("Users").document(user.getUid());
    public static String wilaya = "";
    RelativeLayout block_the_account;
    Spinner wilaya_spinner;
    TextView fullName, adrTextView, phoneTextView;
    MaterialCardView pass, name, phone, adr;
    MaterialButton submitname, submitadr, submitphone, submitpass;
    MaterialButton cancelname, canceladr, cancelphone, cancelpass;
    TextInputEditText lnameedit, fnameedit, adr1edit, zipcode, adr2edit, baladia, passedit, vpassedit, ppassedit, phoneedit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_infos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fullName = view.findViewById(R.id.full_name);
        adrTextView = view.findViewById(R.id.adrTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);
        block_the_account = view.findViewById(R.id.block_the_account);
        pass = view.findViewById(R.id.pass_card);
        name = view.findViewById(R.id.name_card);
        phone = view.findViewById(R.id.phone_card);
        wilaya_spinner = view.findViewById(R.id.wilaya_spinner);
        adr = view.findViewById(R.id.adr_card);
        passedit = view.findViewById(R.id.edittext_pass);
        ppassedit = view.findViewById(R.id.edittext_opass);
        vpassedit = view.findViewById(R.id.edittext_vpass);
        fnameedit = view.findViewById(R.id.fedittext_name);
        lnameedit = view.findViewById(R.id.edittext_name);
        phoneedit = view.findViewById(R.id.edittext_phone);
        zipcode = view.findViewById(R.id.zipcode);
        adr1edit = view.findViewById(R.id.adr1);
        adr2edit = view.findViewById(R.id.adr2);
        baladia = view.findViewById(R.id.baladia);

        submitadr = view.findViewById(R.id.submit_adr);
        submitname = view.findViewById(R.id.submit_name);
        submitpass = view.findViewById(R.id.submit_pass);
        submitphone = view.findViewById(R.id.submit_phone);

        canceladr = view.findViewById(R.id.cancel_adr);
        cancelname = view.findViewById(R.id.cancel_name);
        cancelpass = view.findViewById(R.id.cancel_pass);
        cancelphone = view.findViewById(R.id.cancel_phone);
        fetchInfos();
        WilayaAdapter adapter = new WilayaAdapter(getContext(), getResources().getStringArray(R.array.wilayas));
        wilaya_spinner.setAdapter(adapter);
        wilaya_spinner.setSelection(15);


        wilaya_spinner.setPrompt(" Wilayas");
        wilaya_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wilaya = adapter.getItem(position);
                Log.d("hbhb", "onItemSelected: " + adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wilaya = adapter.getItem(15);
            }
        });


        submitadr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bala = baladia.getText().toString().trim();
                String zip = zipcode.getText().toString().trim();
                String adr2 = adr2edit.getText().toString().trim();
                String adr1 = adr1edit.getText().toString().trim();

                // canceladr.performClick();
                if (bala.isEmpty()) {
                    baladia.setError(getString(R.string.empty));
                    return;
                }
                if (adr1.isEmpty()) {
                    adr1edit.setError(getString(R.string.empty));
                    return;
                }
                if (zip.isEmpty()) {
                    zipcode.setError(getString(R.string.empty));
                    return;
                }
                documentReference.update("adresse", new Adresse(adr1, adr2, Integer.parseInt(zip), bala, wilaya)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        baladia.setText("");
                        adr1edit.setText("");
                        adr2edit.setText("");
                        zipcode.setText("");
                        canceladr.performClick();
                    }
                });

            }
        });

        submitname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(null, "fname", fnameedit);
                updateData(cancelname, "lname", lnameedit);

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
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), op);
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
                updateData(cancelphone, "phone", phoneedit);
                //     cancelphone.performClick();

            }
        });


        // ...........

        canceladr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.adrTextView).setVisibility(View.VISIBLE);
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
                view.findViewById(R.id.phoneTextView).setVisibility(View.VISIBLE);
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
                    view.findViewById(R.id.phoneTextView).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_phone_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im2).setRotation(0);

                } else {
                    view.findViewById(R.id.phoneTextView).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_phone_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im2).setRotation(270);

                }

            }
        });
        adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_adr_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.adrTextView).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_adr_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im4).setRotation(0);

                } else {
                    view.findViewById(R.id.adrTextView).setVisibility(View.INVISIBLE);
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
        FirebaseDatabase.getInstance().getReference().child("BlockedUsers").
                child(user.getUid()).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),R.string.succ,Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });
    }

    private void fetchInfos() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firestore Doc", "DocumentSnapshot data: " + document.getData());
                        User in = document.toObject(User.class);
                        String firstName = in.getFname();
                        String lastName = in.getLname();
                        String adr = in.getAdresse().getBaladia();
                        String phone = in.getPhone();
                        fullName.setText(getString(R.string.full_name, firstName, lastName));
                        adrTextView.setText(getString(R.string.email_display, adr));
                        phoneTextView.setText(getString(R.string.phone_display, phone));
                    }
                }
            }
        });
    }

    private void updateData(final Button cancelbu, String reference, TextInputEditText... editTexts) {
        String ss = "";
        boolean k = true;
        for (TextInputEditText editText : editTexts) {
            ss = editText.getText().toString().trim();
            if (ss.isEmpty()) {
                editText.setError(getString(R.string.empty));
                k = false;
                break;
            }

        }

        if (k)
            documentReference.update(reference, ss).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    for (TextInputEditText editText : editTexts)
                        editText.setText("");
                    if (cancelbu != null)
                        cancelbu.performClick();
                }

            });

    }

}