package com.example.yaencasa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaencasa.Adapters.AdapterR_Shopping_cart;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Auxiliary.NetworkTools;
import com.example.yaencasa.Data.Cart_Elements;
import com.example.yaencasa.Data.ModelElement;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.Data.Network.RetrofitOrdersImpl;
import com.google.android.material.textfield.TextInputLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Shopping_cart extends Fragment {

    //Recycler
    private AdapterR_Shopping_cart adapterR_shopping_cart;
    private RecyclerView recycler;
    private ArrayList<ModelElement> al_shopping_cart;
    private ArrayList<ModelProduct> al_products;
    private ConstraintLayout linearLayoutEmpty;


    //Details sending
    private EditText cell_num;
    private EditText ET_adress;
    private double priceTotalCUP;
    private EditText ET_name;
    private ProgressDialog progressDialogUp;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoC;
    private String selectedZone="No";
    private int lastAutoCSelected=0;


    //Shared Preferences
    private boolean show=true;
    private String idsSends;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;

    //Location
    private static final int RESULT_CODE_MAP = 31;
    private String latitud = "no";
    private String longitud = "no";

    //Internet
    RetrofitOrdersImpl retrofitOrders;
    ProgressDialog progressDialogSubiend;


    public Fragment_Shopping_cart() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_shopping_cart,container,false);

        //Declarations
        cell_num=(EditText) root.findViewById(R.id.FSC_ET_cellNum);
        ET_adress=(EditText) root.findViewById(R.id.FSC_ET_adress);
        LinearLayout linearLayoutSheet= root.findViewById(R.id.FSC_LL_personal_info);
        ET_name=(EditText) root.findViewById(R.id.FSC_ET_name);
        textInputLayout=(TextInputLayout)root.findViewById(R.id.FSC_TIL_spinner);
        autoC=(AutoCompleteTextView) root.findViewById(R.id.FSC_autoC);


        //Spinner--Zona
        ArrayList<String> al_zones=new ArrayList<>();
        addZones(al_zones);
        ArrayAdapter<String> strigArrayAdapter=new ArrayAdapter<String>(getContext(),R.layout.list_items_spinner,al_zones);
        autoC.setAdapter(strigArrayAdapter);
        autoC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoC.getText().toString().trim().isEmpty()){
                    selectedZone="No";
                }else{
                    selectedZone=autoC.getText().toString();
                    lastAutoCSelected=i;
                }
            }
        });



        //RecyclerView
        al_products=new ArrayList<>();
        recycler=(RecyclerView) root.findViewById(R.id.FSC_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recycler.setHasFixedSize(true);
        al_shopping_cart= Cart_Elements.al_elements;
        adapterR_shopping_cart=new AdapterR_Shopping_cart(getContext(),al_shopping_cart);
        recycler.setAdapter(adapterR_shopping_cart);
        linearLayoutEmpty=(ConstraintLayout) root.findViewById(R.id.FSC_CL_empty);
        updateRecyclerAdapter();

        //Preferencias
        sharedPreferences = requireActivity().getSharedPreferences("YaEnCasa", 0);
        sharedEditor= sharedPreferences.edit();
        idsSends = sharedPreferences.getString("idsEnviados", " ");
        show=sharedPreferences.getBoolean("mostrar", true);
        ET_name.setText(sharedPreferences.getString("Nombre",""));
        cell_num.setText(sharedPreferences.getString("NumTelef",""));
        ET_adress.setText(sharedPreferences.getString("Direccion",""));
        int index=sharedPreferences.getInt("zoneIndexS",0);
        if (autoC.getText().toString().trim().isEmpty()){
            selectedZone="No";
        }else{
            selectedZone=autoC.getText().toString();
        }


        //Internet
        retrofitOrders = new RetrofitOrdersImpl();


        return root;
    }


    //Start
    private void updateRecyclerAdapter(){
        adapterR_shopping_cart=new AdapterR_Shopping_cart(getContext(),al_shopping_cart);
        if(al_shopping_cart.isEmpty()){
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.INVISIBLE);
        }else{
            linearLayoutEmpty.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
        adapterR_shopping_cart.setClickListener(new AdapterR_Shopping_cart.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                showAlertDialogDelete(position);
            }
        });
        recycler.setAdapter(adapterR_shopping_cart);
        makeTotalPrice();
    }
    private void makeTotalPrice(){
        priceTotalCUP=0;
        for(ModelElement element: al_shopping_cart){
            priceTotalCUP += element.getPrice();
        }
    }

    private void addZones(ArrayList<String> al_zones) {
        al_zones.add("El Boquerón --- +60 CUP");
        al_zones.add("La Playita --- +50 CUP");
        al_zones.add("Sandino --- +100 CUP");
        al_zones.add("Aguada del Negro --- +100 CUP");
        al_zones.add("Tiplantas --- +110 CUP");
        al_zones.add("María Nuñes --- +50 CUP");
        al_zones.add("La Cooperativa --- +150 CUP");
        al_zones.add("La Micro --- +60 CUP");
        al_zones.add("Reparto Militar 1 --- +100 CUP");
        al_zones.add("Reparto Militar 2 --- +150 CUP");
        al_zones.add("El Itabo --- +80 CUP");
        al_zones.add("El Itabo final --- +150 CUP");
        al_zones.add("Reparto Armando Silva --- +60 CUP");
        al_zones.add("Reparto Médico --- +50 CUP");
        al_zones.add("Reparto Cordoví  --- +50 CUP");
        al_zones.add("La Morena --- +100 CUP");
        al_zones.add("La Morena final --- +180 CUP");
    }


    //Aux
    public void showAlertDialogDelete(int pos) {
        //init alert dialog
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.Eliminar_elementos);
        builder.setMessage(R.string.Tiene_certeza_eliminar_elemento);
        //set listeners for dialog buttons
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                al_shopping_cart.remove(pos);
                updateRecyclerAdapter();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }
    private String makeProduct(){
        StringBuilder stringBuilder=new StringBuilder("");
        /*
        1 Pan con pasta verde
          Cantidad: 4
          Precio: 500 CUP
          Agregos:
            Salsa
            Picante
         */
        for (int a=0;a<al_shopping_cart.size();a++){
            stringBuilder.append((a+1)+" "+al_shopping_cart.get(a).getProduct().getName());
            stringBuilder.append("--n--");
            stringBuilder.append("   Cantidad: "+al_shopping_cart.get(a).getAmount());
            stringBuilder.append("--n--");
            stringBuilder.append("   Precio: ");
            stringBuilder.append(al_shopping_cart.get(a).getPrice());
            stringBuilder.append(" CUP");
            stringBuilder.append("--n--");
        }
        return stringBuilder.toString().replace("*","+");
    }

    //BtnEnviarPedido
    public void btn_enviarPedido(View view) {
        checkData();
    }
    private void checkData(){
        if (NetworkTools.isOnline(getContext(), true)) {
            if(!al_shopping_cart.isEmpty()) {
                if (!ET_name.getText().toString().trim().isEmpty()) {
                    if (!cell_num.getText().toString().trim().isEmpty()) {
                        if (!ET_adress.getText().toString().trim().isEmpty()) {
                            if(!selectedZone.equals("No")){
                                sharedEditor.putString("Nombre",ET_name.getText().toString());
                                sharedEditor.putString("NumTelef",cell_num.getText().toString());
                                sharedEditor.putString("Direccion",ET_adress.getText().toString());
                                sharedEditor.putInt("zoneIndexS",lastAutoCSelected);
                                sharedEditor.apply();
                                showAlertDialogVerifEnvio();
                            }else{
                                autoC.setError(getString(R.string.Este_campo_no_puede_vacio));
                            }
                        } else {
                            ET_adress.setError(getString(R.string.Este_campo_no_puede_vacio));
                        }
                    } else {
                        cell_num.setError(getString(R.string.Este_campo_no_puede_vacio));
                    }
                } else {
                    ET_name.setError(getString(R.string.Este_campo_no_puede_vacio));
                }
            }else {
                showAlertDialogEmpty();
            }
        }
    }
    public void showAlertDialogEmpty() {
        //init alert dialog
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.Carrito_vacio);
        builder.setMessage(R.string.Nada_a_enviar);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }
    private void showAlertDialogVerifEnvio(){
        //init alert dialog
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.Enviar_pedido);
        String message=getString(R.string.verificar_envio);
        builder.setMessage(message);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                progressDialogSubiend=ProgressDialog.show(requireContext(),getString(R.string.Enviando_pedido),getString(R.string.por_favor_espere),false,false);
                sendOrderInternet();
            }
        });
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }
    private void sendOrderInternet(){
        String token = Constants.PHP_TOKEN;
        double price = priceTotalCUP;
        String products = makeProduct();
        String celnumber = cell_num.getText().toString();
        String location = latitud+":"+longitud;
        String address = ET_adress.getText().toString();
        String name = ET_name.getText().toString();

        Call<Integer> call = retrofitOrders.addOrder(token, price, products, celnumber, location, address, name);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    li_finalizado(response.body());
                }else{
                    progressDialogSubiend.dismiss();
                    NetworkTools.showAlertDialogNoInternet(getContext());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                progressDialogSubiend.dismiss();
                NetworkTools.showAlertDialogNoInternet(getContext());
            }
        });
    }
    private void li_finalizado(Integer id){
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View vista= inflater.inflate(R.layout.li_pedido_enviado, null);
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setView(vista);
        final androidx.appcompat.app.AlertDialog alertDialog=builder.create();

        //Declaraciones
        ImageView imagenCruz=(ImageView)vista.findViewById(R.id.liPE_IV_cerrar);
        CardView cardViewPedidos=(CardView)vista.findViewById(R.id.liPE_CV_ir_a_pedidos);
        CardView cardViewWapp=(CardView)vista.findViewById(R.id.liPE_CV_ir_a_Wapp);

        //Listener
        imagenCruz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        cardViewPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ir_a_mis_pedidos();
            }
        });


        //Preferernces
        al_shopping_cart.clear();
        updateRecyclerAdapter();
        sharedEditor.putString("idsEnviados",idsSends+"/"+id);
        sharedEditor.apply();

        //Finalizado
        builder.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void ir_a_mis_pedidos(){

        if(NetworkTools.isOnline(requireContext(),true)){
            Intent intent=new Intent(getContext(),Activity_Orders.class);
            startActivity(intent);
        }
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }
}