package org.metabrainz.android.api.data;

public class UserCollectionInfo implements Comparable<UserCollectionInfo>{
	
	private String mbid;
	private String name;
	private String editor;
	private int count;
	
	public String getMbid() {
		return mbid;
	}
	
	public void setMbid(String mbid) {
		this.mbid = mbid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEditor() {
		return editor;
	}
	
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

    @Override
    public int compareTo(UserCollectionInfo another) {
        return getName().compareTo(another.getName());
    }

}
