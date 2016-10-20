package com.codepath.drnick.sifter.network;

import android.util.Log;

import com.codepath.drnick.sifter.Article;
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

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void fetchArticles(FetchArticlesRequest request, final FetchArticlesCallback callback) {

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key","e6ec7693b8544295a54bc6fccdd3bc7c");
        params.put("q",request.getQuery());
        params.put("page",request.getPage());

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
