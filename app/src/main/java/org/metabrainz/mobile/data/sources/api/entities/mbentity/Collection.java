package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

public class Collection {

    @SerializedName("id")
    private
    String mbid;
    private String type;
    private String editor;
    private String name;
    @SerializedName("entity-type")
    private String entityType;
    private int count;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "mbid='" + mbid + '\'' +
                ", type='" + type + '\'' +
                ", editor='" + editor + '\'' +
                ", name='" + name + '\'' +
                ", entityType='" + entityType + '\'' +
                ", count=" + count +
                '}';
    }
}
