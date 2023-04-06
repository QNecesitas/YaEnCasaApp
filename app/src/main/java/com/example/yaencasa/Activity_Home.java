package com.example.yaencasa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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



}