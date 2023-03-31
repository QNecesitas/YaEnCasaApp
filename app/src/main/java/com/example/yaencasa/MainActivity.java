package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.Data.ProductRepository;
import com.example.yaencasa.Data.RemoteProductsDataSource;
import com.example.yaencasa.Network.IRetrofitProducts;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialogEliminado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Visibility(View view) {

    }

    public void Remove(View view) {
    }

    public void AddProduct(View view) {
        ModelProduct product=new ModelProduct(1,"Perro caliente",4.00,"Tiene ketchup y mostaza");
        Retrofit.Builder rtfBuilder=new Retrofit.Builder();
        Retrofit retrofit=rtfBuilder
                .baseUrl()
        RemoteProductsDataSource remoteProductsDataSource=new RemoteProductsDataSource();
        ProductRepository productRepository=new ProductRepository()
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


}