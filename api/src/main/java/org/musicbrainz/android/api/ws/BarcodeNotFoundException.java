/*
 * Copyright (C) 2011 Jamie McDonald
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

package org.musicbrainz.android.api.ws;

public class BarcodeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Barcode not found in the MusicBrainz database: ";
	
	private String barcode;
	
	public BarcodeNotFoundException() {
		barcode = "unknown";
	}
	
	public BarcodeNotFoundException(String barcode) {
		this.barcode = barcode;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	@Override
	public String getMessage() {
		return MESSAGE + barcode;
	}

}
