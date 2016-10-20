package com.codepath.drnick.sifter.models;

import android.text.TextUtils;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nick on 10/19/16.
 */

@Parcel
public class SearchFilters {

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setBeginDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        beginDate = calendar.getTime();
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
        return df.format(beginDate);
    }

    public String getBeginDateForUserDisplay() {
        if (beginDate == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return month + "/" + day + "/" + year;
    }

    Date beginDate;
    String sort;
    ArrayList<String> newsDesk;

    public SearchFilters() {
        beginDate = null;
        sort = "newest";
        newsDesk = new ArrayList<>();
    }

}
