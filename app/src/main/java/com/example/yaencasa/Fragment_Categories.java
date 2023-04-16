package com.example.yaencasa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaencasa.Adapters.AdapterR_Categories;
import com.example.yaencasa.Auxiliary.CategoryState;
import com.example.yaencasa.Auxiliary.NetworkTools;
import com.example.yaencasa.Data.ModelCategory;
import com.example.yaencasa.Data.Network.RetrofitCategorialmpl;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Categories extends Fragment {


    //Recycler
    private RecyclerView recycler;
    private ArrayList<ModelCategory> al_categories;
    private AdapterR_Categories adapter;
    private ProgressDialog progressDialogCargando;
    private RetrofitCategorialmpl retrofitCategorialmpl;
    private RecyclerTouchListener recyclerTouchListener;

    View root;


    public Fragment_Categories() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       root=inflater.inflate(R.layout.fragment__categories,container,false);



        //RecyclerView
        recycler=(RecyclerView) root.findViewById(R.id.categories_recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        al_categories =new ArrayList<>();
        adapter=new AdapterR_Categories(getContext(), al_categories);


        retrofitCategorialmpl = new RetrofitCategorialmpl();

        return root;

         }


    //Download info
    private void loadMainInternetInfo(){
        if(NetworkTools.isOnline(requireContext(),false)) {
            progressDialogCargando = ProgressDialog.show(getContext(), getString(R.string.Cargando_datos), getString(R.string.Espere), false, false);
            loadCategories();
        }else{
            showAlertDialogNoInternet();
        }
    }
    private void loadCategories(){
        if (NetworkTools.isOnline(requireContext(), false)) {

            Call<ArrayList<ModelCategory>> call= retrofitCategorialmpl.fetchCategories();
            call.enqueue(new Callback<ArrayList<ModelCategory>>() {
                @Override
                public void onResponse(Call<ArrayList<ModelCategory>> call, Response<ArrayList<ModelCategory>> response) {
                    if(response.isSuccessful()){
                       progressDialogCargando.dismiss();
                       if(response.body()!=null) {
                           al_categories = response.body();
                       }
                       if(!al_categories.isEmpty()) CategoryState.IdCategorySelected = al_categories.get(0).getIdCategory();
                       updateRecyclerAdapter();
                    }else{
                        progressDialogCargando.dismiss();
                        NetworkTools.showAlertDialogNoInternet(requireContext());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ModelCategory>> call, Throwable t) {
                    progressDialogCargando.dismiss();
                    NetworkTools.showAlertDialogNoInternet(requireContext());
                }
            });


        } else {
            showAlertDialogNoInternet();
        }

    }
    public void showAlertDialogNoInternet() {
        //init alert dialog
        android.app.AlertDialog.Builder builder =  new android.app.AlertDialog.Builder(getContext());
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
    private void updateRecyclerAdapter(){

        LinearLayout linearLayoutEmpty=(LinearLayout)root.findViewById(R.id.categories_image_empty);
        if(al_categories.isEmpty()){
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);

        }else{
            linearLayoutEmpty.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }

        adapter =new AdapterR_Categories(getContext(), al_categories);
        adapter.setClickListener(new AdapterR_Categories.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
               CategoryState.IdCategorySelected = al_categories.get(position).getIdCategory();
               if (recyclerTouchListener!=null)recyclerTouchListener.onClick();
            }
        });
        recycler.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadMainInternetInfo();
    }

    public interface RecyclerTouchListener{
        void onClick();
    }

    public void setRecyclerTouchListener(RecyclerTouchListener recyclerTouchListener) {
        this.recyclerTouchListener = recyclerTouchListener;
    }
}