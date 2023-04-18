package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Auxiliary.IDCreater;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.Data.Network.RetrofitProductsImpl;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private int WAIT_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Personal ID to identify the person in the orders
        sharedPreferences = getSharedPreferences("YaEnCasa", 0);
        sharedEditor = sharedPreferences.edit();
        IDCreater.personalId = sharedPreferences.getLong("PersonalID", 0);
        if (IDCreater.personalId == 0) {
            long id = IDCreater.generate();
            sharedEditor.putLong("PersonalID", id);
            sharedEditor.apply();
        }


        View escondedor = getWindow().getDecorView();
        escondedor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //Handler
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.arg1 == 1) {
                    Intent intent = new Intent(MainActivity.this, Activity_Home.class);
                    startActivity(intent);
                }
            }
        };

        //Thread
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(WAIT_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message message = Message.obtain();
            message.arg1 = 1;
            handler.sendMessage(message);
        });
        thread.start();
    }




}