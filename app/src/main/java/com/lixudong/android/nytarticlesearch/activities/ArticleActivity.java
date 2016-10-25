package com.lixudong.android.nytarticlesearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lixudong.android.nytarticlesearch.models.Article;
import com.lixudong.android.nytarticlesearch.R;
import com.lixudong.android.nytarticlesearch.utils.ConnectionChecker;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        WebView webView = (WebView) findViewById(R.id.wvArticle);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ConnectionChecker connectionChecker = new ConnectionChecker();
                if(connectionChecker.isOnline()) {
                    view.loadUrl(url);
                } else {
                    Toast.makeText(getApplicationContext(), "internet is not avialable", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        webView.loadUrl(article.getWebUrl());
    }
}
