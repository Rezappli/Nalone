package com.nolonely.mobile.bdd.sql_lite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nolonely.mobile.objects.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nolonely.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql = "CREATE TABLE USERS (\n" +
                "  uid varchar(36) NOT NULL,\n" +
                "  name varchar(100) NOT NULL,\n" +
                "  pseudo varchar(100) NOT NULL,\n" +
                "  city varchar(100) NOT NULL,\n" +
                "  description varchar(255) DEFAULT NULL,\n" +
                "  mail varchar(100) NOT NULL,\n" +
                "  phone varchar(13) NOT NULL,\n" +
                "  number_events_attend int NOT NULL,\n" +
                "  number_events_create int NOT NULL,\n" +
                "  image_url varchar(255) DEFAULT NULL,\n" +
                "  latitude double NOT NULL,\n" +
                "  longitude double NOT NULL\n" +
                ")";

        db.execSQL(strSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSql = "drop table USERS";
        db.execSQL(strSql);
        this.onCreate(db);
    }

    public void insertUserConnect(User user) {
        String strSql = "INSERT INTO users (uid, name, pseudo, city, description, mail, phone, number_events_attend, number_events_create, image_url, latitude, longitude) VALUES ('"
                + user.getUid() + "', " + user.getName() + ", " + user.getPseudo() + ")";
        this.getWritableDatabase().execSQL(strSql);
    }

    public List<User> readMainUser() {
        List<User> users = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().query("Users",
                new String[]{"uid", "name", "pseudo", "city", "description", "mail", "phone", "number_events_attend", "number_events_create", "image_url", "latitude", "longitude"},
                null, null, null, null, null, "10");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getInt(7),
                    cursor.getInt(8), cursor.getString(9),
                    cursor.getDouble(10), cursor.getDouble(11));
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();

        return users;
    }
}
