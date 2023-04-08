package com.example.yaencasa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.IModel_Content;
import com.example.yaencasa.Data.ModelAd;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.R;

import java.util.ArrayList;

public class AdapterR_EditProduct extends RecyclerView.Adapter<AdapterR_EditProduct.ProductViewHolder> implements Filterable {
    private Context context;
    private ArrayList<IModel_Content> al_contents;
    private RecyclerTouchListener listener;
    private CustomFilter customFilter;
    private ArrayList<IModel_Content> al_EditarProductos_filter;

    public AdapterR_EditProduct(Context context, ArrayList<IModel_Content> al_contents){
        this.context = context;
        this.al_contents = al_contents;
        this.customFilter= new CustomFilter(AdapterR_EditProduct.this);
        this.al_EditarProductos_filter=new ArrayList<>();
        al_EditarProductos_filter.addAll(al_contents);
    }

    @Override
    public int getItemCount(){
        return al_contents.size();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_edit_product,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position){
        IModel_Content content = al_contents.get(position);

        if(content instanceof ModelAd){
            onBindViewHolderAds(holder,position);
        }else {
            onBindViewHolderProducts(holder,position);
        }

    }


    private void onBindViewHolderAds(ProductViewHolder holder, int position){
        ModelAd ad = (ModelAd) al_contents.get(position);

        Glide.with(context)
                .load(Constants.PHP_IMAGES_AD+"Ad_"+ad.getId()+".jpg")
                .error(ContextCompat.getDrawable(context,R.drawable.shopping_bag_white))
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.IV_image);

        holder.TV_name.setText(ad.getName());
        holder.TV_price.setVisibility(View.GONE);
        holder.TV_descProduct.setVisibility(View.GONE);

    }

    private void onBindViewHolderProducts(ProductViewHolder holder, int position){
        IModel_Content iproduct = (IModel_Content) al_contents.get(position);
        ModelProduct product = (ModelProduct) iproduct;

        Glide.with(context)
                .load(Constants.PHP_IMAGES+"P_"+product.getId()+".jpg")
                .error(ContextCompat.getDrawable(context,R.drawable.shopping_bag_white))
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.IV_image);

        holder.TV_name.setText(product.getName());
        String priceStr=product.getPrice()+" CUP";
        holder.TV_price.setText(priceStr);
        holder.TV_descProduct.setText(product.getDesc());
        holder.TV_Ad.setVisibility(View.GONE);
    }

    public void setClickListener(RecyclerTouchListener listener){
        if(listener != null) this.listener=listener;
    }

    protected class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView IV_image;
        TextView TV_name;
        TextView TV_price;
        TextView TV_descProduct;
        TextView TV_Ad;


        public ProductViewHolder(final View itemView){
            super(itemView);
            IV_image =(ImageView)itemView.findViewById(R.id.RP_IV_ImageProduct);
            TV_name =(TextView)itemView.findViewById(R.id.RP_TV_name);
            TV_price =(TextView)itemView.findViewById(R.id.RP_TV_Price);
            TV_descProduct =(TextView)itemView.findViewById(R.id.RP_TV_descProduct);
            TV_Ad = (TextView) itemView.findViewById(R.id.RP_TV_AD);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(al_contents.get(getAdapterPosition()) instanceof ModelProduct){
                        if(listener!=null) listener.onClickProduct(itemView,getAdapterPosition());
                    }else{
                        if(listener!=null) listener.onClickAd(itemView,getAdapterPosition());
                    }
                }
            });
        }

    }

    public interface RecyclerTouchListener{
        void onClickAd(View v, int position);
        void onClickProduct(View v,int position);
    }

    @Override
    public Filter getFilter() {
        return customFilter;
    }

    public class CustomFilter extends Filter {
        AdapterR_EditProduct adapterR_editProducts;

        public CustomFilter(AdapterR_EditProduct adapterR_editProducts) {
            super();
            this.adapterR_editProducts = adapterR_editProducts;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            al_EditarProductos_filter.clear();



            FilterResults filterResults=new FilterResults();
            if(charSequence.length()==0){
                al_EditarProductos_filter.addAll(al_contents);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for( IModel_Content content:al_contents){
                    if(content instanceof ModelProduct){
                        ModelProduct product=(ModelProduct) content;
                        if(product.getName().toLowerCase().trim().contains(filterPattern)){
                            al_EditarProductos_filter.add(content);
                        }
                    }else{
                        ModelAd ad=(ModelAd) content;
                        if(ad.getName().toLowerCase().trim().contains(filterPattern)){
                            al_EditarProductos_filter.add(content);
                        }
                    }

                }
            }
            filterResults.values=al_EditarProductos_filter;
            filterResults.count=al_EditarProductos_filter.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapterR_editProducts.notifyDataSetChanged();
        }
    }

}