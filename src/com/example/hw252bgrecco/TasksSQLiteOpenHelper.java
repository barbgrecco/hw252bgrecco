package com.example.hw252bgrecco;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = Tasks.DATABASE_NAME;
    private final static int DB_VERSION = 1;

    private final static String TABLE_NAME = Tasks.Task.TABLE_NAME;
    private final static String TABLE_ROW_ID = Tasks.Task.ID;
    private final static String TABLE_ROW_NAME = Tasks.Task.NAME;

    public TasksSQLiteOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

        
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQueryString = 
                        "CREATE TABLE " + 
                        TABLE_NAME + " (" + 
                        TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
                        TABLE_ROW_NAME + " TEXT" + ");";
        
        db.execSQL(createTableQueryString);
        /*
        ContentValues cv = new ContentValues();        
        cv.put(Tasks.Task.NAME, "Pro Android 4");
        cv.put(Tasks.Task.DESC, "1430239301");
        db.insert(TABLE_NAME, null, cv);
        cv.clear();
*/
    }

    @Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onDowngrade(db, oldVersion, newVersion);
	}


	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
	
	// Adding new task
    void addTask(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_NAME, name); // Task Name
 
        // Inserting Row
        long insertID = db.insert(TABLE_NAME, null, values);
        
        //updateTaskName(db, name, (int)insertID);
        db.close(); // Closing database connection
    }	
    
    private void updateTaskName(SQLiteDatabase db, String name, int id) {
 
    	name += String.valueOf(id);
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_NAME, name);

 
        // updating row
        db.update(TABLE_NAME, values, TABLE_ROW_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    
    public Cursor getAllTasks() {
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    
    // Deleting single task
    public int deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int numRows = db.delete(TABLE_NAME, TABLE_ROW_ID + " = ?", 
                new String[] { id });
        db.close();
        return numRows;
    }    

}
