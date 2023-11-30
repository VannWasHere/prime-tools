package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class OpenCamera extends AppCompatActivity {
    private static final int VIDEO_CAPTURE = 101;
    private static final int PHOTO_CAPTURE = 102;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    Uri fileUri;
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_camera);

        Button btnRecord = findViewById(R.id.btnRecord);
        Button btnCapture = findViewById(R.id.btnCapture);

        if (!hasCamera()) {
            btnRecord.setEnabled(false);
            btnCapture.setEnabled(false);
        }

//        Initialize ActivityResultLauncher
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleActivityResult(result.getResultCode(), result.getData())
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
//                if permission already granted
                startCapture(null);
            }
        }

        btnRecord.setOnClickListener(v -> startRecording(null));
        btnCapture.setOnClickListener(v -> startCapture(null));
    }

    private void handleActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Capture successful", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    // For capturing images
    public void startCapture(View view) {
        try {
            String random = String.valueOf(System.currentTimeMillis());
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "img_" + random);
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera app");
            fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            cameraResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // For recording videos
    public void startRecording(View view) {
        try {
            String random = String.valueOf(System.currentTimeMillis());
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "vid_" + random);
            values.put(MediaStore.Video.Media.DESCRIPTION, "Video capture by camera app");
            fileUri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 100);
            cameraResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}