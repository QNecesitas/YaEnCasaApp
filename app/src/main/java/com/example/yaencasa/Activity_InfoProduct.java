package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.CurrentProduct;
import com.example.yaencasa.Data.ProductOnCar;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

public class Activity_InfoProduct extends AppCompatActivity {


    private ImageView image;
    private TextView name;
    private TextView description;
    private EditText et_amount;
    private ImageView btn_less;
    private ImageView btn_more;
    private TextView tv_price;

    private int amount = 1;
    private double productPrice;
    private double totalPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_product);

        //Binding views from XML...
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.AIP_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        image=(ImageView)findViewById(R.id.aip_iv);
        name = (TextView) findViewById(R.id.aip_tv_nombre);
        description = (TextView) findViewById(R.id.aip_tv_descripcion);
        et_amount = (EditText) findViewById(R.id.aip_et_cantidad);
        btn_less = (ImageView) findViewById(R.id.aip_btn_less);
        btn_more = (ImageView) findViewById(R.id.aip_btn_more);
        tv_price = (TextView) findViewById(R.id.aip_precio);

        //Setting values to show info
        loadImage();
        name.setText(CurrentProduct.name);
        description.setText(CurrentProduct.description);
        productPrice = CurrentProduct.price;
        totalPrice = productPrice;
        tv_price.setText(totalPrice+" CUP");

        //Listeners
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_amount.getText().toString().trim().isEmpty()) {
                    amount = 0;
                    totalPrice = productPrice * amount;
                    tv_price.setText(getString(R.string._0_00)+" CUP");
                } else if (et_amount.getText().toString().equals("0")) {
                    amount = 1;
                    totalPrice = productPrice * amount;
                    String txt_price = totalPrice + " CUP";
                    tv_price.setText(txt_price);
                    et_amount.setText(String.valueOf(amount));
                } else {
                    amount = Integer.parseInt(et_amount.getText().toString());
                    totalPrice = productPrice * amount;
                    String precioText = totalPrice + " CUP";
                    tv_price.setText(precioText);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    //Glide for set the image's product
    private void loadImage() {
        Glide.with(Activity_InfoProduct.this)
                .load(Constants.PHP_IMAGES + "P_" + CurrentProduct.id + ".jpg")
                .error(ContextCompat.getDrawable(Activity_InfoProduct.this,R.drawable.shopping_bag_white))
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

    //Buttons
    public void sendProductToCar(View view) {
        if(et_amount.getText().toString().trim().isEmpty()){
            FancyToast.makeText(Activity_InfoProduct.this,getString(R.string.amount_required), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }
        else {
            ProductOnCar productOnCar=new ProductOnCar(CurrentProduct.name,totalPrice,amount);
            CurrentProduct.shoppingCar.add(productOnCar);
            finish();
            FancyToast.makeText(Activity_InfoProduct.this,getString(R.string.sended_to_shopping_car), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
        }
    }
    public void cancel(View view) {
        finish();
    }
    public void decreaseAmount(View view) {
        if (amount > 1) {
            amount--;
            totalPrice=productPrice*amount;
            tv_price.setText(String.valueOf(totalPrice));
            et_amount.setText(""+amount);
        }

    }
    public void increaseAmount(View view) {
        amount++;
        totalPrice=productPrice*amount;
        tv_price.setText(String.valueOf(totalPrice));
        et_amount.setText(""+amount);
    }

}