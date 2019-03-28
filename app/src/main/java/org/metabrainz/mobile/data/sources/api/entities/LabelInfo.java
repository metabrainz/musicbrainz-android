package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;

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
