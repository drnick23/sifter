package com.codepath.drnick.sifter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.drnick.sifter.activities.ArticleActivity;
import com.codepath.drnick.sifter.activities.FilterActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.etQuery) EditText etQuery;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.gvResults) GridView gvResults;

    ArrayList<Article> articleList;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        articleList = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articleList);
        gvResults.setAdapter(adapter);
    }

    @OnItemClick(R.id.gvResults)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("DEBUG","onItemClick");
        // launch new activity with article
        launchArticleActivity(articleList.get(position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.miFilter:
                // launch the filter settings activity
                Log.d("DEBUG","TODO: launch filter settings");
                launchFilterActivity();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btnSearch)
    public void onArticleSearch(View view) {
        Log.d("DEBUG","onArticleSearch");
        String query = etQuery.getText().toString();
        Toast.makeText(this, "search for "+query, Toast.LENGTH_LONG).show();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key","e6ec7693b8544295a54bc6fccdd3bc7c");
        params.put("q",query);
        params.put("page",0);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJSONResults = null;
                try {
                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                    //articleList.addAll(Article.fromJSONArray(articleJSONResults));
                    //adapter.notifyDataSetChanged();
                    adapter.addAll(Article.fromJSONArray(articleJSONResults));
                    Log.d("DEBUG", articleJSONResults.toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    // our activity launchers
    public void launchArticleActivity(Article article) {
        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
        i.putExtra("article", Parcels.wrap(article));
        startActivity(i);
    }

    public void launchFilterActivity() {
        Intent i = new Intent(getApplicationContext(), FilterActivity.class);
        startActivity(i);
    }
}
