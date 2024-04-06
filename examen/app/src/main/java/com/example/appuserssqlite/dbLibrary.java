package com.example.appuserssqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class dbLibrary extends SQLiteOpenHelper {
    String tblBook = "Create Table book(id text, name text, value text, checkbox integer)";
    public dbLibrary(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblBook);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table book");
        db.execSQL(tblBook);
    }
}
