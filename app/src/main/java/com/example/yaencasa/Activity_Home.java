package com.example.yaencasa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class Activity_Home extends AppCompatActivity {

    //Fragments
    private FragmentManager fragmentManager;


    //DrawerLayout
    DrawerLayout drawer;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Toolbar
        Toolbar toolbar=(Toolbar) findViewById(R.id.AH_toolbar);
        setSupportActionBar(toolbar);


        //Fragments
        fragmentManager=getSupportFragmentManager();


        //BottomBar
        BottomNavigationView bottomAppBarView=findViewById(R.id.AH_bottom_navigation);
        showFragmentProducts();
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
                }
                return false;
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

                }

                return true;
            }
        });






    }


    private void showFragmentProducts(){

        Fragment_Product fragment_product=new Fragment_Product();
        fragmentManager.beginTransaction()
                .add(R.id.AH_FL,fragment_product)
                .commit();
    }


    private void showFragmentCategories(){
        Fragment_Categories fragment_categories=new Fragment_Categories();
        fragmentManager.beginTransaction()
                .add(R.id.AH_FL,fragment_categories)
                .commit();
    }

    private void showFragmentShopping_cart(){
        Fragment_Shopping_cart fragment_shopping_cart=new Fragment_Shopping_cart();
        fragmentManager.beginTransaction()
                .add(R.id.AH_FL,fragment_shopping_cart)
                .commit();
    }

    private void showFragmentMenu(){
        drawer.openDrawer(navigationView);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }

    }

    private void li_terminos() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.li_terminos, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

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


}