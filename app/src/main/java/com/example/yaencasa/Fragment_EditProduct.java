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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yaencasa.Adapters.AdapterR_EditProduct;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Auxiliary.IDCreater;
import com.example.yaencasa.Auxiliary.ImageTools;
import com.example.yaencasa.Auxiliary.NetworkTools;
import com.example.yaencasa.Auxiliary.Permissions;
import com.example.yaencasa.Data.ContentRepository;
import com.example.yaencasa.Data.IModel_Content;
import com.example.yaencasa.Data.ModelCategory;
import com.example.yaencasa.Data.ModelProduct;
import com.example.yaencasa.Data.Network.RetrofitProductsImpl;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_EditProduct extends Fragment {

    //AddEditProduct
    private int amount;

    //Eliminiar
    private ImageView li_imagen_producto;

    //Recycler
    private RecyclerView recycler;
    private ArrayList<IModel_Content> array_content;
    private ArrayList<ModelCategory> array_categories;
    private AdapterR_EditProduct adapterR_editProducts;


    //Categories
    private TextView TV_filter_category;
    LinearLayout linearLayoutEmpty;

    //Internet
    private ProgressDialog progressDialogAdding, progressDialogCargando,
            progressDialogEliminado, progressDialogActualizando;
    private Integer selected_category;
    private final int INTENT_RESULT_GALERIA = 5;
    private final int PERMISO_GALERIA = 3;
    private Uri uriLLegadaRecortada;
    private ContentRepository contentRepository;
    private RetrofitProductsImpl retrofitProducts;
    private String imageFile="no";










    public Fragment_EditProduct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment__edit_product, container, false);

        selected_category = getActivity().getIntent().getIntExtra("category", 0);


        //RecyclerView
        recycler = (RecyclerView) root.findViewById(R.id.FEP_recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        array_content = new ArrayList<>();
        adapterR_editProducts = new AdapterR_EditProduct(getContext(), array_content);
        array_categories = new ArrayList<>();
        
        TV_filter_category = (TextView) root.findViewById(R.id.FEP_TV_Category);
        linearLayoutEmpty = (LinearLayout) root.findViewById(R.id.FEP_imagen_empty);

        //Search
        SearchView searchView = (SearchView) root.findViewById(R.id.FEP_SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterR_editProducts.getFilter().filter(s);
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
                    array_content=arrayList;
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



    //Add element
    public void click_FABadd(View view) {
        if (array_content.size() < 700) {
            liAddElement();
        } else {
            showAlertDialogToMuch();
        }
    }
    private void liAddElement() {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        View view = layoutInflater.inflate(R.layout.li_editproduct, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //Declaraciones
        ImageView imageView = (ImageView) view.findViewById(R.id.Image_edit_product);
        EditText et_nombre = (EditText) view.findViewById(R.id.ET_Nombre);
        EditText et_precioCUP = (EditText) view.findViewById(R.id.ET_PrecioCUP);
        EditText et_desc = (EditText) view.findViewById(R.id.ET_Desc);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_add = (Button) view.findViewById(R.id.btn_add);


        //Inicializaciones
        li_imagen_producto = imageView;
        amount = 1;

        //Listeners

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInfoDataAdd(et_desc,et_nombre,et_precioCUP)) {

                    alertDialog.dismiss();
                    progressDialogAdding = ProgressDialog.show(requireContext(), getString(R.string.Agregando_producto), getString(R.string.Espere), false, false);
                    addInfoInternet(et_desc,et_nombre,et_precioCUP);
                }
            }
        });

        li_imagen_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), li_imagen_producto);
                popupMenu.getMenuInflater().inflate(R.menu.menu_image_del_add, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_image_add) {
                            escogerimagenGaleria(INTENT_RESULT_GALERIA);
                        } else if (menuItem.getItemId() == R.id.menu_image_del) {
                            li_imagen_producto.setImageDrawable(null);
                            imageFile="no";
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        //Finalizado
        builder.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void addInfoInternet(EditText et_desc, EditText et_nombre,
                                 EditText et_precioCUP){
        Call<String> call = retrofitProducts.addProduct(
                Constants.PHP_TOKEN,
                imageFile,
                selected_category,
                IDCreater.generate(),
                et_nombre.getText().toString(),
                Double.parseDouble(et_precioCUP.getText().toString()),
                et_desc.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialogAdding.dismiss();
                FancyToast.makeText(requireContext(), getString(R.string.Operacion_realizada_con_exito), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                loadMainInternetInfo();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialogAdding.dismiss();
                showAlertDialogNoInternet();
            }
        });
    }



    private boolean checkInfoDataAdd(EditText et_desc, EditText et_nombre,
                                     EditText et_precioCUP){
        int amountTrue=0;

        //1- Descripcion
        if (!et_desc.getText().toString().trim().isEmpty()) {
            amountTrue++;
        }else{
            et_desc.setError(getString(R.string.Este_campo_no_puede_vacio));
        }


        //2- Precio CUP
        if (!et_precioCUP.getText().toString().trim().isEmpty()) {
            amountTrue++;
        }else{
            et_precioCUP.setError(getString(R.string.Este_campo_no_puede_vacio));
        }

        //3- Nombre
        if (!et_nombre.getText().toString().trim().isEmpty()) {
            amountTrue++;
        }else{
            et_nombre.setError(getString(R.string.Este_campo_no_puede_vacio));
        }

        //4- Internet
        if (NetworkTools.isOnline(requireContext(), true)) {
            amountTrue++;
        }

        return amountTrue==4;
    }
    private boolean checkInfoDataEdit(EditText et_desc,
                                      EditText et_nombre,
                                      EditText et_precioCUP){
        int amountTrue=0;

        //1- Descripcion
        if (!et_desc.getText().toString().trim().isEmpty()) {
            amountTrue++;
        }else{
            et_desc.setError(getString(R.string.Este_campo_no_puede_vacio));
        }


        //2- Precio CUP
        if (!et_precioCUP.getText().toString().trim().isEmpty()) {
            amountTrue++;
        }else{
            et_precioCUP.setError(getString(R.string.Este_campo_no_puede_vacio));
        }

        //3- Nombre
        if (!et_nombre.getText().toString().trim().isEmpty()) {
            amountTrue++;
        }else{
            et_nombre.setError(getString(R.string.Este_campo_no_puede_vacio));
        }

        //4- Internet
        if(NetworkTools.isOnline(requireContext(), true)) {
            amountTrue++;
        }

        return amountTrue==4;
    }
    public void showAlertDialogToMuch() {
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.Demasiados_elementos);
        builder.setMessage(R.string.Ha_excedido_numero_maximo_productos);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }




    //Options product
    private void li_options_product(int position) {

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.li_options_product, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        //Declarations
        ImageView iv_visibility = (ImageView) view.findViewById(R.id.liop_iv_visibility);
        TextView tv_nombre = (TextView) view.findViewById(R.id.liop_tv_nombre);
        ImageView imageView = (ImageView) view.findViewById(R.id.liop_iv);
        TextView tv_precioCUP = (TextView) view.findViewById(R.id.liop_tv_precioCUP);
        TextView tv_precioRebajaCUP = (TextView) view.findViewById(R.id.liop_tv_precioRebajaCUP);
        TextView tv_descr = (TextView) view.findViewById(R.id.liop_tv_descripcion);
        ImageView iv_close = (ImageView) view.findViewById(R.id.liop_btn_close);
        Button btn_turnVisibility = (Button) view.findViewById(R.id.liop_btn_turnVisibility);
        Button btn_edit = (Button) view.findViewById(R.id.liop_btn_edit);
        Button btn_delete = (Button) view.findViewById(R.id.liop_btn_delete);


        //Fill product
        IModel_Content iModel_content = array_content.get(position);
        tv_nombre.setText(array_content.get(position).getName());
        
        
        ModelProduct modelProduct = (ModelProduct) iModel_content; 
        String precioCUP = modelProduct.getPrice() + " CUP";
        tv_precioCUP.setText(precioCUP);
        tv_descr.setText(modelProduct.getDesc());
        
        if (modelProduct.getState()) {
            iv_visibility.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.visibility));
            btn_turnVisibility.setText(R.string.Ocultar);
        } else {
            iv_visibility.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.visibility_off));
            btn_turnVisibility.setText(R.string.Mostrar);
        }
        Glide.with(requireContext())
                .load(Constants.PHP_IMAGES + "P_" + modelProduct.getId() + ".jpg")
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);


        //Listener
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_turnVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkTools.isOnline(requireContext(), true)) {
                    alertDialog.dismiss();
                    progressDialogActualizando = ProgressDialog.show(requireContext(), getString(R.string.Actualizando), getString(R.string.Espere), false, false);
                    updateVisibility(position);
                }
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                li_editElement(position, imageView.getDrawable());
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                showAlertDialogDeleteProduct(position);
            }
        });


        //Finalizado
        builder.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    //Others
    private void updateVisibility(int position) {

        Call<String> call = retrofitProducts.updateVisibilityProduct(
                Constants.PHP_TOKEN,
                ((ModelProduct)array_content.get(position)).getId(),
                ((ModelProduct)array_content.get(position)).getState()?0:1
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialogActualizando.dismiss();
                FancyToast.makeText(requireContext(), getString(R.string.Operacion_realizada_con_exito), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                ((ModelProduct)array_content.get(position)).setState(
                        !((ModelProduct)array_content.get(position)).getState()
                );
                updateRecyclerAdapter();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialogActualizando.dismiss();
                NetworkTools.showAlertDialogNoInternet(requireContext());
            }
        });
    }
    private void deleteProduct(int position){
        Call<String> call = retrofitProducts.removeProduct(
                Constants.PHP_TOKEN,
                ((ModelProduct)array_content.get(position)).getId()
                );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialogEliminado.dismiss();
                FancyToast.makeText(requireContext(), getString(R.string.Operacion_realizada_con_exito), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                loadMainInternetInfo();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialogEliminado.dismiss();
                NetworkTools.showAlertDialogNoInternet(requireContext());
            }
        });
    }
    private void showAlertDialogDeleteProduct(int position) {
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.Eliminar_producto);
        builder.setMessage(R.string.Desea_eliminar_producto);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (NetworkTools.isOnline(requireContext(), true)) {
                    dialog.dismiss();
                    progressDialogEliminado = ProgressDialog.show(requireContext(), getString(R.string.Eliminando_producto), getString(R.string.Espere), false, false);
                    deleteProduct(position);
                }
            }
        });
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }




    //EditProduct
    private void li_editElement(int position, Drawable drawable) {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        View view = layoutInflater.inflate(R.layout.li_editproduct, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //Declaraciones
        li_imagen_producto = (ImageView) view.findViewById(R.id.Image_edit_product);
        EditText et_nombre = (EditText) view.findViewById(R.id.ET_Nombre);
        EditText et_precioCUP = (EditText) view.findViewById(R.id.ET_PrecioCUP);
        EditText et_desc = (EditText) view.findViewById(R.id.ET_Desc);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_add = (Button) view.findViewById(R.id.btn_add);

        //Fill product
        ModelProduct modelProduct = (ModelProduct) array_content.get(position);
        et_nombre.setText(modelProduct.getName());
        String precioCUP = String.valueOf(modelProduct.getPrice());
        et_precioCUP.setText(precioCUP);
        et_desc.setText(modelProduct.getDesc());
        li_imagen_producto.setImageDrawable(drawable);

        //Listeners
        li_imagen_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), li_imagen_producto);
                popupMenu.getMenuInflater().inflate(R.menu.menu_image_del_add, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_image_add) {
                            escogerimagenGaleria(INTENT_RESULT_GALERIA);
                        } else if (menuItem.getItemId() == R.id.menu_image_del) {
                            li_imagen_producto.setImageDrawable(null);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInfoDataEdit(et_desc,et_nombre,et_precioCUP)) {
                    alertDialog.dismiss();
                    progressDialogActualizando = ProgressDialog.show(requireContext(), getString(R.string.Actualizando), getString(R.string.Espere), false, false);
                    editInfoInternet(et_desc,et_nombre,et_precioCUP);
                }
            }
        });



        //Finalizado
        builder.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void editInfoInternet(EditText et_desc, EditText et_nombre,
                                 EditText et_precioCUP){
        Call<String> call = retrofitProducts.updateProduct(
                Constants.PHP_TOKEN,
                imageFile,
                IDCreater.generate(),
                et_nombre.getText().toString(),
                Double.parseDouble(et_precioCUP.getText().toString()),
                et_desc.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialogActualizando.dismiss();
                FancyToast.makeText(requireContext(), getString(R.string.Operacion_realizada_con_exito), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                loadMainInternetInfo();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialogActualizando.dismiss();
                showAlertDialogNoInternet();
            }
        });
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

        adapterR_editProducts = new AdapterR_EditProduct(requireContext(), array_content);
        adapterR_editProducts.setClickListener(new AdapterR_EditProduct.RecyclerTouchListener() {
                                                   @Override
                                                   public void onClickAd(View v, int position) {

                                                   }

                                                   @Override
                                                   public void onClickProduct(View v, int position) {
                                                       li_options_product(position);
                                                   }
                                               });
        recycler.setAdapter(adapterR_editProducts);
        progressDialogCargando.dismiss();
    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        //Galeria
        if (requestCode == INTENT_RESULT_GALERIA) {
            if (data != null) {
                Uri contentUri = data.getData();
                try {
                    File file = ImageTools.createTempImageFile(requireContext(), ImageTools.getHoraActual("yyMMddHHmmss"));
                    recortarImagen(contentUri, Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), R.string.error_obtener_imagen, Toast.LENGTH_SHORT).show();
                }
            }
        }


        //UCrop
        if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                uriLLegadaRecortada = UCrop.getOutput(data);
                li_imagen_producto.setImageURI(uriLLegadaRecortada);
                Bitmap bitmap = ((BitmapDrawable) li_imagen_producto.getDrawable()).getBitmap();
                imageFile = ImageTools.convertImageString(bitmap);
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_obtener_imagen), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == UCrop.RESULT_ERROR) {
            Toast.makeText(requireContext(), getString(R.string.error_obtener_imagen), Toast.LENGTH_SHORT).show();
        }
    }
    private void escogerimagenGaleria(int INTENT_RESULT_GALERIA){

        if (Permissions.siHayPermisoDeAlmacenamiento(requireContext())) {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, INTENT_RESULT_GALERIA);

        } else {
            Permissions.pedirPermisoDeAlmacenamiento(requireActivity(), PERMISO_GALERIA);
        }

    }
    private void recortarImagen(Uri uri1, Uri uri2) {
        try {
            UCrop.of(uri1, uri2)
                    .withAspectRatio(3, 3)
                    .withMaxResultSize(ImageTools.ANCHO_DE_FOTO_A_SUBIR, ImageTools.ALTO_DE_FOTO_A_SUBIR)
                    .start(requireActivity());
        } catch (Exception e) {
            Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

    }


    //Auxiliares
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISO_GALERIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                escogerimagenGaleria(INTENT_RESULT_GALERIA);
            } else {
                showAlertDialogPermisoDenegado();
            }
        }
    }
    public void showAlertDialogPermisoDenegado() {
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.permiso_denegado);
        builder.setMessage(R.string.debe_otorgar_permisos_galeria);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create the alert dialog and show it
        builder.create().show();
    }


    @Override
    public void onResume() {
        super.onResume();
        loadMainInternetInfo();
    }


}