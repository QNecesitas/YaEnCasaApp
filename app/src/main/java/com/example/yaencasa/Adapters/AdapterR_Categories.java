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
import com.example.yaencasa.R;

import java.util.ArrayList;

public class AdapterR_Categories extends RecyclerView.Adapter<AdapterR_Categories.CategoriasViewHolder>{

    private Context context;
    private ArrayList<ModelCategory> pila_categorias;
    private RecyclerTouchListener listener;



    public interface RecyclerTouchListener{
        void onClickItem(View v, int position);
    }

    public AdapterR_Categories(Context context, ArrayList<ModelCategory> pila_categorias){
        this.context=context;
        this.pila_categorias = pila_categorias;
    }


    public CategoriasViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_category,null);
        return new CategoriasViewHolder(view);
    }

    public void onBindViewHolder(CategoriasViewHolder holder, int position){
        ModelCategory modelCategory = pila_categorias.get(position);

        Glide.with(context)
                .load(Constants.PHP_IMAGES +"C_"+modelCategory.getName()+".jpg")
                .centerCrop()
                .into(holder.imagenview);

        holder.nombre_categoria.setText(modelCategory.getName());
        String cantProductText=modelCategory.getAmount()+" "+context.getString(R.string.productos);
        holder.cant_productos.setText(cantProductText);

    }

    public void setClickListener(RecyclerTouchListener listener){
        this.listener=listener;
    }

    public int getItemCount(){
        return pila_categorias.size();
    }


    class CategoriasViewHolder extends RecyclerView.ViewHolder{
        TextView nombre_categoria;
        TextView cant_productos;
        ImageView imagenview;

        public CategoriasViewHolder(final View itemView){
            super(itemView);

            nombre_categoria=(TextView)itemView.findViewById(R.id.recycler_categoria_nombre);
            cant_productos=(TextView)itemView.findViewById(R.id.recycler_categoria_cant_productos);
            imagenview=(ImageView)itemView.findViewById(R.id.imagen_recycler_categorias);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener!=null) listener.onClickItem(itemView,getAdapterPosition());
                }
            });


        }

    }
}

