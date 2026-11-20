/*
 * Copyleft © 2022, 2026 Dmitry Tretyakov (aka. Tinelix)
 *
 * This file is part of Tinelix IRC Client for Android.
 *
 * Tinelix IRC Client is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see https://www.gnu.org/licenses/.
 *
 * Source code: https://github.com/tinelix/irc-client-legacy
 */

package dev.tinelix.irc.android.legacy.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import dev.tinelix.irc.android.legacy.Global;

public class AESCipher {

    /**
     * Referenced from https://github.com/crazygit/AES-Encryption-Demo
    **/

    private byte[] mDigest;

    private AESCipher(byte[] digest) {
        mDigest = digest;
    }

    public static AESCipher createInstance(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            return new AESCipher(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encrypt(String plainText) throws
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException,      IllegalBlockSizeException,
            BadPaddingException,      InvalidAlgorithmParameterException {

        byte[] plainBytes = plainText.getBytes();
        byte[] ivBytes = new byte[16];
        byte[] inputBytes = new byte[ivBytes.length + plainBytes.length];

        SecretKeySpec keySpec = new SecretKeySpec(mDigest, "AES");
        SecureRandom random = new SecureRandom();
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        random.nextBytes(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        System.arraycopy(
                ivBytes, 0, inputBytes, 0, ivBytes.length
        );
        System.arraycopy(
                plainBytes, 0, inputBytes, ivBytes.length, plainBytes.length
        );

        return Global.bytesToHex(cipher.doFinal(inputBytes));
    }

    public String decrypt(String cipherText) throws
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException,      IllegalBlockSizeException,
            BadPaddingException,      InvalidAlgorithmParameterException {

        byte[] cipherBytes = Global.hexToBytes(cipherText);
        byte[] ivBytes = new byte[16];
        byte[] inputBytes = new byte[cipherBytes.length - 16];

        System.arraycopy(cipherBytes, 0, ivBytes, 0, 16);
        System.arraycopy(cipherBytes, 16, inputBytes, 0, cipherBytes.length - 16);

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec keySpec = new SecretKeySpec(mDigest, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return new String(cipher.doFinal(inputBytes));
    }

}
