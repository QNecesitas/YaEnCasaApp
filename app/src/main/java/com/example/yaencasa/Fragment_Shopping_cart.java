package com.example.yaencasa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class Fragment_Shopping_cart extends Fragment {

    //Global variables
    private static final int selected=1;
    private static final int no_selected=0;


    public Fragment_Shopping_cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_shopping_cart,container,false);

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }
}