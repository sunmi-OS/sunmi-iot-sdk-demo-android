package com.sunmi.iotsdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();

        findViewById(R.id.btn_iot_conn_service_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //联接配置
                Intent intent = new Intent(MainActivity.this,IoTConnServiceSDKDemoActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_thing_adapter_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //能力实现
                Intent intent = new Intent(MainActivity.this,ThingAdapterSDKDemoActivity.class);
                startActivity(intent);
            }
        });
    }
}