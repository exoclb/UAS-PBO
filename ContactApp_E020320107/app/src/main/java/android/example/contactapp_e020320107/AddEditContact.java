 package android.example.contactapp_e020320107;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

    public class AddEditContact extends AppCompatActivity {


    private ImageView profileIv;
    private EditText nameEt,phoneEt,emailEt,noteEt;
    private FloatingActionButton fab;

    // String variable
        String name,phone,email,note;

        // action bar
        ActionBar actionBar;

        // permission constant
        private static final int CAMERA_PERMISSION_CODE = 100;
        private static final int STORAGE_PERMISSION_CODE = 200;
        private static final int IMAGE_FROM_GALLERY_CODE = 300;
        private static final int IMAGE_FROM_CAMERA_CODE = 400;

        // string array permission
        private String[] cameraPermission;
        private String[] storagePermission;

        // Image uri var
        Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        // init permission
        cameraPermission = new  String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        // init action bar
        actionBar = getSupportActionBar();

        // set title in action bar
        actionBar.setTitle("Add Contact");

        // back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // init view
        profileIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        noteEt = findViewById(R.id.noteEt);
        fab = findViewById(R.id.fab);

        // add even handler
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

    }

        private void showImagePickerDialog() {

            // option for dialog
            String options[] = {"Camera","Gallery"};

            // Alert dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // setTitle
            builder.setTitle("Choose An Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // handle item click
                    if (which == 0){ // start from 0 index
                        // camera selected
                        if (!checkCameraPermission()){
                            // request camera permission
                            requestCameraPermission();
                        }else {
                            pickFromCamera();
                        }
                    } else if (which == 1){
                        // gallery selected
                        if (!checkStoragePermission()){
                            // request storage permission
                            requestStoragePermission();
                        } else {
                            PickFromGallery();
                        }
                    }
                }
            }).create().show();
        }

        private void PickFromGallery() {
            // intent for taking image from gallery
            Intent  galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*"); // only image

            startActivityForResult(galleryIntent,IMAGE_FROM_GALLERY_CODE);
        }

        private void pickFromCamera() {

        // Content Values for image info
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
            values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");

            // save ImageUri
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

            // intent to open camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

            startActivityForResult(cameraIntent,IMAGE_FROM_CAMERA_CODE);
        }

        private void saveData() {

        // take user give data in variable
            name = nameEt.getText().toString();
            phone= phoneEt.getText().toString();
            email = emailEt.getText().toString();
            note = noteEt.getText().toString();

            // check filed data
            if (!name.isEmpty() || !phone.isEmpty() || !email.isEmpty() || !note.isEmpty()){
                // save data, if user have only one data
                // function for save data on SQLite database
            } else {
                // show toast message
                Toast.makeText(getApplicationContext(), "Nothing to save..", Toast.LENGTH_SHORT).show();
            }
        }

        // ctr + O

        // back button click
        @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return super.onSupportNavigateUp();
        }

        // check camera permission
        private boolean checkCameraPermission(){
            boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)    ==  (PackageManager.PERMISSION_GRANTED);
            boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)    ==  (PackageManager.PERMISSION_GRANTED);

            return result & result1;
        }

        // request for camera permission
        private void requestCameraPermission(){
            ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE);
        }

        // check storage permission
        private boolean checkStoragePermission(){
            boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)    ==  (PackageManager.PERMISSION_GRANTED);

           return result1;
        }

        // request for camera permission
        private void requestStoragePermission(){
            ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE); // handle request permission on override method
        }

        // handel request permission
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch (requestCode){
                case CAMERA_PERMISSION_CODE:
                    if  (grantResults.length >0){

                        // if all permission allowed return true, otherwise false
                        boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if  (cameraAccepted && storageAccepted){
                            // both permission granted
                            pickFromCamera();
                        } else {
                            // permission not granted
                            Toast.makeText(getApplicationContext(), "Camera & Storage Permission needed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case STORAGE_PERMISSION_CODE:
                    if  (grantResults.length >0){

                        // if all permission allowed return true, otherwise false
                        boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if  (storageAccepted){
                            // permission granted
                            pickFromGallery();
                        } else {
                            // permission not granted
                            Toast.makeText(getApplicationContext(), "Storage Permission needed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }

        private void pickFromGallery() {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");

            startActivityForResult(galleryIntent,IMAGE_FROM_GALLERY_CODE);
        }


}