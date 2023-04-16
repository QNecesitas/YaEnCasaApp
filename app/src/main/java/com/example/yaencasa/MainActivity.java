package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Shared Preferences
        sharedPreferences = getSharedPreferences("YaEnCasa", 0);
        sharedEditor = sharedPreferences.edit();
        IDCreater.personalId = sharedPreferences.getLong("PersonalID",0);
        if(IDCreater.personalId == 0){
            long id = IDCreater.generate();
            sharedEditor.putLong("PersonalID",id);
            sharedEditor.apply();
        }


    }


    public void click(View view) {
        Intent intent = new Intent(this, Activity_Home.class);
        startActivity(intent);


    }


}