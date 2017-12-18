package com.example.testdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.MessageDigest;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView responseText;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public  String user,password,code;
    private Intent intent;
//    private MD5 md5;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        responseText = findViewById(R.id.response_text);
        usernameEdit = findViewById(R.id.user);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember_password);

        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            usernameEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }

        Button register = findViewById(R.id.register1);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                password = passwordEdit.getText().toString();
//                code = getMD5(password);
//                Log.d("MainActivity",code);
                sendRequestWithOkHttp();
//                intent = new Intent(MainActivity.this,JsonText.class);
//                startActivity(intent);
//                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendRequestWithOkHttp(){
        user = usernameEdit.getText().toString();
        password = passwordEdit.getText().toString();
        code = getMD5(password);
        editor = pref.edit();
        if (rememberPass.isChecked()){
            editor.putBoolean("remember_password",true);
            editor.putString("account",user);
            editor.putString("password",password);
        }else {
            editor.clear();
        }
        editor.apply();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("user","hyccpq")
//                            .add("password","de6cf650a5d80049bc5b2efae016d63d4adeb6ac")
//                            .build();
                    Request request = new Request.Builder()
                            .url("http://192.168.137.1:3000/api/login?user=" + user + "&password=" + code)
//                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
//                    intent.putExtra("jsondata",responseData);
                    showResponse(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
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

//    public void parseJSONWithGson(String jsonData){
//        Gson gson = new Gson();
//
//    }

    public void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}
