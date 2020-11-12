package com.example.nalone;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import static com.example.nalone.util.Constants.application;
import static com.example.nalone.util.Constants.formatD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static Uri getUriFromUid(String uid){
        String file_path = application.getCacheDir().getAbsolutePath();
        File dir = new File(file_path);
        File file = new File(dir, uid);

        return Uri.fromFile(file);
    }

    public static boolean fileExists(String uid){
        String file_path = application.getCacheDir().getAbsolutePath();
        File dir = new File(file_path);
        File file = new File(dir, uid);
        return file.exists();
    }





}
