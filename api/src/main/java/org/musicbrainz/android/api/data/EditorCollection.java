package org.musicbrainz.android.api.data;

import java.util.Collections;
import java.util.LinkedList;

public class EditorCollection extends EditorCollectionStub {
	
	private LinkedList<ReleaseStub> releases = new LinkedList<ReleaseStub>();

	public LinkedList<ReleaseStub> getReleases() {
	    Collections.sort(releases);
		return releases;
	}

	public void setReleases(LinkedList<ReleaseStub> releases) {
		this.releases = releases;
	}
	
	public void addRelease(ReleaseStub release) {
	    releases.add(release);
	}

}
