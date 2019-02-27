package org.metabrainz.mobile.api.data.search.entity;

import com.google.gson.annotations.SerializedName;

public class LabelInfo {
    private Label label;
    @SerializedName("catalog-number")
    private String catalogNumber;

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }


}
