package com.example.yaencasa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.IModel_Content;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.Data.ModelAd;
import com.example.yaencasa.R;

import java.util.ArrayList;

public class AdapterR_Product extends RecyclerView.Adapter<AdapterR_Product.ProductViewHolder>{
    private Context context;
    private ArrayList<IModel_Content> al_contents;
    private AdapterR_Product.RecyclerTouchListener listener;
    private AdapterR_Product.CustomFilter customFilter;
    private ArrayList<IModel_Content> al_Productos_filter;

    public AdapterR_Product(Context context, ArrayList<IModel_Content> al_contents){
        this.context = context;
        this.al_contents = al_contents;
        this.customFilter= new AdapterR_Product.CustomFilter(AdapterR_Product.this);
        this.al_Productos_filter=new ArrayList<>();
        al_Productos_filter.addAll(al_contents);
    }

    @Override
    public int getItemCount(){
        return al_Productos_filter.size();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_product,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position){
        IModel_Content content = al_Productos_filter.get(position);

        if(content instanceof ModelAd){
            onBindViewHolderAds(holder,position);
        }else {
            onBindViewHolderProducts(holder,position);
        }

    }


    private void onBindViewHolderAds(ProductViewHolder holder, int position){
        ModelAd ad = (ModelAd) al_Productos_filter.get(position);

        Glide.with(context)
                .load(Constants.PHP_IMAGES_AD+"Ad_"+ad.getId()+".jpg")
                .error(ContextCompat.getDrawable(context,R.drawable.shopping_bag_white))
                .centerCrop()
                .into(holder.image);

        holder.name.setText(ad.getName());
        holder.price.setVisibility(View.GONE);
        holder.descProduct.setVisibility(View.GONE);
        holder.TV_Ad.setVisibility(View.VISIBLE);
    }

    private void onBindViewHolderProducts(ProductViewHolder holder, int position){
        IModel_Content iproduct = (IModel_Content) al_Productos_filter.get(position);
        ModelProduct product = (ModelProduct) iproduct;

        Glide.with(context)
                .load(Constants.PHP_IMAGES+"P_"+product.getIdProduct()+".jpg")
                .error(ContextCompat.getDrawable(context,R.drawable.shopping_bag_white))
                .centerCrop()
                .into(holder.image);

        holder.name.setText(product.getName());
        String priceStr=product.getPrice()+" CUP";
        holder.price.setText(priceStr);
        holder.descProduct.setText(product.getDesc());
        holder.TV_Ad.setVisibility(View.GONE);
    }

    public void setClickListener(RecyclerTouchListener listener){
        if(listener != null) this.listener=listener;
    }

    protected class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        TextView price;
        TextView descProduct;
        TextView TV_Ad;

        public ProductViewHolder(final View itemView){
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.RP_IV_ImageProduct);
            name=(TextView)itemView.findViewById(R.id.RP_TV_name);
            price=(TextView)itemView.findViewById(R.id.RP_TV_Price);
            descProduct=(TextView)itemView.findViewById(R.id.RP_TV_descProduct);
            TV_Ad = (TextView) itemView.findViewById(R.id.RP_Ad);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(al_Productos_filter.get(getAdapterPosition()) instanceof ModelProduct){
                        if(listener!=null) listener.onClickProduct(itemView,getAdapterPosition(),al_Productos_filter);
                    }else{
                        if(listener!=null) listener.onClickAd(itemView,getAdapterPosition(), al_Productos_filter);
                    }
                }
            });
        }

    }

    public interface RecyclerTouchListener{
        void onClickAd(View v, int position, ArrayList<IModel_Content> al_filter);
        void onClickProduct(View v,int position, ArrayList<IModel_Content> al_filter);
    }


    public Filter getFilter() {
        return customFilter;
    }

    public class CustomFilter extends Filter {
        AdapterR_Product adapterR_Products;

        public CustomFilter(AdapterR_Product adapterR_Products) {
            super();
            this.adapterR_Products = adapterR_Products;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            al_Productos_filter.clear();



            FilterResults filterResults=new FilterResults();
            if(charSequence.length()==0){
                al_Productos_filter.addAll(al_contents);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for( IModel_Content content:al_contents){
                    if(content instanceof ModelProduct){
                        ModelProduct product=(ModelProduct) content;
                        if(product.getName().toLowerCase().trim().contains(filterPattern)){
                            al_Productos_filter.add(content);
                        }
                    }else{
                        ModelAd ad=(ModelAd) content;
                        if(ad.getName().toLowerCase().trim().contains(filterPattern)){
                            al_Productos_filter.add(content);
                        }
                    }

                }
            }
            filterResults.values=al_Productos_filter;
            filterResults.count=al_Productos_filter.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapterR_Products.notifyDataSetChanged();
        }
    }

}