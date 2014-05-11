package com.example.hw252bgrecco;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
	
	 private LayoutInflater mInflater;
	 
	 public CustomCursorAdapter(Context context, Cursor c, int flags) {
		 super(context, c, flags);
		 mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	 
	 @Override
	 public void bindView(View view, Context context, Cursor cursor) {
	  TextView content = (TextView) view.findViewById(R.id.task_name);
	  String viewText = cursor.getString(1);
	  viewText += cursor.getString(0);
	  content.setText(viewText);
	 
	 }
	 
	 @Override
	 public View newView(Context context, Cursor cursor, ViewGroup parent) {
	  return mInflater.inflate(R.layout.single_row_item, parent, false);
	 }
}
