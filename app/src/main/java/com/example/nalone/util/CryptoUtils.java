package com.example.nalone.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CryptoUtils {

    private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
    private static int CIPHER_KEY_LEN = 32; //256 bits


    /**
     * Encrypt data using AES Cipher (CBC) with 128 bit key
     *
     *
     * @param data - data to encrypt
     * @return encryptedData data in base64 encoding with iv attached at end after a :
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String data) {
        try {
            if (Constants.key.length() < CIPHER_KEY_LEN) {
                int numPad = CIPHER_KEY_LEN - Constants.key.length();

                for(int i = 0; i < numPad; i++){
                    Constants.key += "0"; //0 pad to len 16 bytes
                }

            } else if (Constants.key.length() > CIPHER_KEY_LEN) {
                Constants.key = Constants.key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
            }


            IvParameterSpec initVector = new IvParameterSpec(Constants.iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(Constants.key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector);

            byte[] encryptedData = cipher.doFinal((data.getBytes()));

            String base64_EncryptedData = Base64.getEncoder().encodeToString(encryptedData);
            String base64_IV = Base64.getEncoder().encodeToString(Constants.iv.getBytes("UTF-8"));

            return base64_EncryptedData + ":" + base64_IV;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param data - encrypted data with iv at the end separate by :
     * @return decrypted data string
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String data) {
        try {

            String[] parts = data.split(":");

            IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[1]));
            SecretKeySpec skeySpec = new SecretKeySpec(Constants.key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] decodedEncryptedData = Base64.getDecoder().decode(parts[0]);

            byte[] original = cipher.doFinal(decodedEncryptedData);

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
