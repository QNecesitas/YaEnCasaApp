package com.example.yaencasa;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaencasa.Adapters.AdapterR_MyOrders;
import com.example.yaencasa.Auxiliary.NetworkTools;
import com.example.yaencasa.Data.ModelOrder;
import com.example.yaencasa.Data.Network.RetrofitOrdersImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_MyOrders extends AppCompatActivity {
    //198


    private static final long TIMEPO_DE_ESPERA = 60000;

    //Internet
    RetrofitOrdersImpl retrofitOrders;

    //Recycler
    private AdapterR_MyOrders adapterR_myOrders;
    private RecyclerView recycler;
    private ArrayList<ModelOrder> al_myOrders;
    private boolean inActivity;
    private Handler handler;
    private String idsPreferences;
    private ConstraintLayout clNoInternet;
    private  SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        //Toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.AMO_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.icon_navigation){
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //RecyclerView
        recycler=(RecyclerView) findViewById(R.id.AMO_recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(Activity_MyOrders.this));
        al_myOrders=new ArrayList<>();
        adapterR_myOrders=new AdapterR_MyOrders(Activity_MyOrders.this,al_myOrders);
        recycler.setAdapter(adapterR_myOrders);

        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("PuertoShop",0);
        sharedEditor=sharedPreferences.edit();
        idsPreferences=sharedPreferences.getString("idsEnviados"," ");

        //No internet
        clNoInternet=(ConstraintLayout) findViewById(R.id.AMO_CL_text_no_conx);

        retrofitOrders = new RetrofitOrdersImpl();

    }

    //Download info
    private void loadMainInternetInfo(){
        if(NetworkTools.isOnline(Activity_MyOrders.this,false)){
            loadRecyclerInfo();
        }else{
            clNoInternet.setVisibility(View.VISIBLE);
        }
    }
    private void loadRecyclerInfo(){
        if(NetworkTools.isOnline(Activity_MyOrders.this,false)) {

            Call<ArrayList<ModelOrder>> call = retrofitOrders.fetchOrders();
            call.enqueue(new Callback<ArrayList<ModelOrder>>() {
                @Override
                public void onResponse(Call<ArrayList<ModelOrder>> call, Response<ArrayList<ModelOrder>> response) {
                    if (response.isSuccessful()) {
                        al_myOrders.clear();
                        clNoInternet.setVisibility(View.GONE);
                        al_myOrders = response.body();
                        if (al_myOrders.isEmpty()) {

                        } else {
                            clNoInternet.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ModelOrder>> call, Throwable t) {
                    clNoInternet.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    //Auxiliares
    private void updateRecyclerAdapter(){
        ConstraintLayout linearLayoutEmpty=(ConstraintLayout)findViewById(R.id.AMO_image_empty);
        if(al_myOrders.isEmpty()){
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }else{
            linearLayoutEmpty.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
        adapterR_myOrders =new AdapterR_MyOrders(Activity_MyOrders.this,al_myOrders);
        recycler.setAdapter(adapterR_myOrders);
    }

    //BtnActions
    private void showAlertDialogVaciar(){
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_MyOrders.this);
        builder.setCancelable(true);
        builder.setTitle(R.string.Vaciar_pedidos);
        builder.setMessage(R.string.Tiene_certeza_de_vaciarMisPedidos);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                click_vaciar_pedidos();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }
    public void click_vaciar_mis_pedidos(View view){
        showAlertDialogVaciar();
    }
    public void click_vaciar_pedidos(){
        sharedEditor.putString("idsEnviados"," ");
        sharedEditor.apply();
        finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        inActivity=false;
    }

    @Override
    public void onResume(){
        super.onResume();
        inActivity=true;

        //Handler
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.arg1 == 2) {
                    loadMainInternetInfo();
                }
            }
        };

        //Thread
        Thread thread = new Thread(() -> {
            while (inActivity) {
                Message message = Message.obtain();
                message.arg1 = 2;
                handler.sendMessage(message);

                try {
                    Thread.sleep(TIMEPO_DE_ESPERA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}