package com.example.yaencasa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.ModelCategory;
import com.example.yaencasa.Data.ModelZone;
import com.example.yaencasa.R;

import java.util.ArrayList;

public class AdapterR_Zones extends RecyclerView.Adapter<AdapterR_Zones.ZonesViewHolder>{

    private Context context;
    private ArrayList<ModelZone> pila_Zones;
    private AdapterR_Zones.RecyclerTouchListener listener;



    public interface RecyclerTouchListener{
        void onClickItem(View v, int position);
    }

    public AdapterR_Zones(Context context, ArrayList<ModelZone> pila_Zones){
        this.context=context;
        this.pila_Zones = pila_Zones;
    }


    public ZonesViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_zone,null);
        return new ZonesViewHolder(view);
    }

    public void onBindViewHolder(ZonesViewHolder holder, int position){
        ModelZone modelZone = pila_Zones.get(position);

        Glide.with(context)
                .load(Constants.PHP_IMAGES +"Z_"+modelZone.getName()+".jpg")
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.city);

        holder.name_zone.setText(modelZone.getName());
        holder.price.setText(modelZone.getPrice());


    }

    public void setClickListenerDelete(RecyclerTouchListener listener){
        this.listener=listener;
    }

    public int getItemCount(){
        return pila_Zones.size();
    }


    class ZonesViewHolder extends RecyclerView.ViewHolder{
        TextView name_zone;
        TextView price;
        ImageView city;


        public ZonesViewHolder(final View itemView){
            super(itemView);

            name_zone=(TextView)itemView.findViewById(R.id.RZ_TV_name);
            price=(TextView)itemView.findViewById(R.id.RZ_TV_price);
            city=(ImageView)itemView.findViewById(R.id.RZ_IV);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener!=null) listener.onClickItem(itemView,getAdapterPosition());
                }
            });


        }

    }

}
