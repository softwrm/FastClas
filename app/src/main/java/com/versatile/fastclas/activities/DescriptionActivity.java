package com.versatile.fastclas.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.shockwave.pdfium.PdfDocument;
import com.versatile.fastclas.adapters.SliderImagesAdapter;
import com.versatile.fastclas.utils.Utility;
import com.otelpt.fastclas.R;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener, OnLoadCompleteListener {

    ViewPager viewpagerImages;
    ArrayList<String> imagesArrayList = new ArrayList<>();
    String description="", image_one, image_two, image_three, image_four, image_five;
    ImageView imgBack;
    TextView txtToolbar;
    TextView txtDescription;
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName="";
    WebView webView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        pdfView=(PDFView)findViewById(R.id.pdfView);
        webView=(WebView)findViewById(R.id.webview);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);


        Intent intent = getIntent();
        description = intent.getStringExtra("description");
        Log.i("description",description);


        image_one = intent.getStringExtra("one");
        image_two = intent.getStringExtra("two");
        image_three = intent.getStringExtra("three");
        image_four = intent.getStringExtra("four");
        image_five = intent.getStringExtra("five");

        if (!Utility.isValueNullOrEmpty(image_one)) {
            imagesArrayList.add(image_one);
        }
        if (!Utility.isValueNullOrEmpty(image_two)) {
            imagesArrayList.add(image_two);
        }
        if (!Utility.isValueNullOrEmpty(image_three)) {
            imagesArrayList.add(image_three);
        }
        if (!Utility.isValueNullOrEmpty(image_four)) {
            imagesArrayList.add(image_three);
        }
        if (!Utility.isValueNullOrEmpty(image_five)) {
            imagesArrayList.add(image_five);
        }

        initUI(progressDialog);

    }

    private void initUI(final ProgressDialog progressDialog) {
        viewpagerImages = findViewById(R.id.viewpagerImages);
        imgBack = findViewById(R.id.imgBack);
        txtToolbar = findViewById(R.id.txtToolbar);
        txtDescription = findViewById(R.id.txtDescription);

        txtDescription.setText(description);
        txtToolbar.setText("Notes");
        imgBack.setOnClickListener(this);

        if (imagesArrayList.size() >= 1) {
            viewpagerImages.setVisibility(View.VISIBLE);
            SliderImagesAdapter mSliderImagesAdapter = new SliderImagesAdapter(this, imagesArrayList);
            viewpagerImages.setAdapter(mSliderImagesAdapter);
        } else {
            viewpagerImages.setVisibility(View.GONE);
        }

       // displayPDF(description);
        callWebView(progressDialog);
       // new RetrivePdfStream().execute("https://docs.google.com/gview?embedded=true&url="+description);


    }

    private void displayPDF(String description) {
        pdfFileName =description;
        pdfView.fromAsset("https://docs.google.com/gview?embedded=true&url="+description)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("TAG", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
    class  RetrivePdfStream extends AsyncTask<String,Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }

            }
            catch(Exception e){

            }


            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.fromStream(inputStream);
        }
    }
    public void callWebView(final ProgressDialog progressDialog){
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+description);


    }
    private class MyBrowser extends WebViewClient {

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent ke) {

            Log.e("Unhandled Key Event",ke.toString());


        }

        @Override
        public void onPageFinished(WebView view, String url) {


            super.onPageFinished(view, url);
            webView.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.display='none'; })()");
        }

    }
}
