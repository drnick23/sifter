package com.codepath.drnick.sifter.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by nick on 10/19/16.
 */

@Parcel
public class SearchFilters {

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public void setNewsDesk(ArrayList<String> newsDesk) {
        this.newsDesk = newsDesk;
    }

    public ArrayList<String> getNewsDesk() {
        return newsDesk;
    }

    String beginDate;
    String sort;
    ArrayList<String> newsDesk;

    public SearchFilters() {
        beginDate = "";
        sort = "oldest";
        newsDesk = new ArrayList<>();
    }

}
