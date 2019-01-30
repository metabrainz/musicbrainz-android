package org.metabrainz.mobile.api.util;

import java.util.StringTokenizer;

public class StringFormat {

    public static String initialCaps(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, " ", true);
        StringBuilder sb = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            token = String.format("%s%s", Character.toUpperCase(token.charAt(0)), token.substring(1));
            sb.append(token);
        }
        return sb.toString();
    }

    public static String formatDuration(int durationSeconds) {

        // TODO: Would be much cleaner using String.format().
        if (durationSeconds == 0) {
            return "";
        }

        int s = durationSeconds / 1000;
        int secs = s % 60;
        int mins = (s % 3600) / 60;
        int hrs = s / 3600;

        String mS = "" + mins;
        String sS = "" + secs;
        if (secs < 10)
            sS = "0" + secs;

        if (hrs == 0) {
            return mS + ':' + sS;
        } else {
            if (mins < 10)
                mS = "0" + mins;
            return hrs + ":" + mS + ":" + sS;
        }
    }

}
