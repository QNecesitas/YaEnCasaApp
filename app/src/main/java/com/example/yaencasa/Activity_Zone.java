package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.yaencasa.Adapters.AdapterR_MyOrders;
import com.example.yaencasa.Data.ModelOrder;

import java.util.ArrayList;
import java.util.Objects;

public class Activity_Zone extends AppCompatActivity {

    //Recycler
    private RecyclerView recycler;
    private AdapterR_MyOrders adapterR_myOrders;
    private ArrayList<ModelOrder> al_myOrders;

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
        al_myOrders=new ArrayList<>();
        adapterR_myOrders=new AdapterR_MyOrders(Activity_Zone.this,al_myOrders);
        recycler.setAdapter(adapterR_myOrders);




    }
}