package com.example.yaencasa.Data;

import android.util.Log;

import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.Network.RetrofitAdsImpl;
import com.example.yaencasa.Data.Network.RetrofitProductsImpl;

import java.util.ArrayList;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentRepository {
    //Network
    private RetrofitAdsImpl retrofitAds;
    private RetrofitProductsImpl retrofitProducts;
    private ContentReadyListener listener;

    //Category
    int idCategorySelected=1;

    //Data
    private ArrayList<IModel_Content> al_content;
    private ArrayList<ModelProduct> al_products;
    private ArrayList<ModelAd> al_ads;



    public ContentRepository() {
        //Network
        retrofitAds = new RetrofitAdsImpl();
        retrofitProducts = new RetrofitProductsImpl();

        //Data
        al_content=new ArrayList<>();
        al_products=new ArrayList<>();
        al_ads=new ArrayList<>();
    }



    public void setListener(ContentReadyListener listener) {
        this.listener = listener;
    }

    public interface ContentReadyListener{
        void onReady(ArrayList<IModel_Content> arrayList);
        void onFailure();
    }



    public void fetchContent(){

        fetchProducts();

    }

    private void fetchProducts(){
        Call<ArrayList<ModelProduct>> callProduct = retrofitProducts.fetchProducts(Constants.PHP_TOKEN,idCategorySelected);

        callProduct.enqueue(new Callback<ArrayList<ModelProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelProduct>> call, Response<ArrayList<ModelProduct>> response) {
                if(response.isSuccessful()){
                    al_products=response.body();
                    fetchAds();
                }else{
                    if(listener!=null)listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelProduct>> call, Throwable t) {
                if(listener!=null)listener.onFailure();
            }
        });
    }

    private void fetchAds(){

        Call<ArrayList<ModelAd>> callAd = retrofitAds.fetchAds();

        callAd.enqueue(new Callback<ArrayList<ModelAd>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelAd>> call, Response<ArrayList<ModelAd>> response) {
                if (response.isSuccessful()){
                    al_ads=response.body();
                    makeContent();
                }else{
                    if(listener!=null)listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelAd>> call, Throwable t) {
                if(listener!=null)listener.onFailure();
            }
        });

    }

    private void makeContent(){
        al_content.clear();
        al_content.addAll(al_products);
        Stack<ModelAd> stack_ads_aux = new Stack<>();
        stack_ads_aux.addAll(al_ads);

        for (int f=0; f<al_content.size();f++){
            if(f % 4 == 0 && f != 0){
                if(!stack_ads_aux.isEmpty())al_content.add(f,stack_ads_aux.pop());
            }
        }
        if(listener!=null)listener.onReady(al_content);
    }




}
