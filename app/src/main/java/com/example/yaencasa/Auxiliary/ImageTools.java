package com.example.yaencasa.Auxiliary;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageTools {
    public static final int ANCHO_DE_FOTO_A_SUBIR = 900;
    public static final int ALTO_DE_FOTO_A_SUBIR = 900;

    public static String convertImageString(Bitmap bitmap){
        Bitmap BitRec = Bitmap.createScaledBitmap(bitmap, ANCHO_DE_FOTO_A_SUBIR, ALTO_DE_FOTO_A_SUBIR, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitRec.compress(Bitmap.CompressFormat.JPEG, 28, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String getHoraActual(String SumaDeFormatos) {
        return new SimpleDateFormat(SumaDeFormatos,
                Locale.getDefault()).format(new Date());
    }

    public static File createTempImageFile(Context context, String nombre) throws IOException {
        File storageDir = context.getExternalCacheDir();
        return File.createTempFile(nombre, ".png", storageDir);
    }

    public static File obtenerTempImageFile(Context context, String nombre) throws IOException {
        File storageDir = context.getExternalCacheDir();
        return new File(storageDir, nombre);
    }

}
