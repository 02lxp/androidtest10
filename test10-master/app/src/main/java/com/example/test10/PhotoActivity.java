package com.example.test10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    private String mFilePath;
    private String fileName;
    private String[] permissions = null;
    private List<String> mPermissionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }else{
            onphoto();
        }
    }
//    拍照并存储
    public void onphoto(){
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

        if (outputImage.exists()) {
            outputImage.delete();
        }
        Uri uri = null;
        fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        mFilePath = outputImage.getAbsolutePath()+"/"+ fileName;
        ContentValues contentValues = new ContentValues();
        if (Build.VERSION.SDK_INT >= 24) {
            Uri imageUri = FileProvider.getUriForFile(PhotoActivity.this,
                    "com.example.test10.fileprovider", outputImage);
        }else{
            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        startActivity(intent);
    }

    //    申请权限后调用函数
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            if (hasPermissionDismiss) {
                Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
            } else {
                onphoto();
            }
        }

    }
}
