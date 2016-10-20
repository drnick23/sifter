package com.codepath.drnick.sifter.models;

import android.text.TextUtils;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by nick on 10/19/16.
 */

@Parcel
public class SearchFilters {

    public Calendar getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Calendar beginDate) {
        this.beginDate = beginDate;
    }

    public void setBeginDate(int year, int month, int dayOfMonth) {
        beginDate = Calendar.getInstance();
        beginDate.set(Calendar.MONTH, month);
        beginDate.set(Calendar.YEAR, year);
        beginDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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

    public String getNewsDeskForQuery() {
        String spacedQuotedItems = "\""+ TextUtils.join("\" \"", newsDesk)+"\"";
        return "news_desk:("+spacedQuotedItems+")";
    }

    public String getBeginDateForQuery() {
        java.text.DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return df.format(beginDate.getTime());
    }

    public String getBeginDateForUserDisplay() {
        if (beginDate == null) {
            return "";
        }
        int year = beginDate.get(Calendar.YEAR);
        int month = beginDate.get(Calendar.MONTH);
        int day = beginDate.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMMM d, ''yy");
        return format.format(beginDate.getTime());
        //return month + "/" + day + "/" + year;
    }

    Calendar beginDate;
    String sort;
    ArrayList<String> newsDesk;

    public SearchFilters() {
        beginDate = null;
        sort = "newest";
        newsDesk = new ArrayList<>();
    }

}
