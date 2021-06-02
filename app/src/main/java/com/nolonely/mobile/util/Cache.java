package com.nolonely.mobile.util;

import android.net.Uri;

import static com.nolonely.mobile.util.Constants.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Cache {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy hh:mm:ss");

    public static void saveUriFile(String uid, Uri uri) {
        try{
            String file_path = application.getCacheDir().getAbsolutePath();
            File dir = new File(file_path);
            if(!dir.exists())
                dir.mkdirs();
            File file = new File(dir, uid);
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(uri.toString().getBytes());
            fOut.flush();
            fOut.close();

            File file_date = new File(dir, uid+"_date");
            FileOutputStream fOut_date = new FileOutputStream(file_date);
            fOut_date.write(sdf.format(new Date(System.currentTimeMillis())).getBytes());
            fOut_date.flush();
            fOut_date.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getImageDate(String uid){
        String file_path = application.getCacheDir().getAbsolutePath();
        File dir = new File(file_path);
        File file = new File(dir, uid+"_date");

        String data = "";
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
            return sdf.format(new Date(buffer.getLong()));
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;

    }

    public static Uri getUriFromUid(String uid) {
        String file_path = application.getCacheDir().getAbsolutePath();
        File dir = new File(file_path);
        File file = new File(dir, uid);

        String data = "";
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        byte [] buf = data.getBytes();
        String s = null;
        try {
            s = new String(buf, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse(s);
        return uri;
    }

    public static boolean fileExists(String uid){
        String file_path = application.getCacheDir().getAbsolutePath();
        File dir = new File(file_path);
        File file = new File(dir, uid);
        return file.exists();
    }
}
