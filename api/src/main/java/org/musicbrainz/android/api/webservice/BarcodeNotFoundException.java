package org.musicbrainz.android.api.webservice;

public class BarcodeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 118690723271061260L;
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
