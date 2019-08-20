package org.metabrainz.mobile.data.sources;

import android.util.Pair;

import java.util.List;

public class QueryUtils {

    public static String getQuery(List<Pair<String, String>> arguments) {
        StringBuilder queryBuilder = new StringBuilder();
        for (Pair<String, String> argument : arguments)
            if (argument.second != null && !argument.second.isEmpty())
                queryBuilder.append("(").append(argument.first).append(":")
                        .append(argument.second).append(")");

        return queryBuilder.toString();

    }
}
