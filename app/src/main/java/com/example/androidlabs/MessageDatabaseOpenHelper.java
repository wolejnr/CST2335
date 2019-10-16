package com.example.androidlabs;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MessageDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ChatDB";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "Chats";
    public static final String COL_ID = "_id";
    public static final String COL_CHAT = "CHAT";
    public static final String COL_CHAT_TYPE = "CHAT_TYPE";

    public MessageDatabaseOpenHelper(Activity ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "( "
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CHAT + " TEXT, " + COL_CHAT_TYPE + " INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i("Database Upgrade", "Old Version:" + oldVersion + " newVersion:"+newVersion);

        // Delete the old table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create a new table
        onCreate(sqLiteDatabase);
    }

    public void onDownGrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old Version:" + oldVersion + " newVersion:"+newVersion);

        // Delete the old table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create a new table
        onCreate(sqLiteDatabase);
    }
}
