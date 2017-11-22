package com.example.tara.oughttado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tara on 20/11/2017.
 */

public class ToDoDatabase extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "todos";
    private static final String KEY_ID = "id";
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
                COL_2 + " TEXT )";

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

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, item);
        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        contentValues.put(COL_2, item);
        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

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
     * Deletes info from database
     */
    public void delete(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL_1 + " = '" + task + "'";

        Log.d(TAG, "delete: query: " + query);
        Log.d(TAG, "delete: Deleting " + task + " from database.");

        db.execSQL(query);
    }
}
