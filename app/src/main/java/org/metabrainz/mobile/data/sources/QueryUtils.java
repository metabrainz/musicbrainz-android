package org.metabrainz.mobile.data.sources;

import java.util.HashMap;
import java.util.Map;

public class QueryUtils {

    public static String getQuery(HashMap<String, String> arguments) {
        StringBuilder queryBuilder = new StringBuilder();
        for (Map.Entry<String, String> argument : arguments.entrySet())
            if (argument.getKey() != null && !argument.getValue().isEmpty())
                queryBuilder.append("(").append(argument.getKey()).append(":")
                        .append(argument.getValue()).append(")");

        return queryBuilder.toString();

    }
}
