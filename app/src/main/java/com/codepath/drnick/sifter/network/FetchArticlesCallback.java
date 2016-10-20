package com.codepath.drnick.sifter.network;

import com.codepath.drnick.sifter.Article;

import java.util.ArrayList;

/**
 * Created by nick on 10/19/16.
 */

public interface FetchArticlesCallback {
    void onSuccess(ArrayList<Article> articleList);
    void onError(Error error);
}
