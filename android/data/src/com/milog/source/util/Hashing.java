package com.milog.source.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Milo on 6/5/16.
 */
public class Hashing {

    public static String hash(String pw, String context) {

        String pwFinal = "";

        try {

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(pw.getBytes(), 0, pw.length());
            byte[] base64MD5 = Base64.getEncoder().encode(m.digest());
            String base64String = new String(base64MD5, "ASCII");
            base64String = base64String.replace("=", "");

            byte[] macFinal = hmacMD5(base64String, context);
            pwFinal = hashToString(macFinal);

        }

        catch (NoSuchAlgorithmException e){
            System.err.println("No Such Algorithm MD5");
        }

        catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported Encoding ASCII");
        }

        return pwFinal;
    }

    public static String dbpwHash(String pw, String context) {

        byte[] hash = hmacMD5(pw.toLowerCase(), context);
        return hashToString(hash);
    }


    private static byte[] hmacMD5(String data, String keyString) {
        byte[] finalHash = null;

        try {
            SecretKeySpec key = new SecretKeySpec((keyString.getBytes()), "ASCII");
            Mac mac = Mac.getInstance("HMACMD5");
            mac.init(key);
            finalHash = mac.doFinal(data.getBytes("ASCII"));
        }

        catch (NoSuchAlgorithmException e){
            System.err.println("No Such Algorithm HMACMD5");
        }

        catch (InvalidKeyException e) {
            System.err.println("Invalid Key Exception " + keyString);
        }

        catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported Encoding ASCII");
        }

        return finalHash;
    }

    private static String hashToString(byte[] hash) {

        StringBuffer hashBuffer = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xFF & hash[i]);
            if (hex.length() == 1) hashBuffer.append('0');
            hashBuffer.append(hex);
        }

        return hashBuffer.toString();
    }
}

