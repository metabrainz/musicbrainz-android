package org.metabrainz.mobile.util;

import org.jaudiotagger.tag.FieldKey;

public class MetadataChange {
    private FieldKey tagName;
    private String originalValue;
    private String newValue;
    private boolean discardChange;

    public MetadataChange(FieldKey tagName, String originalValue, String newValue) {
        this.tagName = tagName;
        this.originalValue = originalValue;
        this.newValue = newValue;
        this.discardChange = false;
    }

    public boolean isDiscardChange() {
        return discardChange;
    }

    public void setDiscardChange(boolean discardChange) {
        this.discardChange = discardChange;
    }

    public FieldKey getTagName() {
        return tagName;
    }

    public void setTagName(FieldKey tagName) {
        this.tagName = tagName;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public boolean isChanged() {
        if (originalValue != null && newValue != null)
            return !originalValue.trim().equalsIgnoreCase(newValue.trim());
        else return true;
    }
}
