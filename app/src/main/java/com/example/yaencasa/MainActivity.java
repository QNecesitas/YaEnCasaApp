package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    private ProgressDialog progressDialogEliminado;
    private RetrofitProductsImpl retrofitProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofitProducts=new RetrofitProductsImpl();


    }

    public void Visibility(View view) {

    }


    public void Remove(View view) {
    }



    public void UpdateProduct(View view) {
    }


    public void FetchProduct(View view) {
    }

    public void showAlertDialogDeleteCategoria(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Prueba");
        builder.setMessage(text);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void click(View view) {
        Intent intent = new Intent(this, Activity_Home.class);
        startActivity(intent);


    }


}