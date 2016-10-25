package com.lixudong.android.nytarticlesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.lixudong.android.nytarticlesearch.R;
import com.lixudong.android.nytarticlesearch.adapters.ArticleArrayAdapter;
import com.lixudong.android.nytarticlesearch.models.Article;
import com.lixudong.android.nytarticlesearch.models.Filter;
import com.lixudong.android.nytarticlesearch.utils.ConnectionChecker;
import com.lixudong.android.nytarticlesearch.utils.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    GridView gvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;
    Filter searchFilter;
    String queryRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
    }

    public void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gvResults = (GridView) findViewById(R.id.gvResult);
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(articleArrayAdapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", Parcels.wrap(article));
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                ConnectionChecker connectionChecker = new ConnectionChecker();
                if(connectionChecker.isOnline()) {
                    getArticles(queryRecord, page);
                } else {
                    Toast.makeText(getApplicationContext(), "internet is not avialable", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                articleArrayAdapter.clear();
                // Persist the query for endless scrolling
                queryRecord = query;
                ConnectionChecker connectionChecker = new ConnectionChecker();
                if(connectionChecker.isOnline()) {
                    getArticles(query, 0);
                } else {
                    Toast.makeText(getApplicationContext(), "internet is not avialable", Toast.LENGTH_SHORT).show();
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit){
            Intent i = new Intent(getApplicationContext(), EditFilterActivity.class);
            startActivityForResult(i, 20);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 20) {
            searchFilter = (Filter) Parcels.unwrap(data.getParcelableExtra("filter"));
        }
    }

    public void getArticles(String query, int page) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams requestParams = new RequestParams();
        requestParams.put("api-key", "7494f6c4d819449789f4d52bc20cada9");
        requestParams.put("page", page);
        requestParams.put("q", query);
        applySearchFilter(requestParams);
        Log.d("DEBUG", requestParams.toString());

        client.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articleArrayAdapter.addAll(Article.fromJSONArray(articleJsonResults));
                    Log.d("DEBUG", articleJsonResults.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void applySearchFilter(RequestParams requestParams) {
        if(searchFilter == null) {
            return;
        }
        if(searchFilter.getDate() != null) {
            requestParams.put("begin_date", searchFilter.getDate());
        }
        if(searchFilter.getSortOrder() != null) {
            requestParams.put("sort", searchFilter.getSortOrder());
        }
        String newsDesk = "";
        if(searchFilter.isArts()) {
            newsDesk = newsDesk + "\\\"Arts\\\" ";
        }
        if(searchFilter.isFashion()) {
            newsDesk = newsDesk + "\\\"Fashion & Style\\\" ";
        }
        if(searchFilter.isSports()) {
            newsDesk = newsDesk + "\\\"Sports\\\"";
        }
        if(newsDesk != "") {
            requestParams.put("fq", "news_desk:(" + newsDesk + ")");
        }
    }
}
