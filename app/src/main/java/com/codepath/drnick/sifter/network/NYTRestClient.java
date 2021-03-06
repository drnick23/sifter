package com.codepath.drnick.sifter.network;

import android.util.Log;

import com.codepath.drnick.sifter.models.Article;
import com.codepath.drnick.sifter.models.SearchFilters;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nick on 10/19/16.
 */

public class NYTRestClient {

    private static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final String API_KEY = "a2a8ed7806b044ae9d70c40e052528ef";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void fetchArticles(FetchArticlesRequest request, final FetchArticlesCallback callback) {

        if (!NetworkUtils.isOnline()) {
            callback.onError(new Error("Not connected to internet"));
            return;
        }

        String url = BASE_URL+"articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key",API_KEY);
        params.put("q",request.getQuery());
        params.put("page",request.getPage());

        SearchFilters filter = request.getSearchFilters();
        if (filter != null) {
            if (filter.getSort() != null) {
                params.put("sort", filter.getSort());
            }
            if (filter.getNewsDesk() != null && filter.getNewsDesk().size() > 0) {
                params.put("fq", filter.getNewsDeskForQuery());
            }
            if (filter.getBeginDate() != null) {
                params.put("begin_date", filter.getBeginDateForQuery());
            }
        }


        Log.d("DEBUG", "params: "+params.toString());

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJSONResults = null;
                try {
                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                    Log.d("DEBUG", articleJSONResults.toString());
                    callback.onSuccess(Article.fromJSONArray(articleJSONResults));
                }
                catch (JSONException e){
                    e.printStackTrace();
                    callback.onError(new java.lang.Error(e.getMessage()));
                }
            }
        });


    }

}
