package com.example.hw252bgrecco;

import android.provider.BaseColumns;

public class Tasks implements BaseColumns {

	public static final String DATABASE_NAME = "Tasks";
	
    public static final class Task {
    	
        public static final String TABLE_NAME = "task";

        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";

        public static final String[] PROJECTION = new String[] {
        /* 0 */ Tasks.Task.ID,
        /* 1 */ Tasks.Task.NAME};
        

    }
    
 }