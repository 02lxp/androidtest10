package com.example.test10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        Intent intent = init();
        String displayName = intent.getStringExtra("displayName");
        String number = intent.getStringExtra("number");
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{ Manifest.permission.SEND_SMS }, 1);
        }else{
            onSendSms(displayName,number);
        }
    }
    public void onSendSms(String displayName,String number) {
        Uri uri = Uri.parse("smsto:" + number) ;
        Intent it = new Intent(Intent.ACTION_SENDTO,uri) ;
        it.putExtra("sms_body",displayName) ;
        startActivity(it) ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = init();
                    String displayName = intent.getStringExtra("displayName");
                    String number = intent.getStringExtra("number");
                    onSendSms(displayName,number);
                } else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    public Intent init() {
        Intent intent = getIntent();
        return intent;
    }
}