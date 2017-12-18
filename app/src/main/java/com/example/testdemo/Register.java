package com.example.testdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TUSKI on 2017/12/17.
 */

public class Register extends AppCompatActivity {

    private EditText registerUser,registerPassword,confirmPassword,email;
    private Button registerButton;
    private TextView responseText;
    private  String mregisterUser,mregisterPassword,mconfirmPassword,memail,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.register_activity);
        responseText = findViewById(R.id.response_text1);
        registerUser = findViewById(R.id.register_user);
        registerPassword = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);
        email = findViewById(R.id.email);
        registerButton = findViewById(R.id.register);

//        mregisterUser = registerUser.getText().toString();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOkHttp();
//                mregisterPassword = registerPassword.getText().toString();
//                code = getMD5(mregisterPassword);
//                Log.d("Register",code);
                Toast.makeText(Register.this,"Success",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void sendRequestWithOkHttp(){
        mregisterUser = registerUser.getText().toString();
        mregisterPassword = registerPassword.getText().toString();
//        mconfirmPassword = confirmPassword.getText().toString();
        memail = email.getText().toString();
        code = getMD5(mregisterPassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.137.1:3000/api/register?user=" + mregisterUser + "&password=" + code
                                    + "&email=" + memail)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

    public  String getMD5(String str) {
        String cc = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            cc =  new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cc;
    }
}
