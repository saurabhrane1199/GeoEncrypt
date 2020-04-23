package com.encrypto.android.geoencrypt;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.encrypto.android.geoencrypt.database.TableController;
import com.encrypto.android.geoencrypt.geoCryptology.geoCryptology;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 4;
    private static final int DECRYPT_FILE_REQUEST = 5;
    Context context = null;
    private byte[] Key;
    Button addFiles,viewFiles;
    private static final int WRITE_EXST = 44;

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
        setContentView(R.layout.activity_main);
        addFiles = findViewById(R.id.encryptFiles);
        viewFiles = findViewById(R.id.vexst);
        addFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFileToEncrypt();
            }
        });
        viewFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFileToDecrypt();
            }
        });
    }

    public void selectFileToEncrypt(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    public void selectFileToDecrypt(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, DECRYPT_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST) {
            Uri fullFileUri = data.getData();
//            Log.d("ACTIVITY_RESULT", fullFileUri.toString());
//            String filePath = getFileName(fullFileUri);
//            Log.d("FILE_PATH", filePath);
            encryptFiles(fullFileUri);
        }
        else if(requestCode == DECRYPT_FILE_REQUEST){
            Uri fullFileUri = data.getData();
//            Log.d("ACTIVITY_RESULT", fullFileUri.toString());
//            String filePath = getFileName(fullFileUri);
//            Log.d("FILE_PATH", filePath);
            decryptFile(fullFileUri);
        }
    }

    private void encryptFiles(Uri file){

        final Uri filePath = file;
        class addingFiles extends AsyncTask<Void,Void,Void> {

            private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setTitle("Adding File");
                progressDialog.setMessage("Encrypting...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMax(100);
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    geoCryptology.encryptFile(filePath,context);
                } catch (BadPaddingException | FileNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void result){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"File Added",Toast.LENGTH_SHORT).show();
            }
        }
        addingFiles ad = new addingFiles();
        ad.execute();
    }


    private void decryptFile(Uri file){

        final Uri filePath = file;
        class decryptingFiles extends AsyncTask<Void,Void,Void> {

            private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setTitle("Adding File");
                progressDialog.setMessage("Encrypting...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMax(100);
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    geoCryptology.decryptFile(filePath,context);
                } catch (BadPaddingException | FileNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void result){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"File Added",Toast.LENGTH_SHORT).show();
            }
        }
        decryptingFiles df = new decryptingFiles();
        df.execute();
    }















    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }



}
