package com.example.yaencasa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.IModel_Content;
import com.example.yaencasa.Data.ModelElement;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.R;

import java.util.ArrayList;


public class AdapterR_Shopping_cart extends RecyclerView.Adapter<AdapterR_Shopping_cart.CarritoViewHolder>{
    private Context context;
    private ArrayList<ModelElement> al_contents;
    private RecyclerTouchListener listener;


    public interface RecyclerTouchListener{
        void onClickItem(View v, int position);
    }
    public void setClickListener(RecyclerTouchListener listener){
        this.listener=listener;
    }


    public AdapterR_Shopping_cart(Context context, ArrayList<ModelElement> array_shopping_cart){
        this.context=context;
        this.al_contents = array_shopping_cart;
    }


    @Override
    public CarritoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_shopping_cart,parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarritoViewHolder holder, int position){
        ModelElement element = al_contents.get(position);

        Glide.with(context)
                .load(Constants.PHP_IMAGES+"P_"+element.getProduct().getIdProduct()+".jpg")
                .error(ContextCompat.getDrawable(context,R.drawable.shopping_bag))
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imageview);

        holder.name.setText(element.getProduct().getName());
        String price="Precio: "+element.getPrice()+" CUP";
        holder.price.setText(price);
        String cantidad = "Cantidad: "+element.getAmount();
        holder.tv_amount.setText(cantidad);
    }

    @Override
    public int getItemCount(){
        return al_contents.size();
    }

    class CarritoViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView tv_amount;
        ImageView imageview;
        ImageView iv_delete;

        public CarritoViewHolder(final View itemView){
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.RSC_TV_name);
            price=(TextView)itemView.findViewById(R.id.RSC_TV_price);
            imageview=(ImageView)itemView.findViewById(R.id.RSC_IV_productPicture);
            iv_delete=(ImageView)itemView.findViewById(R.id.RSC_IV_delete);
            tv_amount = (TextView) itemView.findViewById(R.id.RSC_TV_amount);


            iv_delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener!=null) listener.onClickItem(itemView,getAdapterPosition());
                }
            });

        }

    }

}
