package org.metabrainz.mobile.data.sources.api.entities

import java.io.Serializable

class LifeSpan : Serializable {
    var begin: String? = null
    var end: String? = null
    var isEnded = false
    override fun toString(): String {
        return "LifeSpan{" +
                "begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", ended=" + isEnded +
                '}'
    }

    val timePeriod: String
        get() {
            val builder = StringBuilder()
            if (begin != null && !begin!!.isEmpty()) builder.append(begin)
            if (begin != null && !begin!!.isEmpty() && end != null && !end!!.isEmpty()) builder.append("-")
            if (end != null && !end!!.isEmpty()) builder.append(end)
            return builder.toString()
        }
}