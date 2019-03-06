package org.metabrainz.mobile.api.data.search.entity;

import java.io.Serializable;

public class LifeSpan implements Serializable {
    private String begin;
    private String end;
    private boolean ended;

    @Override
    public String toString() {
        return "LifeSpan{" +
                "begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", ended=" + ended +
                '}';
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getTimePeriod() {
        StringBuilder builder = new StringBuilder();
        if (begin != null && !begin.isEmpty()) builder.append(begin);
        if (begin != null && !begin.isEmpty() && end != null && !end.isEmpty()) builder.append("-");
        if (end != null && !end.isEmpty()) builder.append(end);
        return builder.toString();
    }
}
