package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yaencasa.Adapters.AdapterR_Categories;
import com.example.yaencasa.Adapters.AdapterR_MyOrders;
import com.example.yaencasa.Adapters.AdapterR_Zones;
import com.example.yaencasa.Auxiliary.CategoryState;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Auxiliary.NetworkTools;
import com.example.yaencasa.Data.ModelCategory;
import com.example.yaencasa.Data.ModelOrder;
import com.example.yaencasa.Data.ModelZone;
import com.example.yaencasa.Data.Network.RetrofitZoneslmpl;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Zone extends AppCompatActivity {

    //Recycler
    private RecyclerView recycler;
    private AdapterR_Zones adapterR_zones;
    private ArrayList<ModelZone> al_zone;
    private ProgressDialog progressDialogCargando;
    private RetrofitZoneslmpl retrofitZoneslmpl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.AZ_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //RecyclerView
        recycler=(RecyclerView) findViewById(R.id.AZ_recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(Activity_Zone.this));
        al_zone=new ArrayList<>();
        adapterR_zones=new AdapterR_Zones(Activity_Zone.this,al_zone);
        recycler.setAdapter(adapterR_zones);

        retrofitZoneslmpl=new RetrofitZoneslmpl();




    }



    private void loadMainInternetInfo(){
        if(NetworkTools.isOnline(Activity_Zone.this,false)) {
            progressDialogCargando = ProgressDialog.show(Activity_Zone.this, getString(R.string.Cargando_datos), getString(R.string.Espere), false, false);
            loadZone();
        }else{
            showAlertDialogNoInternet();
        }
    }
    private void loadZone(){
        if (NetworkTools.isOnline(Activity_Zone.this, false)) {

            Call<ArrayList<ModelZone>> call= retrofitZoneslmpl.fetchZones();
            call.enqueue(new Callback<ArrayList<ModelZone>>() {
                @Override
                public void onResponse(Call<ArrayList<ModelZone>> call, Response<ArrayList<ModelZone>> response) {
                    if(response.isSuccessful()){
                        progressDialogCargando.dismiss();
                        al_zone = response.body();
                        updateRecyclerAdapter();
                    }else{
                        progressDialogCargando.dismiss();
                        NetworkTools.showAlertDialogNoInternet(Activity_Zone.this);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ModelZone>> call, Throwable t) {
                    progressDialogCargando.dismiss();
                    NetworkTools.showAlertDialogNoInternet(Activity_Zone.this);
                }
            });


        } else {
            showAlertDialogNoInternet();
        }

    }
    private void updateRecyclerAdapter(){

        adapterR_zones=new AdapterR_Zones(Activity_Zone.this, al_zone);
        adapterR_zones.setClickListenerDelete(new AdapterR_Zones.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                click_delete_zones(position);
            }
        });
        recycler.setAdapter(adapterR_zones);
    }


    public void showAlertDialogNoInternet() {
        //init alert dialog
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(Activity_Zone.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.Revise_su_conexion);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Reintentar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                loadMainInternetInfo();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }



    public void click_delete_zones(int position){
        ProgressDialog pdialog = ProgressDialog.show(Activity_Zone.this, getString(R.string.delete_zone), getString(R.string.por_favor_espere), false, false);


        Call<String> call = retrofitZoneslmpl.removeZone(Constants.PHP_TOKEN,al_zone.get(position).getIdZone());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    pdialog.dismiss();
                    loadMainInternetInfo();
                    FancyToast.makeText(Activity_Zone.this,getString(R.string.zone_delete), FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show();
                }else{
                    pdialog.dismiss();
                    NetworkTools.showAlertDialogNoInternet(Activity_Zone.this);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pdialog.dismiss();
                NetworkTools.showAlertDialogNoInternet(Activity_Zone.this);
            }
        });

    }
    public void click_add_zones(View view) {




    }

    public void li_newZone(){
        LayoutInflater inflater = LayoutInflater.from(Activity_Zone.this);
        View vista = inflater.inflate(R.layout.li_new_zone, null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Activity_Zone.this);
        builder.setView(vista);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();


        //Declaraciones
        EditText name=(EditText) vista.findViewById(R.id.LINZ_ET_name);
        EditText price=(EditText) vista.findViewById(R.id.LINZ_ET_price);
        Button cancel=(Button) vista.findViewById(R.id.LINZ_BTN_cancel);
        Button acept=(Button) vista.findViewById(R.id.LINZ_BTN_acept);



    }



}