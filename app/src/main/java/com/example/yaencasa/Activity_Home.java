package com.example.yaencasa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Bradcasts.BatteryReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class Activity_Home extends AppCompatActivity {

    //Fragments
    private FragmentManager fragmentManager;

    //Preferences
    private String idsSends;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private RadioButton radioButtonLocal;
    private BottomNavigationView bottomAppBarView;


    //DrawerLayout
    private DrawerLayout drawer;
    private NavigationView navigationView;


    //Broadcast
    private BatteryReceiver batteryReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constants.APP_MODE == Constants.ClientAdmin.ADMIN) {
            setContentView(R.layout.activity_home_admin);
        } else {
            setContentView(R.layout.activity_home);
        }

        //Toolbar
        Toolbar toolbar=(Toolbar) findViewById(R.id.AH_toolbar);
        setSupportActionBar(toolbar);


        //Fragments
        fragmentManager=getSupportFragmentManager();


        //BottomBar
        bottomAppBarView=findViewById(R.id.AH_bottom_navigation);
        showFragmentCategories();
        bottomAppBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_products:
                        showFragmentProducts();
                        break;
                    case R.id.page_categories:
                        showFragmentCategories();
                        break;
                    case R.id.page_cart:
                        showFragmentShopping_cart();
                        break;
                    case R.id.page_menu:
                        showFragmentMenu();
                        break;
                    case R.id.page_orders:
                        showFragmentOrders();
                        break;
                    case R.id.page_edit_products:
                        showFragmentEditProducts();
                        break;
                }
                return true;
            }
        });


        //NavigationView
        drawer=(DrawerLayout) findViewById(R.id.AH_DL);
        navigationView=(NavigationView) findViewById(R.id.AH_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer=(DrawerLayout) findViewById(R.id.AH_DL);
                drawer.closeDrawer(navigationView);

                switch (item.getItemId()){

                    case R.id.menu_client_about_developers:
                    case R.id.menu_boss_about_developers:
                        Intent intent1 =new Intent(Activity_Home.this, Activity_About_Dev.class);
                        startActivity(intent1);
                        break;
                    case R.id.menu_client_about_us:
                    case R.id.menu_boss_about_us:
                        Intent intent2 =new Intent(Activity_Home.this, Activity_About_Us.class);
                        startActivity(intent2);
                        break;
                    case R.id.menu_client_terms:
                    case R.id.menu_boss_terms:
                        li_terminos();
                        break;



                    case R.id.menu_client_mis_pedidos:
                        Intent intent3 =new Intent(Activity_Home.this, Activity_MyOrders.class);
                        startActivity(intent3);
                        break;
                    case R.id.menu_boss_home:
                        showFragmentProducts();
                        break;
                    case R.id.menu_boss_zone:
                        Intent intent6 =new Intent(Activity_Home.this, Activity_Zone.class);
                        startActivity(intent6);
                        break;
                }

                //Admin


                return true;
            }
        });


        //Shared preferences
        sharedPreferences = getSharedPreferences("YaEnCasa", 0);
        sharedEditor = sharedPreferences.edit();


        //Battery Receiver
        batteryReceiver = new BatteryReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(batteryReceiver, intentFilter);

    }


    private void showFragmentEditProducts(){

        Fragment_EditProduct fragment_edit_product=new Fragment_EditProduct();
        fragmentManager.beginTransaction()
                .replace(R.id.AH_FL,fragment_edit_product)
                .commit();
    }

    private void showFragmentProducts(){

        Fragment_Product fragment_product=new Fragment_Product();
        fragmentManager.beginTransaction()
                .replace(R.id.AH_FL,fragment_product)
                .commit();
    }

    private void showFragmentOrders(){

        Fragment_Orders fragment_orders=new Fragment_Orders();
        fragmentManager.beginTransaction()
                .replace(R.id.AH_FL,fragment_orders)
                .commit();
    }

    private void showFragmentCategories(){
        Fragment_Categories fragment_categories=new Fragment_Categories();
        fragment_categories.setRecyclerTouchListener(new Fragment_Categories.RecyclerTouchListener() {
            @Override
            public void onClick() {
               if(Constants.APP_MODE==Constants.ClientAdmin.CLIENT){
                   bottomAppBarView.setSelectedItemId(R.id.page_products);
               }else{
                   bottomAppBarView.setSelectedItemId(R.id.page_edit_products);
               }
            }
        });
        fragmentManager.beginTransaction()
                .replace(R.id.AH_FL,fragment_categories)
                .commit();
    }

    private void showFragmentShopping_cart(){
        Fragment_Shopping_cart fragment_shopping_cart=new Fragment_Shopping_cart();
        fragmentManager.beginTransaction()
                .replace(R.id.AH_FL,fragment_shopping_cart)
                .commit();
    }

    private void showFragmentMenu(){
        drawer.openDrawer(navigationView);
    }

    private void li_terminos() {
        LayoutInflater layoutInflater = LayoutInflater.from(Activity_Home.this);
        View view = layoutInflater.inflate(R.layout.li_terminos, null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Activity_Home.this);
        builder.setView(view);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        ImageView btnClose = (ImageView) view.findViewById(R.id.liT_IV_cerrar);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        //Finalizado
        builder.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showAlertDialogExit();
        }

    }

    private void showAlertDialogExit() {
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.salir);
        builder.setMessage(R.string.seguro_desea_salir);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish the activity
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog gone
                dialog.dismiss();
            }
        });
        //create the alert dialog and show it
        builder.create().show();
    }

}