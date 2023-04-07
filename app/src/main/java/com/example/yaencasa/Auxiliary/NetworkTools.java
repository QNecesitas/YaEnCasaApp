package com.example.yaencasa.Auxiliary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.yaencasa.R;

public class NetworkTools
{

    public static boolean isOnline(Context context, boolean mostrarAlertDialg){
        boolean resutl;
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();

        resutl= ni != null&&ni.isConnected();
        if(!resutl&&mostrarAlertDialg){
            showAlertDialogNoInternet(context);

        }
        return resutl;
    }
    public static void showAlertDialogNoInternet(Context context) {
        //init alert dialog
        AlertDialog.Builder builder =  new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(context.getString(R.string.Se_ha_producido_un_error));
        builder.setMessage(R.string.Revise_su_conexion);
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

}
