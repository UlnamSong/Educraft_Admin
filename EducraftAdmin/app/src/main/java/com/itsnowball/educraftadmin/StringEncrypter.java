package com.itsnowball.educraftadmin;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ulnamsong on 2016. 11. 13..
 */

public class StringEncrypter {
    private Cipher rijndael;
    private SecretKeySpec key;
    private IvParameterSpec initalVector;

    public StringEncrypter(String key) throws Exception {
        this(key, GlobalConstants.AES_IVT);
    }

    /**
     * Creates a StringEncrypter instance.
     *
     * @param key
     *            A key string which is converted into UTF-8 and hashed by MD5. Null or an empty string is not allowed.
     * @param initialVector
     *            An initial vector string which is converted into UTF-8 and hashed by MD5. Null or an empty string is not allowed.
     * @throws Exception
     */
    public StringEncrypter(String key, String initialVector) throws Exception {
        if (key == null || key.equals(""))
            throw new Exception("The key can not be null or an empty string.");

        if (initialVector == null || initialVector.equals(""))
            throw new Exception("The initial vector can not be null or an empty string.");

        // Create a AES algorithm.
        this.rijndael = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Initialize an encryption key and an initial vector.
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        this.key = new SecretKeySpec(md5.digest(key.getBytes("UTF8")), "AES");
        this.initalVector = new IvParameterSpec(md5.digest(initialVector.getBytes("UTF8")));
    }

    /**
     * Encrypts a string.
     *
     * @param value
     *            A string to encrypt. It is converted into UTF-8 before being encrypted. Null is regarded as an empty string.
     * @return An encrypted string.
     * @throws Exception
     */
    public String encrypt(String value) throws Exception {
        if (value == null)
            value = "";

        // Initialize the cryptography algorithm.
        this.rijndael.init(Cipher.ENCRYPT_MODE, this.key, this.initalVector);

        // Get a UTF-8 byte array from a unicode string.
        byte[] utf8Value = value.getBytes("UTF8");

        // Encrypt the UTF-8 byte array.
        byte[] encryptedValue = this.rijndael.doFinal(utf8Value);

        // Return a base64 encoded string of the encrypted byte array.
        return Base64Encoder.encode(encryptedValue);
    }

    /**
     * Decrypts a string which is encrypted with the same key and initial vector.
     *
     * @param value
     *            A string to decrypt. It must be a string encrypted with the same key and initial vector. Null or an empty string is not allowed.
     * @return A decrypted string
     * @throws Exception
     */
    public String decrypt(String value) {
        try {
            if (value == null || value.equals(""))
                return "";

            // Initialize the cryptography algorithm.
            this.rijndael.init(Cipher.DECRYPT_MODE, this.key, this.initalVector);

            // Get an encrypted byte array from a base64 encoded string.
            byte[] encryptedValue = Base64Encoder.decode(value);

            // Decrypt the byte array.
            byte[] decryptedValue = this.rijndael.doFinal(encryptedValue);

            // Return a string converted from the UTF-8 byte array.
            return new String(decryptedValue, "UTF8");
        } catch (Exception ex) {
            //System.out.println(ex.getLocalizedMessage());
        }
        return value;
    }

    public String md5(String value) throws Exception {
        byte[] bpara = new byte[value.length()];
        byte[] rethash;
        int i;

        for (i = 0; i < value.length(); i++)
            bpara[i] = (byte) (value.charAt(i) & 0xff);

        try {
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            rethash = md5er.digest(bpara);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }

        StringBuffer r = new StringBuffer(32);
        for (i = 0; i < rethash.length; i++) {
            String x = Integer.toHexString(rethash[i] & 0xff).toUpperCase();
            if (x.length() < 2)
                r.append("0");
            r.append(x);
        }

        return r.toString();
    }
}


