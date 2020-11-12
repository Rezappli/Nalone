package com.example.nalone;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import static com.example.nalone.util.Constants.application;
import static com.example.nalone.util.Constants.formatD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Cache {

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
            Log.w("image","sauvegarde de : " + uid + " chemin :"+file_path);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
