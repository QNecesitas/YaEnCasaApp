package com.example.yaencasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Activity_AdView extends AppCompatActivity {

    private String url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_view);

        url = getIntent().getStringExtra("urlAd");
        //WebView
        webView = (WebView) findViewById(R.id.av_webview);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                showAlertDialogNoInternet();
            }
        });

    }

    public void showAlertDialogNoInternet() {
        //init alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_AdView.this);
        builder.setCancelable(true);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.Revise_su_conexion);
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Reintentar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(url);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.Salir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        //create the alert dialog and show it
        builder.create().show();
    }

}