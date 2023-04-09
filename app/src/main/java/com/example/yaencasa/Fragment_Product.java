package com.example.yaencasa;

import static android.app.Activity.RESULT_CANCELED;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yaencasa.Adapters.AdapterR_Product;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Auxiliary.IDCreater;
import com.example.yaencasa.Auxiliary.ImageTools;
import com.example.yaencasa.Auxiliary.NetworkTools;
import com.example.yaencasa.Auxiliary.Permissions;
import com.example.yaencasa.Data.Cart_Elements;
import com.example.yaencasa.Data.ContentRepository;
import com.example.yaencasa.Data.IModel_Content;
import com.example.yaencasa.Data.ModelAd;
import com.example.yaencasa.Data.ModelCategory;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.Data.Network.RetrofitProductsImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Product extends Fragment {

    //Recycler
    private RecyclerView recycler;
    private ArrayList<IModel_Content> array_content;
    private ArrayList<ModelCategory> array_categories;
    private AdapterR_Product adapterR_products;


    //Categories
    private TextView TV_filter_category;
    int selected_category;
    LinearLayout linearLayoutEmpty;

    //Internet
    private ProgressDialog progressDialogCargando;
    private ContentRepository contentRepository;
    private RetrofitProductsImpl retrofitProducts;




    public Fragment_Product() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        selected_category = getActivity().getIntent().getIntExtra("category", 0);


        //RecyclerView
        recycler = (RecyclerView) root.findViewById(R.id.FP_recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        array_content = new ArrayList<>();
        adapterR_products = new AdapterR_Product(getContext(), array_content);
        array_categories = new ArrayList<>();

        TV_filter_category = (TextView) root.findViewById(R.id.FP_TV_Category);
        linearLayoutEmpty = (LinearLayout) root.findViewById(R.id.FP_imagen_empty);



        //Search
        SearchView searchView = (SearchView) root.findViewById(R.id.FP_SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterR_products.getFilter().filter(s);
                return true;
            }
        });

        //Internet
        contentRepository = new ContentRepository();
        retrofitProducts = new RetrofitProductsImpl();


        return root;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }




    //Download info
    private void loadMainInternetInfo() {
        if (NetworkTools.isOnline(requireContext(), false)) {
            progressDialogCargando = ProgressDialog.show(requireContext(), getString(R.string.Cargando_datos), getString(R.string.Espere), false, false);
            loadRecyclerInfo();
        } else {
            showAlertDialogNoInternet();
        }
    }
    private void loadRecyclerInfo() {
        if (NetworkTools.isOnline(requireContext(), false)) {

            contentRepository.setListener(new ContentRepository.ContentReadyListener() {
                @Override
                public void onReady(ArrayList<IModel_Content> arrayList) {
                    array_content.clear();
                    array_content.addAll(arrayList);
                    updateRecyclerAdapter();
                }

                @Override
                public void onFailure() {
                    progressDialogCargando.dismiss();
                    showAlertDialogNoInternet();
                }
            });
            contentRepository.fetchContent();


        } else {
            showAlertDialogNoInternet();
        }
    }
    public void showAlertDialogNoInternet() {
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setCancelable(true);
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

    //Auxiliares
    private void updateRecyclerAdapter() {


        if (array_content.isEmpty()) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        } else {
            linearLayoutEmpty.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }

        adapterR_products = new AdapterR_Product(requireContext(), array_content);
        adapterR_products.setClickListener(new AdapterR_Product.RecyclerTouchListener() {
            @Override
            public void onClickAd(View v, int position, ArrayList<IModel_Content> al_filter) {
                String url = ((ModelAd) al_filter.get(position)).getUrl();
                Intent intent = new Intent(requireActivity(),Activity_AdView.class);
                intent.putExtra("urlAd",url);
                startActivity(intent);
            }

            @Override
            public void onClickProduct(View v, int position, ArrayList<IModel_Content> al_filter) {
                Cart_Elements.productInfo = (ModelProduct)al_filter.get(position);
                Intent intent = new Intent(requireActivity(), Activity_InfoProduct.class);
                startActivity(intent);
            }
        });
        recycler.setAdapter(adapterR_products);
        progressDialogCargando.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        loadMainInternetInfo();
    }

}