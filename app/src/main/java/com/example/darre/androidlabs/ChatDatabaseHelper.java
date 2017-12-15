package com.example.darre.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.example.darre.androidlabs.ChatWindow.ACTIVITY_NAME;


public class ChatDatabaseHelper extends SQLiteOpenHelper {


    public final static String name = "MyTable";
    public final static String DATABASE_NAME = "chatContent";
    public final static int VERSION_NUM = 3;
    public final static String KEY_ID = "ID";
    public final static String KEY_MESSAGE = "MESSAGE";




        public ChatDatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE_MSG = "CREATE TABLE " + name + "("
                    + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + KEY_MESSAGE + " TEXT )";
            db.execSQL(CREATE_TABLE_MSG);
            Log.i(ACTIVITY_NAME, "Calling onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ name); //delete what was there previously
            onCreate(db);
            Log.i("ChatDatabaseHelper", "Calling onCreate");
            Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
        }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + name); //delete what was there previously
        onCreate(db);
        //  Log.i("ChatDatabaseHelper", "Calling onCreate");
        Log.i("ChatDatabaseHelper", "Calling onDowngrade, newVersion=" + newVersion + "oldVersion=" + oldVersion);

    }

}





