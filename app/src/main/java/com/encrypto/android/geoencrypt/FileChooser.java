package com.encrypto.android.geoencrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FileChooser extends AppCompatActivity {

    static final int REQUEST_FILE_OPEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);
    }
}
