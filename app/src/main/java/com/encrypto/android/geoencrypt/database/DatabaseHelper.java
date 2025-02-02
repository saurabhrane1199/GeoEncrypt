package com.encrypto.android.geoencrypt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper  extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "VAULT_DATABASE";
    static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE VAULT_DATABASE "
                + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " Vault_Name TEXT NOT NULL, "
                + " KEY TEXT NOT NULL, "
                + " Algorithm TEXT NOT NULL) ";

        db.execSQL(sql);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS VAULT_DATABASE";
        db.execSQL(sql);

        onCreate(db);
    }

}
