package com.example.yaencasa.Auxiliary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    //Comprobar si hay permisos
    public static boolean siHayPermisoDeAlmacenamiento(Context context){

        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }

    }
    public static boolean siHayPermisoDeInfoTelefono(Context context){
        int result= ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if(result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{ return false;}
    }
    public static boolean siHayPermisoDeAbrirCamara(Context context){

        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if(result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }

    }

    //PedirPermisos
    public static void pedirPermisoDeAlmacenamiento(Activity activity, int STORAGE_PERMISSION_CODE) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    public static void pedirPermisoDeInfoTelefono(Activity activity, int PERMISSION_READ_STATE_CODE){

        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_READ_STATE_CODE);
    }
    public static void pedirPermisoDeAbrirCamara(Activity activity, int CAMERA_PERMISSION_CODE){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

}
