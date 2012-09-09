package org.musicbrainz.mobile.string;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.musicbrainz.mobile.R;

import android.content.Context;
import android.content.res.Resources;

/**
 * Static methods to map strings returned by webservice to values in strings.xml
 */
public class StringMapper {

    public static String mapRGTypeString(Context c, String type) {

        Resources res = c.getResources();
        
        if (type == null) {
            return res.getString(R.string.rt_unknown);
        }
      
        if (type.equalsIgnoreCase("album"))
            return res.getString(R.string.rt_album);
        else if (type.equalsIgnoreCase("single"))
            return res.getString(R.string.rt_single);
        else if (type.equalsIgnoreCase("ep"))
            return res.getString(R.string.rt_ep);
        else if (type.equalsIgnoreCase("compilation"))
            return res.getString(R.string.rt_compilation);
        else if (type.equalsIgnoreCase("non-album tracks"))
            return res.getString(R.string.rt_nat);
        else if (type.equalsIgnoreCase("soundtrack"))
            return res.getString(R.string.rt_soundtrack);
        else if (type.equalsIgnoreCase("spokenword"))
            return res.getString(R.string.rt_spokenword);
        else if (type.equalsIgnoreCase("interview"))
            return res.getString(R.string.rt_interview);
        else if (type.equalsIgnoreCase("audiobook"))
            return res.getString(R.string.rt_audiobook);
        else if (type.equalsIgnoreCase("live"))
            return res.getString(R.string.rt_live);
        else if (type.equalsIgnoreCase("remix"))
            return res.getString(R.string.rt_remix);
        else if (type.equalsIgnoreCase("other"))
            return res.getString(R.string.rt_other);
        else
            return res.getString(R.string.rt_unknown);
    }

    public static String buildReleaseFormatsString(Context c, Collection<String> formats) {

        Map<String, Integer> formatCounts = getFormatCounts(formats);
        Set<String> formatKeys = formatCounts.keySet();

        if (formatKeys.isEmpty()) {
            return "";
        }

        Resources res = c.getResources();
        StringBuilder sb = new StringBuilder();

        for (String format : formatKeys) {

            Integer number = formatCounts.get(format);
            if (number > 1) {
                sb.append(number + "x");
            }

            if (format.equals("cd")) {
                sb.append(res.getString(R.string.fm_cd));
            } else if (format.equals("vinyl")) {
                sb.append(res.getString(R.string.fm_vinyl));
            } else if (format.equals("cassette")) {
                sb.append(res.getString(R.string.fm_cassette));
            } else if (format.equals("dvd")) {
                sb.append(res.getString(R.string.fm_dvd));
            } else if (format.equals("digital media")) {
                sb.append(res.getString(R.string.fm_dm));
            } else if (format.equals("sacd")) {
                sb.append(res.getString(R.string.fm_sacd));
            } else if (format.equals("dualdisc")) {
                sb.append(res.getString(R.string.fm_dd));
            } else if (format.equals("laserdisc")) {
                sb.append(res.getString(R.string.fm_ld));
            } else if (format.equals("minidisc")) {
                sb.append(res.getString(R.string.fm_md));
            } else if (format.equals("cartridge")) {
                sb.append(res.getString(R.string.fm_cartridge));
            } else if (format.equals("reel-to-reel")) {
                sb.append(res.getString(R.string.fm_rtr));
            } else if (format.equals("dat")) {
                sb.append(res.getString(R.string.fm_dat));
            } else if (format.equals("other")) {
                sb.append(res.getString(R.string.fm_other));
            } else if (format.equals("wax cylinder")) {
                sb.append(res.getString(R.string.fm_wax));
            } else if (format.equals("piano roll")) {
                sb.append(res.getString(R.string.fm_pr));
            } else if (format.equals("digital compact cassette")) {
                sb.append(res.getString(R.string.fm_dcc));
            } else if (format.equals("vhs")) {
                sb.append(res.getString(R.string.fm_vhs));
            } else if (format.equals("video-cd")) {
                sb.append(res.getString(R.string.fm_vcd));
            } else if (format.equals("super video-cd")) {
                sb.append(res.getString(R.string.fm_svcd));
            } else if (format.equals("betamax")) {
                sb.append(res.getString(R.string.fm_bm));
            } else if (format.equals("hd compatible digital")) {
                sb.append(res.getString(R.string.fm_hdcd));
            } else if (format.equals("usb flash drive")) {
                sb.append(res.getString(R.string.fm_usb));
            } else if (format.equals("slotmusic")) {
                sb.append(res.getString(R.string.fm_sm));
            } else if (format.equals("universal media disc")) {
                sb.append(res.getString(R.string.fm_umd));
            } else if (format.equals("hd-dvd")) {
                sb.append(res.getString(R.string.fm_hddvd));
            } else if (format.equals("dvd-audio")) {
                sb.append(res.getString(R.string.fm_dvda));
            } else if (format.equals("dvd-video")) {
                sb.append(res.getString(R.string.fm_dvdv));
            } else if (format.equals("blu-ray")) {
                sb.append(res.getString(R.string.fm_br));
            } else {
                sb.append(format);
            }
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private static Map<String, Integer> getFormatCounts(Collection<String> formats) {

        Map<String, Integer> formatCounts = new HashMap<String, Integer>();
        for (String format : formats) {
            Integer count = formatCounts.get(format);
            formatCounts.put(format, (count == null) ? 1 : count + 1);
        }
        return formatCounts;
    }

}
