package com.createch.adminmeublessalsabil.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.Adapter.ColorsAdapter;
import com.createch.adminmeublessalsabil.Adapter.MaterialsAdapter;
import com.createch.adminmeublessalsabil.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditProduct extends AppCompatActivity {

    ColorsAdapter adapter ;
    MaterialsAdapter ada ;
    String theCategory = "" ;
    List<String> selected_colors , selected_materials ;
    Spinner prodType;
    MaterialButton addColor ,addMaterial,cancel , submit,addImage;
    TextInputEditText length ,width,height , quantity,price,name;
    RecyclerView colors ,materials ;
    String urlimage ;
    ImageView pro_pic;
    String[] categories = {"طاولة","كرسي","باب","خزانة"};
    String refer ;
    private Uri image_uri = null;
    private Uri old_image_uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        refer = getIntent().getStringExtra("ref");

        selected_colors = new ArrayList<String>();
        selected_materials =new ArrayList<String>();


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
        fillTheInfos();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushChanges();
            }
        });




    }



    private void fillTheInfos() {

        FirebaseFirestore.getInstance().collection("Products").document(refer)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                 urlimage = documentSnapshot.get("image").toString();
                selected_materials = (List<String>) documentSnapshot.get("materials");
                selected_colors = (ArrayList<String>) documentSnapshot.get("colors");
                old_image_uri = Uri.parse(urlimage);
                theCategory = documentSnapshot.get("category").toString() ;
                name.setText(documentSnapshot.get("name").toString());
                height.setText(documentSnapshot.get("height").toString());
                width.setText(documentSnapshot.get("width").toString());
                length.setText(documentSnapshot.get("length").toString());
                price.setText(documentSnapshot.get("price").toString());
                quantity.setText(documentSnapshot.get("quantity").toString());


                // image ....
                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(100, 100) ;
                Glide.with(getApplicationContext()).load(old_image_uri).apply(requestOptions).into(pro_pic);
                // colors
                adapter = new ColorsAdapter(selected_colors,"remove");
                colors.setHasFixedSize(true);
                colors.setLayoutManager(new GridLayoutManager(getApplicationContext(),5));
                colors.setAdapter(adapter);




                // materials ..
                ada = new MaterialsAdapter(getApplicationContext(),selected_materials);
                materials.setHasFixedSize(true);
                materials.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                materials.setAdapter(ada);
                // spinner ...
                prodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        theCategory = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter bb = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                prodType.setAdapter(bb);
                prodType.setSelection(bb.getPosition(theCategory));
            }
        });
    }



    public void pushChanges(){
        View vv =name ;

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
        if (selected_materials.isEmpty()) {
            Snackbar.make(vv, "اضف على الاقل مادة واحدة ", Snackbar.LENGTH_SHORT).show();
        }
        if (selected_colors.isEmpty()) {
            Snackbar.make(vv,"اضف على الاقل لون واحد ", Snackbar.LENGTH_SHORT).show();
        }else  {

            final ProgressDialog progressDialog0 = new ProgressDialog(this);
            progressDialog0.setTitle(R.string.saving_data);
            progressDialog0.setCancelable(false);
            progressDialog0.show();

            if (image_uri!= null){

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

                                                uploadFields(task,  vv , progressDialog0, refer , names,
                                                        lengths , heights, prices , quantitys, widths);


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
                Log.d("hbhb", "addProduct: they are diff ");
            }else{

                Log.d("hbhb", "addProduct: they are the same ");

            }
            uploadFields(  vv , progressDialog0, refer , names,
                    lengths , heights, prices , quantitys, widths);

        }

    }

    private void uploadFields(Task<Uri> task, View vv ,ProgressDialog progressDialog0,String ts ,String names,
                              String lengths ,String heights,String prices ,String quantitys,String widths ) {
        Uri downloadUri = task.getResult();
        String mUri = downloadUri.toString();
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", names);
        data.put("materials",selected_materials);
        data.put("colors", selected_colors);
        data.put("image", mUri);
        data.put("category", theCategory);
        data.put("length", Double.parseDouble(lengths));
        data.put("height", Double.parseDouble(heights));
        data.put("price", Double.parseDouble(prices));
        data.put("quantity", Double.parseDouble(quantitys));
        data.put("width", Double.parseDouble(widths));
        FirebaseFirestore.getInstance().collection("Products").document(ts).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
startActivity(new Intent(getApplicationContext(),AdminPanelActivity.class));
Snackbar.make(vv,"تم الرفع بنجاح ", Snackbar.LENGTH_SHORT).show();

                } else {
                    String errMsg = task.getException().getLocalizedMessage();
                    Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                }
                progressDialog0.dismiss();
            }
        });
    }


    private void uploadFields( View vv ,ProgressDialog progressDialog0,String ts ,String names,
                              String lengths ,String heights,String prices ,String quantitys,String widths ) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", names);
        data.put("materials",selected_materials);
        data.put("colors", selected_colors);
        data.put("category", theCategory);
        data.put("length", Double.parseDouble(lengths));
        data.put("height", Double.parseDouble(heights));
        data.put("price", Double.parseDouble(prices));
        data.put("quantity", Double.parseDouble(quantitys));
        data.put("width", Double.parseDouble(widths));
        FirebaseFirestore.getInstance().collection("Products").document(ts).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(),AdminPanelActivity.class));
                    Snackbar.make(vv,"تم الرفع بنجاح ", Snackbar.LENGTH_SHORT).show();

                } else {
                    String errMsg = task.getException().getLocalizedMessage();
                    Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                }
                progressDialog0.dismiss();
            }
        });
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
                    selected_materials.add(editText.getText().toString().trim());
                    ada.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

    }

    public void addColor(View vv){

        //  AlertDialog dialog = new AlertDialog.Builder(this,R.layout.add_material_custom.xml).create();
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).noSliders()
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //    changeBackgroundColor(selectedColor);
                        String substring = Integer.toHexString(selectedColor);
                        selected_colors.add("#"+ substring);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}