package com.example.yaencasa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaencasa.Data.ModelOrder;
import com.example.yaencasa.R;

import java.util.ArrayList;


public class AdapterR_MyOrders extends RecyclerView.Adapter<AdapterR_MyOrders.ProductosViewHolder>{
    private Context context;
    private ArrayList<ModelOrder> al_MisPedidos;
    private RecyclerTouchListener listener_address;



    public interface RecyclerTouchListener{
        void onClickItem(View v, int position);
    }

    public AdapterR_MyOrders(Context context, ArrayList<ModelOrder> array_MisPedido){
        this.context=context;
        this.al_MisPedidos = array_MisPedido;
        }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_my_orders,null);
        return new ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductosViewHolder holder, int position){

        ModelOrder modelo = al_MisPedidos.get(position);

        String price=modelo.getPrice()+" CUP";

        String number=String.valueOf(modelo.getId());
        String phone_number=String.valueOf(modelo.getCelnumber());
        String product=String.valueOf(modelo.getProducts());
        String address=makeAddress(String.valueOf(modelo.getAddress()));
        String date=String.valueOf(modelo.getActDate());
        String nombre = modelo.getName();
        String zonaEntrega = modelo.getZone();

        holder.tv_number_order.setText(number);
        holder.tv_price.setText(price);
        holder.tv_phone_number.setText(phone_number);
        holder.tv_product.setText(product.replaceAll("--n--","\n"));
        holder.tv_address.setText(address);
        holder.tv_date.setText(date);
        holder.tv_nombre.setText(nombre);
        holder.tv_zona.setText(zonaEntrega);


        switch (modelo.getState()){
            case "En espera":
                holder.tv_state.setText(R.string.En_espera_extendido);
                holder.tv_state.setBackgroundColor(ContextCompat.getColor(context,R.color.blue_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.blue));
                break;

            case "Aceptado":
                String aceptadoBy=context.getString(R.string.Pedido_aceptado);
                holder.tv_state.setText(aceptadoBy);
                holder.tv_state.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.yellow));
                break;

            case "Cancelado":
                String cancelBy=context.getString(R.string.Pedido_cancelado)+"\n"+modelo.getDescr();
                holder.tv_state.setText(cancelBy);
                holder.tv_state.setBackgroundColor(ContextCompat.getColor(context,R.color.red_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.red));
                break;

            case "Finalizado":
                holder.tv_state.setText(R.string.pedido_ya_entregado);
                holder.tv_state.setBackgroundColor(ContextCompat.getColor(context,R.color.green_light));
                holder.tv_state.setTextColor(ContextCompat.getColor(context,R.color.green));
                break;

        }

    }

    public String makeAddress(String origin){
        origin=origin.replace("signNumeral","#")
                .replace("signSlach","/")
                .replace("signBackSlach","\\")
                .replace("signGuion","-")
                .replace("signBajoGuion","_");
        return origin;
    }

    public void setListener_address(RecyclerTouchListener listener_address) {
        this.listener_address = listener_address;
    }

    @Override
    public int getItemCount(){
        return al_MisPedidos.size();
    }


    class ProductosViewHolder extends RecyclerView.ViewHolder{
        TextView tv_number_order;
        TextView tv_price;
        TextView tv_phone_number;
        TextView tv_state;
        TextView tv_product;
        TextView tv_address;
        TextView tv_date;
        TextView tv_nombre;
        TextView tv_zona;

        public ProductosViewHolder(final View itemView){
            super(itemView);

            tv_number_order=(TextView)itemView.findViewById(R.id.RMO_numberOrder);
            tv_price=(TextView)itemView.findViewById(R.id.RMO_price);
            tv_phone_number=(TextView)itemView.findViewById(R.id.RMO_phoneNumber);
            tv_state=(TextView)itemView.findViewById(R.id.RMO_state);
            tv_product=(TextView)itemView.findViewById(R.id.RMO_product);
            tv_address=(TextView)itemView.findViewById(R.id.RMO_address);
            tv_date=(TextView)itemView.findViewById(R.id.RMO_date);
            tv_nombre = (TextView) itemView.findViewById(R.id.RMO_name);
            tv_zona = (TextView) itemView.findViewById(R.id.RMO_zone);

        }

    }


}
