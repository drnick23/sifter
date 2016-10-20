package com.codepath.drnick.sifter.network;

import com.codepath.drnick.sifter.models.SearchFilters;

/**
 * Created by nick on 10/19/16.
 */

public class FetchArticlesRequest {
    public SearchFilters getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(SearchFilters searchFilters) {
        this.searchFilters = searchFilters;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    SearchFilters searchFilters;
    int page;
    String query;

    public FetchArticlesRequest() {

    }

}
