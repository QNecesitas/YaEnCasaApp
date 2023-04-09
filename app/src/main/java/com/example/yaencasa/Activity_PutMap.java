package com.example.yaencasa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.yaencasa.Auxiliary.Constants;

import java.util.Objects;

public class Activity_PutMap extends AppCompatActivity {

    private String latitude = "no";
    private String longitude = "no";

    private ProgressDialog progressDialog;

    private String url = Constants.PUTMAP;

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_mapa);
        try {

            //Toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.ASM_toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });

            //Map
            progressDialog = ProgressDialog.show(this, getString(R.string.reload_map), getString(R.string.por_favor_espere), false, false);
            webView = (WebView) findViewById(R.id.ASM_WV);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressDialog.dismiss();
                }

                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    progressDialog.dismiss();
                    showAlertDialogNoInternet();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public void Map_Click_AddLocation(View v) {
        if (webView.getUrl().contains(",") && webView.getUrl().contains("@")) {
            coordinate();
            Intent intent = new Intent();
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.any_location_selected_jet), Toast.LENGTH_LONG).show();
        }

    }

    private void coordinate() {
        String total;
        int arroba = 0;
        int coma1 = 0;
        int coma2 = 0;

        total = webView.getUrl();

        while (total.charAt(arroba) != '@') {
            arroba++;
        }

        coma1 = arroba;

        while (total.charAt(coma1) != ',') {
            coma1++;
        }

        coma2 = coma1 + 1;

        while (total.charAt(coma2) != ',') {
            coma2++;
        }

        latitude = total.substring(arroba + 1, coma1);
        longitude = total.substring(coma1 + 1, coma2);
    }



    public void showAlertDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.revise_conx);
        builder.setPositiveButton(R.string.Reintentar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                webView.loadUrl(url);
            }
        });
        builder.create().show();
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }

}
