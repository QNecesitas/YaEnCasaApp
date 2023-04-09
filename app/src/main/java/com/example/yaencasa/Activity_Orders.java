package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yaencasa.Adapters.AdapterR_Orders;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Auxiliary.NetworkTools;
import com.example.yaencasa.Data.ModelOrder;
import com.example.yaencasa.Data.Network.RetrofitOrdersImpl;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Orders extends AppCompatActivity {
    //213

    private static final long TIMEPO_DE_ESPERA = 60000;

    //Internet
    RetrofitOrdersImpl retrofitOrders;

    //Recycler
    private AdapterR_Orders adapterR_orders;
    private RecyclerView recycler;
    private ArrayList<ModelOrder> al_orders;
    private boolean inActivity;
    private Handler handler;
    private ConstraintLayout clNoInternet;
    private ProgressDialog pdialogDel;

    //Shared Preferences
    private int lastOrderSeenOrNotified;
    private SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.AO_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.icon_navigation) {

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
        recycler = (RecyclerView) findViewById(R.id.AO_recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(Activity_Orders.this));
        al_orders = new ArrayList<>();
        adapterR_orders = new AdapterR_Orders(Activity_Orders.this, al_orders);
        recycler.setAdapter(adapterR_orders);
        clNoInternet=(ConstraintLayout) findViewById(R.id.AO_CL_text_no_conx);

        //Preferencias
        SharedPreferences sharedPreferences = getSharedPreferences("DCero", 0);
        sharedEditor= sharedPreferences.edit();
        lastOrderSeenOrNotified = sharedPreferences.getInt("lastOrderSeenOrNotified", 0);

        //Internet
        retrofitOrders=new RetrofitOrdersImpl();
    }

    //Download info
    private void loadMainInternetInfo() {
        if (NetworkTools.isOnline(Activity_Orders.this, false)) {
            loadRecyclerInfo();
        } else {
            clNoInternet.setVisibility(View.VISIBLE);
        }
    }
    private void loadRecyclerInfo() {
        if (NetworkTools.isOnline(Activity_Orders.this, false)) {

            Call<ArrayList<ModelOrder>> call = retrofitOrders.fetchOrders();
            call.enqueue(new Callback<ArrayList<ModelOrder>>() {
                @Override
                public void onResponse(Call<ArrayList<ModelOrder>> call, Response<ArrayList<ModelOrder>> response) {
                    if (response.isSuccessful()) {
                        al_orders.clear();
                        clNoInternet.setVisibility(View.GONE);
                        al_orders = response.body();
                        if (al_orders.isEmpty()) {

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
    private void updateRecyclerAdapter() {

        ConstraintLayout linearLayoutEmpty = (ConstraintLayout) findViewById(R.id.AO_image_empty);
        if (al_orders.isEmpty()) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        } else {
            linearLayoutEmpty.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }

        adapterR_orders = new AdapterR_Orders(Activity_Orders.this, al_orders);

        adapterR_orders.setListener_cancel(new AdapterR_Orders.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                clickCancelar(position);
            }
        });
        adapterR_orders.setListener_accept(new AdapterR_Orders.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                clickAceptar(position);
            }
        });
        adapterR_orders.setListener_finished(new AdapterR_Orders.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                clickFinalizar(position);
            }
        });

        recycler.setAdapter(adapterR_orders);

    }
    private void showAlertDialogCancelOrAcept(){
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_Orders.this);
        builder.setCancelable(true);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.aceptado_o_cancelado);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }
    private void showAlertDialogVaciar(){
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_Orders.this);
        builder.setCancelable(true);
        builder.setTitle(R.string.Vaciar_pedidos);
        builder.setMessage(R.string.Tiene_certeza_de_vaciar);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                eliminarPedidosInternet();
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
    public void showAlertDialogNoInternet() {
        //init alert dialog
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(Activity_Orders.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.Revise_su_conexion);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Reintentar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }



    //Cambio de estado
    private void li_cancelar(String id, int position) {
        LayoutInflater inflater = LayoutInflater.from(Activity_Orders.this);
        View vista = inflater.inflate(R.layout.li_order_cancel, null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Activity_Orders.this);
        builder.setView(vista);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        //Declaraciones
        EditText editText = (EditText) vista.findViewById(R.id.li_order_cancel_et_respuesta_cliente);
        TextView btn_cancelar = (TextView) vista.findViewById(R.id.li_order_cancel_tv_cancelar);
        TextView btn_aceptar = (TextView) vista.findViewById(R.id.li_order_cancel_tv_acept);


        //Listene
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().trim().isEmpty()) {
                    alertDialog.dismiss();
                    cancelarPedidoInternet(id, position, editText.getText().toString());
                } else {
                    editText.setError(getString(R.string.campo_vacio));
                }
            }
        });

        //Finalizado
        builder.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void cancelarPedidoInternet(String id, int position, String explanation) {
        ProgressDialog pdialog = ProgressDialog.show(Activity_Orders.this, getString(R.string.realizando_operacion), getString(R.string.por_favor_espere), false, false);


        Call<String> call = retrofitOrders.updateOrder(
                Constants.PHP_TOKEN,
                al_orders.get(position).getId(),
                "Cancelado",
                explanation
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    clNoInternet.setVisibility(View.GONE);
                    pdialog.dismiss();
                    FancyToast.makeText(Activity_Orders.this,getString(R.string.Operacion_realizada_con_exito),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                    updateRecyclerAdapter();
                }else{
                    pdialog.dismiss();
                    showAlertDialogNoInternet();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pdialog.dismiss();
                showAlertDialogNoInternet();
            }
        });
    }
    private void aceptarPedidoInternet(String id, int position) {
        ProgressDialog pdialog = ProgressDialog.show(Activity_Orders.this, getString(R.string.realizando_operacion), getString(R.string.por_favor_espere), false, false);


        Call<String> call = retrofitOrders.updateOrder(
                Constants.PHP_TOKEN,
                al_orders.get(position).getId(),
                "Aceptado",
                "no"
                );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    clNoInternet.setVisibility(View.GONE);
                    pdialog.dismiss();
                    FancyToast.makeText(Activity_Orders.this,getString(R.string.Operacion_realizada_con_exito),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                    updateRecyclerAdapter();
                }else{
                    pdialog.dismiss();
                    showAlertDialogNoInternet();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pdialog.dismiss();
                showAlertDialogNoInternet();
            }
        });


    }
    private void finalizarPedidoInternet(String id, int position) {
        ProgressDialog pdialog = ProgressDialog.show(Activity_Orders.this, getString(R.string.realizando_operacion), getString(R.string.por_favor_espere), false, false);


        Call<String> call = retrofitOrders.updateOrder(
                Constants.PHP_TOKEN,
                al_orders.get(position).getId(),
                "Finalizado",
                "no"
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    clNoInternet.setVisibility(View.GONE);
                    pdialog.dismiss();
                    FancyToast.makeText(Activity_Orders.this,getString(R.string.Operacion_realizada_con_exito),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                    updateRecyclerAdapter();
                }else{
                    pdialog.dismiss();
                    showAlertDialogNoInternet();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pdialog.dismiss();
                showAlertDialogNoInternet();
            }
        });
    }
    private void clickFinalizar(int position) {
        if (NetworkTools.isOnline(Activity_Orders.this, true)) {
            finalizarPedidoInternet(String.valueOf(al_orders.get(position).getId()), position);
        }
    }
    public void click_vaciar_pedidos(View view) {
        showAlertDialogVaciar();
    }
    private void clickCancelar(int position) {
        if (!al_orders.get(position).getState().equals("Aceptado")) {
            if (NetworkTools.isOnline(Activity_Orders.this, true)) {
                li_cancelar(String.valueOf(al_orders.get(position).getId()), position);
            }
        }else showAlertDialogCancelOrAcept();
    }
    private void clickAceptar(int position) {
        if(!al_orders.get(position).getState().equals("Cancelado")) {
            if (NetworkTools.isOnline(Activity_Orders.this, true)) {
                aceptarPedidoInternet(String.valueOf(al_orders.get(position).getId()), position);
            }
        }else showAlertDialogCancelOrAcept();
    }

    private void eliminarPedidosInternet() {

       Call<String> call = retrofitOrders.cleanOrders(Constants.PHP_TOKEN);

       call.enqueue(new Callback<String>() {
           @Override
           public void onResponse(Call<String> call, Response<String> response) {
               if(response.isSuccessful()){

               }
           }

           @Override
           public void onFailure(Call<String> call, Throwable t) {

           }
       });

    }

    @Override
    public void onPause() {
        super.onPause();
        inActivity = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        inActivity = true;

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