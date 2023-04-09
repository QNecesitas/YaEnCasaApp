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

public class Activity_ShowMap extends AppCompatActivity {

    private String Latitud;
    private String Longitud;
    /* access modifiers changed from: private */
    private ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    private WebView webView;
    private String url = Constants.SHOWMAP;


    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        try {

            //Toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.APM_toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   finish();
                }
            });

            //Map
            Latitud = getIntent().getStringExtra("Latitud");
            Longitud = getIntent().getStringExtra("Longitud");
            progressDialog = ProgressDialog.show(this, getString(R.string.reload_map), getString(R.string.por_favor_espere), false, false);
            webView = (WebView) findViewById(R.id.APM_WV);
            webView.loadUrl(this.url + this.Latitud + "," + this.Longitud);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressDialog.dismiss();
                }

                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    showAlertDialogNoInternet();
                }
            });


        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    public void showAlertDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.revise_conx);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                webView.loadUrl(url);
            }
        });
        builder.create().show();
    }
}
