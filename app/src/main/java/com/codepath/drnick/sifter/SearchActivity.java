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
import com.codepath.drnick.sifter.models.SearchFilters;
import com.codepath.drnick.sifter.network.FetchArticlesCallback;
import com.codepath.drnick.sifter.network.FetchArticlesRequest;
import com.codepath.drnick.sifter.network.NYTRestClient;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.etQuery) EditText etQuery;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.gvResults) GridView gvResults;

    ArrayList<Article> articleList;
    SearchFilters searchFilters;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        searchFilters = new SearchFilters();
        articleList = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articleList);
        gvResults.setAdapter(adapter);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchAndAppendArticles(page);
                return true;
            }
        });
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
        articleList.clear();
        adapter.notifyDataSetChanged();
        fetchAndAppendArticles(0);
    }

    public void fetchAndAppendArticles(int page) {
        String query = etQuery.getText().toString();

        FetchArticlesRequest request = new FetchArticlesRequest();
        request.setQuery(query);
        request.setPage(page);
        request.setSearchFilters(searchFilters);

        Log.d("DEBUG","Query: "+query+" page:"+page);

        NYTRestClient.fetchArticles(request, new FetchArticlesCallback() {
            @Override
            public void onSuccess(ArrayList<Article> articleList) {
                adapter.addAll(articleList);
            }

            @Override
            public void onError(Error error) {
                Log.d("ERROR","Problem fetching articles");
            }
        });
    }

    //
    // Activity Launchers
    //
    private final int LAUNCH_FILTER_ACITIVTY_REQUEST_CODE = 101;

    public void launchArticleActivity(Article article) {
        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
        i.putExtra("article", Parcels.wrap(article));
        startActivity(i);
    }

    public void launchFilterActivity() {
        Intent i = new Intent(getApplicationContext(), FilterActivity.class);
        i.putExtra("searchFilters", Parcels.wrap(searchFilters));
        startActivityForResult(i,LAUNCH_FILTER_ACITIVTY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ignore any results that were cancelled
        if (resultCode != RESULT_OK) {
            return;
        }

        // now handle any recognized results
        if (requestCode == LAUNCH_FILTER_ACITIVTY_REQUEST_CODE) {
            searchFilters = (SearchFilters) Parcels.unwrap(data.getParcelableExtra("searchFilters"));
            Toast.makeText(this, "Updated search settings", Toast.LENGTH_LONG).show();
        }
    }

}
