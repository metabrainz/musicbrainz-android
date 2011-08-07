/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.ui.util.StringFormat;

import android.content.Context;
import android.content.res.Resources;

/**
 * Class to represent enough information about a release to differentiate
 * between similar releases (e.g. part of the same release group).
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ReleaseStub {

	private String releaseMbid;
	
	private String title;
	private String artistName;
	private String date;
	private int tracksNum;
	private String countryCode;
	private Collection<String> labels;
	private Collection<String> formats;
	
	public ReleaseStub() {
		labels = new LinkedList<String>();
		formats = new LinkedList<String>();
	}
	
	public String getReleaseMbid() {
		return releaseMbid;
	}
	
	public void setReleaseMbid(String releaseMbid) {
		this.releaseMbid = releaseMbid;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getArtistName() {
		return artistName;
	}
	
	public void setArtistName(String artist) {
		this.artistName = artist;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public int getTracksNum() {
		return tracksNum;
	}

	public void setTracksNum(int tracks) {
		this.tracksNum = tracks;
	}

	public Collection<String> getLabelList() {
		return labels;
	}
	
	public String getLabels() {
		return StringFormat.commaSeparate(labels);
	}

	public void addLabel(String label) {
		labels.add(label);
	}

	public Collection<String> getFormatList() {
		return formats;
	}
	
	public void addFormat(String format) {
		formats.add(format);
	}
	
	public String getFormattedFormats(Context context) {
		
		Map<String, Integer> formatCounts = getFormatCounts();
		Set<String> formats = formatCounts.keySet();
		
		if (formats.isEmpty()) {
			return "";
		}
		
		Resources res = context.getResources();
		StringBuilder sb = new StringBuilder();
		
		for (String format : formats) {
			
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
	
	private Map<String, Integer> getFormatCounts() {
		
		Map<String, Integer> formatCounts = new HashMap<String, Integer>();
		for (String format : formats) {
			Integer count = formatCounts.get(format);          
			formatCounts.put(format, (count == null) ? 1 : count + 1);
		}
		return formatCounts;
	}
	
}
