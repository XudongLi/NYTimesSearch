package com.lixudong.android.nytarticlesearch.models;

import org.parceler.Parcel;

import java.util.Map;

@Parcel
public class Filter {
    String date;
    String sortOrder;
    boolean arts;
    boolean fashion;
    boolean sports;

    public Filter() {}

    public Filter(String date, String sortOrder, Map<String, Boolean> newsDesk) {
        this.date = date;
        this.sortOrder = sortOrder;
        this.arts = newsDesk.get("Arts");
        this.fashion = newsDesk.get("Fashion & Style");
        this.sports = newsDesk.get("Sports");
    }

    public String getDate() {
        return date;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public boolean isArts() {
        return arts;
    }

    public boolean isFashion() {
        return fashion;
    }

    public boolean isSports() {
        return sports;
    }
}
