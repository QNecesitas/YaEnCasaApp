package com.example.yaencasa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaencasa.Data.ModelOrder;
import com.example.yaencasa.R;

import java.util.ArrayList;


public class AdapterR_Orders extends RecyclerView.Adapter<AdapterR_Orders.ProductosViewHolder>{
    private Context context;
    private ArrayList<ModelOrder> al_Pedido;
    private RecyclerTouchListener listener_cancel,listener_accept,listener_finished, listener_ubic;

    public void setListener_ubic(RecyclerTouchListener listener_ubic) {
        this.listener_ubic = listener_ubic;
    }


    public interface RecyclerTouchListener{
        void onClickItem(View v, int position);
    }

    public AdapterR_Orders(Context context, ArrayList<ModelOrder> array_Pedido){
        this.context=context;
        this.al_Pedido = array_Pedido;
    }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_orders,parent, false);
        return new ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductosViewHolder holder, int position){

        ModelOrder modelo = al_Pedido.get(position);

        String price=modelo.getPrice()+" CUP";



        String number=String.valueOf(modelo.getId());
        String phone_number=String.valueOf(modelo.getCelnumber());
        String product=String.valueOf(modelo.getProducts());
        String address= modelo.getAddress();
        String date=String.valueOf(modelo.getActDate());
        String name=modelo.getName();
        String location=modelo.getLocation();

        holder.tv_number_order.setText(number);
        holder.tv_price.setText(price);
        holder.tv_phone_number.setText(phone_number);
        holder.tv_state.setText(modelo.getState());
        holder.tv_product.setText(product.replaceAll("--n--","\n"));
        holder.tv_address.setText(address);
        holder.tv_date.setText(date);
        holder.tv_name.setText(name);
        holder.tv_zone.setText(location);

        switch (modelo.getState()){
            case "En espera":
                holder.tv_state.setBackgroundColor(ContextCompat.getColor(context,R.color.blue_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.blue));
                break;
            case "Finalizado":
                holder.tv_state.setBackgroundColor(ContextCompat.getColor(context,R.color.green_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.green));
                break;

            case "Aceptado":
                holder.tv_state.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.yellow));
                break;

            case "Cancelado":
                holder.tv_state.setBackground(ContextCompat.getDrawable(context,R.color.red_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.red));
                break;
        }

    }


    public void setListener_cancel(RecyclerTouchListener listener_cancel) {
        this.listener_cancel = listener_cancel;
    }

    public void setListener_accept(RecyclerTouchListener listener_accept) {
        this.listener_accept = listener_accept;
    }

    public void setListener_finished(RecyclerTouchListener listener_finished) {
        this.listener_finished = listener_finished;
    }

    @Override
    public int getItemCount(){
        return al_Pedido.size();
    }


    class ProductosViewHolder extends RecyclerView.ViewHolder{
        TextView tv_number_order;
        TextView tv_price;
        TextView tv_phone_number;
        TextView tv_state;
        TextView tv_product;
        TextView tv_address;
        Button btn_cancel;
        Button btn_accept;
        Button btn_finished;
        TextView tv_date;
        TextView tv_name;
        TextView tv_zone;
        Button btn_ubic;


        public ProductosViewHolder(final View itemView){
            super(itemView);

            tv_number_order=(TextView)itemView.findViewById(R.id.RO_numberOrder);
            tv_price=(TextView)itemView.findViewById(R.id.RO_price);
            tv_phone_number=(TextView)itemView.findViewById(R.id.RO_phoneNumber);
            tv_state=(TextView)itemView.findViewById(R.id.RO_state);
            tv_product=(TextView)itemView.findViewById(R.id.RO_product);
            tv_address=(TextView)itemView.findViewById(R.id.RO_address);
            btn_cancel=(Button)itemView.findViewById(R.id.RO_btn_cancel);
            btn_accept=(Button)itemView.findViewById(R.id.RO_btn_accept);
            btn_finished=(Button)itemView.findViewById(R.id.RO_btn_finished);
            tv_date=(TextView)itemView.findViewById(R.id.RO_date);
            tv_name=(TextView)itemView.findViewById(R.id.RO_nombre);
            tv_zone=(TextView)itemView.findViewById(R.id.RO_zone);
            btn_ubic = (Button) itemView.findViewById(R.id.RO_btn_ubic);

            btn_cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener_cancel!=null) listener_cancel.onClickItem(itemView,getAdapterPosition());
                }
            });

            btn_accept.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener_accept!=null) listener_accept.onClickItem(itemView,getAdapterPosition());
                }
            });

            btn_finished.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener_finished!=null) listener_finished.onClickItem(itemView,getAdapterPosition());
                }
            });

            btn_ubic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener_ubic != null) listener_ubic.onClickItem(itemView, getAdapterPosition());
                }
            });

        }

    }


}
