package com.milog.source.data;

import okhttp3.HttpUrl;

import java.util.Date;

/**
 * Created by Milo on 6/7/16.
 */
public class SourceTerm {

    public SourceGrade sourceGrade;
    public String name;

    public Date startDate;
    public Date endDate;

    public HttpUrl URL;

    public SourceTerm(SourceGrade sourceGrade, String name, Date startDate, Date endDate, HttpUrl URL) {
        this.sourceGrade = sourceGrade;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.URL = URL;
    }

    @Override
    public String toString() {
        return name;
    }
}
