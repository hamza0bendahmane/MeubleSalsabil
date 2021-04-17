package com.createch.adminmeublessalsabil.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.Adapter.ImagesAdapter;
import com.createch.adminmeublessalsabil.Adapter.ProductsAdapter;
import com.createch.adminmeublessalsabil.Model.Item;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCollection extends AppCompatActivity {

    public   ArrayList<Item> selected_items = new ArrayList<>();
    ProductsAdapter adapter ;
    ImagesAdapter ada ;
    int successCount = 0;
    List<String> selected_images,selected_uris,selected_colors,selected_materials,selected_contents ;
    MaterialButton addProduct,cancel , submit,addImage;
    TextInputEditText description , quantity,price,name;
    RecyclerView products ,images ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        selected_uris = new ArrayList<>();
        selected_images = new ArrayList<String>();
        selected_colors =new ArrayList<String>();
        selected_materials =new ArrayList<String>();
        selected_contents =new ArrayList<String>();




        products = findViewById(R.id.pro_picked);
        description = findViewById(R.id.width_produ);
        addProduct = findViewById(R.id.add_material);
        cancel = findViewById(R.id.cancel_name);
        submit = findViewById(R.id.submit_name);
        addImage = findViewById(R.id.add_pic);
        price = findViewById(R.id.price_produ);
        quantity = findViewById(R.id.quant_produ);
        name = findViewById(R.id.name_prod);
        images = findViewById(R.id.pro_pic);

        setupRecyclerImages();
        setUpRecyclerProducts();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setUpRecyclerProducts() {


        Query query = FirebaseFirestore.getInstance().collection("Products");
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        adapter = new ProductsAdapter(options, getApplicationContext(),"remove",selected_items);

        products.setHasFixedSize(true);
        products.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
        products.setAdapter(adapter);


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
            selected_images.add(data.getData().toString());
            ada.notifyDataSetChanged();
        }
    }

    private void setupRecyclerImages() {

        ada = new ImagesAdapter(selected_images,"remove");
        images.setHasFixedSize(true);
        images.setLayoutManager(new LinearLayoutManager(this));
        images.setAdapter(ada);

    }
    public void addColl(View vv){

        String names = name.getText().toString().trim();
        String descrips = description.getText().toString().trim();
        String quantitys = quantity.getText().toString().trim();
        String prices = price.getText().toString().trim();


        if (names.isEmpty()) {
            name.requestFocus();
            name.setError(getString(R.string.shouldnt_beempty));
        }
        if (descrips.isEmpty()) {
            description.requestFocus();
            description.setError(getString(R.string.shouldnt_beempty));
        }
        if (prices.isEmpty()) {
            price.requestFocus();
            price.setError(getString(R.string.shouldnt_beempty));
        }
        if (quantitys.isEmpty()) {
            quantity.requestFocus();
            quantity.setError(getString(R.string.shouldnt_beempty));
        }
    if (selected_images.isEmpty()) {
            Snackbar.make(vv, "اضف على الاقل صورة واحدة ", Snackbar.LENGTH_SHORT).show();
        }
        /*if (selected_items.isEmpty()) {
            Snackbar.make(vv,"اضف على الاقل منتج واحد ", Snackbar.LENGTH_SHORT).show();
        }*/else  {

            final ProgressDialog progressDialog0 = new ProgressDialog(this);
            progressDialog0.setTitle(R.string.saving_data);
            progressDialog0.setCancelable(false);
            progressDialog0.show();
            ArrayList<Uri> uris = new ArrayList<>();
            for (String im : selected_images ) {
                Uri imuri = Uri.parse(im);
                uris.add(imuri);

            }
        uploadPhotos(progressDialog0,uris,submit,vv, names, descrips, prices, quantitys);


        }

    }
    void uploadfields(ProgressDialog progressDialog0,View vv,String names,String desc,String prices
    , String quantitys){
        final Long ts_long = System.currentTimeMillis() / 1000;
            HashMap<String, Object> data = new HashMap<>();
            selected_items = adapter.getSelected_items();
        for (Item ii:selected_items ) {
            selected_materials.addAll(ii.getMaterials());
            selected_colors.addAll(ii.getColors());
            selected_contents.add(ii.getName());


        }
            data.put("name", names);
            data.put("items",selected_items);
            data.put("colors", selected_colors);
            data.put("contents",selected_contents);
            data.put("materials", selected_materials);
            data.put("images", selected_uris);
            data.put("price", Long.parseLong(prices));
            data.put("quantity", Long.parseLong(quantitys));
            data.put("description", desc);
            FirebaseFirestore.getInstance().collection("Collections").document(ts_long.toString())
                    .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                   //     onBackPressed();
                        Snackbar.make(vv,"تم الرفع بنجاح ", Snackbar.LENGTH_SHORT).show();

                    } else {
                        String errMsg = task.getException().getLocalizedMessage();
                        Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                    }
                    progressDialog0.dismiss();
                }
            });

    }

    void uploadPhotos(ProgressDialog progressDialog,ArrayList<Uri> uris,MaterialButton upload  ,View vv,String names,String desc,String prices
            , String quantitys){

                progressDialog.setMessage("Uploading .... ");
                progressDialog.show();
                upload.setClickable(false); // disable upload button whilst uploading

                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images");
                final List<Uri> clonedImageList =uris;

                int imageListSize = selected_images.size();

                List<Task<Uri>> uploadedImageUrlTasks = new ArrayList<>(imageListSize);

                for (Uri imageUri : clonedImageList) {
                    final String imageFilename = imageUri.getLastPathSegment();
                    Log.d("upoad.onClick()", "Starting upload for \"" + imageFilename + "\"...");
                    String tii = String.valueOf(System.currentTimeMillis() / 1000);
                    StorageReference imageRef = storageReference.child(tii); // Warning: potential for collisions/overwrite
                    UploadTask currentUploadTask = imageRef.putFile(imageUri);

                    Task<Uri> currentUrlTask = currentUploadTask
                            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        Log.d("upload.onClick()", "Upload for \"" + imageFilename + "\" failed!");
                                        throw task.getException(); // rethrow any errors
                                    }

                                    Log.d("upload.onClick()", "Upload for \"" + imageFilename + "\" finished. Fetching download URL...");
                                    return imageRef.getDownloadUrl();
                                }
                            })
                            .continueWithTask(new Continuation<Uri, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<Uri> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        Log.d("upload.onClick()", "Could not get download URL for \"" + imageFilename + "\"!");
                                        throw task.getException(); // rethrow any errors
                                    }

                                    Log.d("upload.onClick()", "Download URL for \"" + imageFilename + "\" is \"" + task.getResult() + "\".");
                                    return task;
                                }
                            });

                    uploadedImageUrlTasks.add(currentUrlTask);
                }

                // At this point, all the files are being uploaded in parallel
                // Each upload is tracked by the tasks in uploadedImageUrlTasks

                Tasks.whenAllComplete(uploadedImageUrlTasks)
                        .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                                int tasksCount = uploadedImageUrlTasks.size();
                                List<String> failedUploads = new ArrayList<>();
                                selected_uris.clear(); // empty old entries?

                                for (Task<Uri> taskk : uploadedImageUrlTasks) {
                                    if (task.isSuccessful()) {
                                        successCount++;
                                        Uri downloadUri = taskk.getResult();
                                        selected_uris.add(downloadUri.toString());
                                    } else {
                                        Uri imageUri = clonedImageList.get(uploadedImageUrlTasks.indexOf(taskk));
                                        failedUploads.add(imageUri.toString());
                                        Log.e("upload.onClick()", "Failed to upload/fetch URL for \"" + imageUri.getLastPathSegment() + "\" with exception", task.getException()); // log exception
                                    }
                                }

                                progressDialog.dismiss(); // dismiss upload dialog

                                if (failedUploads.size() > 0) {
                                    Toast.makeText(getApplicationContext(), failedUploads.size() + "/" + tasksCount + " uploads failed.", Toast.LENGTH_LONG).show();
                                    // TODO: Do something with list of failed uploads such as readd to the now empty upload list
                                    selected_images.addAll(failedUploads);
                                    upload.setClickable(true);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Successfully uploaded all " + tasksCount + " files.", Toast.LENGTH_LONG).show();
                                }
                                uploadfields(progressDialog,vv, names, desc, prices, quantitys);

                                // TODO: Now that imageAddressList has been updated, update the UI - e.g tell recycler view to refresh

                            }
                        });




    }
}