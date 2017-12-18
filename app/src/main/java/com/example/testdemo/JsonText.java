package com.example.testdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by TUSKI on 2017/12/18.
 */

public class JsonText extends AppCompatActivity {
    private TextView jason;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jason_text);
        jason = findViewById(R.id.jason_text);
//        Intent intent = getIntent();
//        final String jsondata = intent.getStringExtra("extra_data");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                show(jsondata);
//            }
//        });
    }
    public void show(final String jasondata){
        jason.setText(jasondata);
    }
}
