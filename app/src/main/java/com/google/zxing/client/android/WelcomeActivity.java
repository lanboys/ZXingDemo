package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.client.android.encode.EncodeActivity;

/**
 * Created by lean on 16/9/26.
 */

public class WelcomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void scanQRCode(View v){
        Intent intent=new Intent(this,CaptureActivity.class);
        startActivity(intent);
    }

    public void buildQRCode(View v){
        Intent intent=new Intent(this,EncodeActivity.class);
        startActivity(intent);
    }


}
