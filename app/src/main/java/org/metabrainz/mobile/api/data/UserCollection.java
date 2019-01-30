package org.metabrainz.mobile.api.data;

import java.util.Collections;
import java.util.LinkedList;

public class UserCollection extends UserSearchResult {

    private LinkedList<ReleaseSearchResult> releases = new LinkedList<ReleaseSearchResult>();

    public LinkedList<ReleaseSearchResult> getReleases() {
        Collections.sort(releases);
        return releases;
    }

    public void setReleases(LinkedList<ReleaseSearchResult> releases) {
        this.releases = releases;
    }

    public void addRelease(ReleaseSearchResult release) {
        releases.add(release);
    }

}
