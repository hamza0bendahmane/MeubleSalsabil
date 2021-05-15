package com.createch.adminmeublessalsabil.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.Adapter.ColorsAdapter;
import com.createch.adminmeublessalsabil.Adapter.MaterialsAdapter;
import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProduct extends AppCompatActivity {
    ColorsAdapter adapter;
    MaterialsAdapter ada;
    String theCategory = "";
    List<String> selected_colors, selected_materials;
    Spinner prodType;
    MaterialButton addColor, addMaterial, cancel, submit, addImage;
    TextInputEditText length, width, height, quantity, price, name;
    RecyclerView colors, materials;

    ImageView pro_pic;
    String[] categories = {"BUREAU SEMI METAL TYPE CIE",
            "BUREAU TRAVAIL SEMI METAL",
            "BUREAU A/RET SEMI METAL",
            "FAUTEUIL DE RECEPTION (06 d'une place)",
            "SALON DE RECEPTION",
            "SALON 03 PIECES",
            "SALON 04 PIECES",
            "SALON 04 PIECES AVEC TABLE HG",
            "SALON 04 PIECES EN CUIR",
            "SALON 04 PLACES",
            "SALON 05 PLACES",
            "SALON 05 PLACES EN CUIR",
            "SALON 07 PIECES",
            "SALON 07 PLACES EN CUIR",
            "SALLE A MANGER OS PLACES",
            "ARMOIR DE RANGEMENT",
            "ARMOIR DE BUREAU METALIQUE",
            "ESTRADE DE TABLEAU",
            "MEUBLE DE RANGEMENT CARTES",
            "TV LCD 107 CM",
            "PORTE MANTEAUX MURAL",
            "TABLEAU MURAL A VOLETS",
            "RAYONNAGE METALIQUE",
            "TABLEAU BLANC",
            "TABLEAU MURAL",
            "TABLEAU MOBILE",
            "TABLEAU MURAL BLANC",
            "TABLE SCOLAIRE EN BM",
            "TABLEAU MAGNETIQUE",
            "TABLEAU PIVOTANT BLANC",
            "TABLE DE DESSIN",
            "CHAMBRE D'ENFANT",
            "SALLE A MANGER",
            "SALON 04 PIECES",
            "TABLE DE SALON",
            "SALON 03 PIECES AVEC TABLE",
            "MEUBLE TV",
            "LIE BEBE",
            "CANAPE",
            "TAPIS DE SALON",
            "COUVRE LIT 01 PLACE",
            "COUVREUT OZ PLACES ",
            "COLLA CUAD CAMERA",
            "OREILLE O2 PLACES"};
    private Uri image_uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        selected_colors = new ArrayList<String>();
        selected_materials = new ArrayList<String>();

        adapter = new ColorsAdapter(selected_colors, "remove", "");

        colors = findViewById(R.id.colors_picked);
        prodType = findViewById(R.id.spinner_categ);
        addColor = findViewById(R.id.add_color);
        addMaterial = findViewById(R.id.add_material);
        cancel = findViewById(R.id.cancel_name);
        submit = findViewById(R.id.submit_name);
        addImage = findViewById(R.id.add_pic);
        length = findViewById(R.id.length_produ);
        width = findViewById(R.id.width_produ);
        height = findViewById(R.id.height_produ);
        price = findViewById(R.id.price_produ);
        quantity = findViewById(R.id.quant_produ);
        name = findViewById(R.id.name_prod);
        materials = findViewById(R.id.materials_names);
        pro_pic = findViewById(R.id.pro_pic);


        setSpinners();
        setUpRecyclerColors();
        setUpRecyclerNames();

    }

    private void setUpRecyclerNames() {

         ada = new MaterialsAdapter(this,selected_materials);
        materials.setHasFixedSize(true);
        materials.setLayoutManager(new GridLayoutManager(this, 3));
        materials.setAdapter(ada);

    }

    public void cancel(View vv){

        onBackPressed();
    }
    public void pick_image(View vv) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null && data.getData() != null) {
            pro_pic.setImageURI(data.getData());
            image_uri = data.getData();
        }
    }
    private void setUpRecyclerColors() {

        colors.setHasFixedSize(true);
        colors.setLayoutManager(new GridLayoutManager(this,5));
        colors.setAdapter(adapter);



    }

    private void setSpinners() {



        prodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    theCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter bb = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        prodType.setAdapter(bb);

    }


    public void addProduct(View vv){

            String names = name.getText().toString().trim();
        String lengths = length.getText().toString().trim();
        String widths = width.getText().toString().trim();
        String heights = height.getText().toString().trim();
        String quantitys = quantity.getText().toString().trim();
        String prices = price.getText().toString().trim();


        if (names.isEmpty()) {
            name.requestFocus();
            name.setError(getString(R.string.shouldnt_beempty));
        }
        if (lengths.isEmpty()) {
            length.requestFocus();
            length.setError(getString(R.string.shouldnt_beempty));
        }
        if (widths.isEmpty()) {
            width.requestFocus();
            width.setError(getString(R.string.shouldnt_beempty));
        }
        if (heights.isEmpty()) {
            height.requestFocus();
            height.setError(getString(R.string.shouldnt_beempty));
        }
        if (prices.isEmpty()) {
            price.requestFocus();
            price.setError(getString(R.string.shouldnt_beempty));
        }
        if (quantitys.isEmpty()) {
            quantity.requestFocus();
            quantity.setError(getString(R.string.shouldnt_beempty));
        }
        if (image_uri == null) {
          Snackbar.make(vv,"قم باضافة صورة",Snackbar.LENGTH_SHORT).show();
        } if (selected_materials.isEmpty()) {
            Snackbar.make(vv, "اضف على الاقل مادة واحدة ", Snackbar.LENGTH_SHORT).show();
        }
        if (selected_colors.isEmpty()) {
            Snackbar.make(vv,"اضف على الاقل لون واحد ", Snackbar.LENGTH_SHORT).show();
        }else  {

                                final ProgressDialog progressDialog0 = new ProgressDialog(this);
                                progressDialog0.setTitle(R.string.saving_data);
                                progressDialog0.setCancelable(false);
                                progressDialog0.show();
                                            byte[] data = null;

                                            // image compression
                                            try {
                                                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri); // getting image from gallery
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                bmp.compress(Bitmap.CompressFormat.PNG, 10, baos);
                                                data = baos.toByteArray();
                                            } catch (Exception e) {

                                            }

                                            final Long ts_long = System.currentTimeMillis() / 1000;
                                            final String ts = ts_long.toString();
                                            final StorageReference childRef = FirebaseStorage.getInstance().getReference().child("Images/"  + "prod" +ts );
                                            final UploadTask uploadTask = childRef.putBytes(data);

                                            // Progress dialog box implemented
                                            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                    int current_progress = (int) progress;
                                                    progressDialog0.setProgress(current_progress);
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                                                    data.put("name", names);
                                                                    data.put("materials",selected_materials);
                                                                    data.put("colors", selected_colors);
                                                                    data.put("image", mUri);
                                                                    data.put("category", theCategory);
                                                                    data.put("length", Long.parseLong(lengths));
                                                                    data.put("height", Long.parseLong(heights));
                                                                    data.put("price", Long.parseLong(prices));
                                                                    data.put("quantity", Long.parseLong(quantitys));
                                                                    data.put("width", Long.parseLong(widths));
                                                                    FirebaseFirestore.getInstance().collection("Products").document(ts).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                onBackPressed();
                                                                                Snackbar.make(vv,"تم الرفع بنجاح ", Snackbar.LENGTH_SHORT).show();

                                                                            } else {
                                                                                String errMsg = task.getException().getLocalizedMessage();
                                                                                Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                                                                            }
                                                                            progressDialog0.dismiss();
                                                                        }
                                                                    });
                                                                } else {
                                                                    //Log.w("LOGIN", "signInWithCredential:failure", task.getException());
                                                                    progressDialog0.dismiss();
                                                                    String errMsg = task.getException().getMessage();
                                                                    Toast.makeText(getApplicationContext(), "خطأ رفع الملف " + errMsg, Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        String errMsg = task.getException().getMessage();
                                                        Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "فشل الرفع " + e, Toast.LENGTH_LONG).show();
                                                }
                                            });
        }
        
    }



    public void addMaterial(View vv){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_material_custom, null);
        dialogBuilder.setView(dialogView);

        EditText editText =  dialogView.findViewById(R.id.addmaterial);
        Button cc =  dialogView.findViewById(R.id.cc);
        Button ss =  dialogView.findViewById(R.id.ss);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().isEmpty())
                    editText.setError(getString(R.string.shouldnt_beempty));
                else {
                    if (!selected_materials.contains(editText.getText().toString().trim()))
                        selected_materials.add(editText.getText().toString().trim());
                    ada.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

    }

    public void addColor(View vv) {

        //  AlertDialog dialog = new AlertDialog.Builder(this,R.layout.add_material_custom.xml).create();
        Dialog d = new Dialog(this);
        List<String> listOfColors = new ArrayList<>();
        listOfColors.add("#DFFF00");
        listOfColors.add("#FFBF00");
        listOfColors.add("#FF7F50");
        listOfColors.add("#FFFFFF");
        listOfColors.add("#000000");
        listOfColors.add("#DE3163");
        listOfColors.add("#6495ED");
        listOfColors.add("#40E0D0");
        listOfColors.add("#FF0000");
        listOfColors.add("#800000");
        listOfColors.add("#800080");
        d.setContentView(R.layout.colors_layout);
        RecyclerView colors_recyc = d.findViewById(R.id.colors_recycler);
        colors_recyc.setHasFixedSize(true);
        colors_recyc.setLayoutManager(new GridLayoutManager(this, 5));
        ColorsAdapter ad = new ColorsAdapter(listOfColors, "", "#ff0000");
        colors_recyc.setAdapter(ad);
        d.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selected_colors.contains(ad.getColor()))
                    selected_colors.add(ad.getColor());
                adapter.notifyDataSetChanged();
                d.dismiss();
            }
        });
        d.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }
}