package org.musicbrainz.android.api.data;

import java.util.Collections;
import java.util.LinkedList;

public class UserCollection extends UserCollectionInfo {
	
	private LinkedList<ReleaseInfo> releases = new LinkedList<ReleaseInfo>();

	public LinkedList<ReleaseInfo> getReleases() {
	    Collections.sort(releases);
		return releases;
	}

	public void setReleases(LinkedList<ReleaseInfo> releases) {
		this.releases = releases;
	}
	
	public void addRelease(ReleaseInfo release) {
	    releases.add(release);
	}

}
