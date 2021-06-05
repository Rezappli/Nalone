package com.nolonely.mobile.bdd.sql_lite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nolonely.mobile.objects.User;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nolonely.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("DATABASE", "Created");
        String strSql = "CREATE TABLE users (\n" +
                "  uid TEXT NOT NULL,\n" +
                "  name TEXT NOT NULL,\n" +
                "  pseudo TEXT NOT NULL,\n" +
                "  city TEXT NOT NULL,\n" +
                "  description TEXT DEFAULT NULL,\n" +
                "  mail TEXT NOT NULL,\n" +
                "  phone TEXT NOT NULL,\n" +
                "  number_events_attend INTEGER NOT NULL,\n" +
                "  number_events_create INTEGER NOT NULL,\n" +
                "  image_url TEXT DEFAULT NULL,\n" +
                "  latitude DOUBLE NOT NULL,\n" +
                "  longitude DOUBLE NOT NULL\n" +
                ")";

        db.execSQL(strSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSql = "drop table users";
        db.execSQL(strSql);
        this.onCreate(db);
    }

    public void insertUserConnect(User user) {
        Log.w("DATABASE", "Inserted");

        String strSql = "INSERT INTO users (uid, name, pseudo, city, description, mail, phone, number_events_attend, number_events_create, image_url, latitude, longitude) VALUES " +
                "('" + user.getUid() + "', '" + user.getName() + "', '" + user.getPseudo() +
                "','" + user.getCity() + "','" + user.getDescription() + "','" + user.getMail()
                + "','" + user.getNumber() + "'," + user.getNumber_events_attend()
                + "," + user.getNumber_events_create() + ",'" + user.getImage_url() + "'," + user.getLatitude() + "," + user.getLongitude() + ")";
        this.getWritableDatabase().execSQL(strSql);
    }

    public User readMainUser() {
        User user = null;

        Cursor cursor = this.getReadableDatabase().rawQuery("select * from users", null);

        cursor.moveToLast();

        if (!cursor.isAfterLast()) {
            user = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getInt(7),
                    cursor.getInt(8), cursor.getString(9),
                    cursor.getDouble(10), cursor.getDouble(11));
            cursor.moveToNext();
        }

        cursor.close();


        return user;
    }
}
