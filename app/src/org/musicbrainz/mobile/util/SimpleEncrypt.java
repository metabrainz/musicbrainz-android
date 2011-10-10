/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * This class provides basic hashing to ensure that user passwords are obscured
 * on rooted devices.
 */
public class SimpleEncrypt {

    public static final String CHARSET = "UTF8";
    public static final String ALGORITHM = "DES";

    public static String encrypt(String secret, String plainText) {
        try {
            return crypto(Cipher.ENCRYPT_MODE, secret, plainText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String secret, String cipherText) {
        try {
            return crypto(Cipher.DECRYPT_MODE, secret, cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String crypto(int mode, String secret, String input) throws InvalidKeyException,
            UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        DESKeySpec keySpec = new DESKeySpec(secret.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey encryptKey = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, encryptKey);

        if (mode == Cipher.ENCRYPT_MODE) {
            byte[] plainText = input.getBytes(CHARSET);
            return Base64.encodeToString(cipher.doFinal(plainText), Base64.DEFAULT);

        } else if (mode == Cipher.DECRYPT_MODE) {
            byte[] cipherText = Base64.decode(input, Base64.DEFAULT);
            return new String(cipher.doFinal(cipherText), CHARSET);
        }
        return null;
    }
}
