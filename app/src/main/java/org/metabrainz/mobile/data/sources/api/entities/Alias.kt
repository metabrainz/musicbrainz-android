package org.metabrainz.mobile.data.sources.api.entities;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Alias {

    @SerializedName("sort-name")
    private String sortName;
    private String name;
    private String locale;
    private String type;
    private boolean primary;
    @SerializedName("begin-date")
    private String beginDate;
    @SerializedName("end-date")
    private String endDate;

    @NonNull
    @Override
    public String toString() {
        return "Alias{" +
                "sortName='" + sortName + '\'' +
                ", name='" + name + '\'' +
                ", locale='" + locale + '\'' +
                ", type='" + type + '\'' +
                ", primary=" + primary +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
