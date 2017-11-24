package com.example.tara.oughttado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.DateFormat;
import android.util.Log;

/**
 * Created by Tara on 20/11/2017.
 */

public class ToDoDatabase extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "todos";
    private static final String KEY_ID = "_id";
    private static final String COL_1 = "title";
    private static final String COL_2 = "completed";
    private static final String[] COLUMNS = {KEY_ID, COL_1, COL_2};
    private static final String TAG = "ToDoDatabase";
    private static ToDoDatabase instance;

    public static ToDoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new ToDoDatabase(context);
        }
        return instance;
    }

    private ToDoDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        // called todos with columns title and completed
        // is autoincrement necessary?
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1 + " TEXT, " +
                COL_2 + " INTEGER )";

        // Create the todos table
        db.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop older todos table if applicable
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);

        // create fresh table
        this.onCreate(db);
    }

    public boolean addData(String item, Boolean value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, item);
        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        contentValues.put(COL_2, value);
        Log.d(TAG, "addData: Adding " + value + " to " + TABLE_NAME);

        // long var for if data is correctly or incorrectly inserted
        long result = db.insert(TABLE_NAME, null, contentValues);

        // result = -1 if incorrect
        return (result != -1);
    }

    /**
     * return all data from db
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Return ID that matches the name passed to it
     */
    public Cursor getItemID(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_NAME +
                " WHERE " + COL_1 + " = '" + task + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
     }

    /**
     * updated checkboxes
     */
    public void update(int newCheckbox, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE_NAME + " SET " + COL_2 +
//                " = '" + newCheckbox + "' WHERE " + KEY_ID + " = '" + key_id +
//                "'";
//        Log.d(TAG, "update: query: " + query);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, newCheckbox);
        db.update(TABLE_NAME, contentValues, KEY_ID + " = " + id, null);
        Log.d(TAG, "update: setting checkbox value to " + newCheckbox);
//        db.execSQL(query);
    }

    /**
     * Deletes info from database
     */
//    public void delete(String task) {
    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + KEY_ID + " = '" + id + "'";

//        db.delete(TABLE_NAME, id, );

        Log.d(TAG, "delete: query: " + query);
        Log.d(TAG, "delete: Deleting " + id + " from database.");
        db.execSQL(query);
    }
}
