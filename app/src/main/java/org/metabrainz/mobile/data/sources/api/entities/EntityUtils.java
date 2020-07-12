package org.metabrainz.mobile.data.sources.api.entities;

import java.util.Iterator;
import java.util.List;


public class EntityUtils {

    public static String getDisplayArtist(List<ArtistCredit> artistCredits) {
        StringBuilder builder = new StringBuilder();
        Iterator<ArtistCredit> iterator = artistCredits.iterator();
        while (iterator.hasNext()) {
            ArtistCredit credit = iterator.next();
            if (credit != null && credit.getName() != null &&
                    !credit.getName().equalsIgnoreCase("null")) {
                builder.append(credit.getName());
                if (iterator.hasNext())
                    builder.append(credit.getJoinphrase());
            }
        }
        return builder.toString();
    }

}
