package com.example.hw252bgrecco;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {
	
    private Context mContext;
	private ActionBar mActionBar;
	
    private TasksSQLiteOpenHelper mDatabaseHelper; 

	ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView mListViewTasks;
    
    private CustomCursorAdapter customAdapter;

    private boolean mDualPane = false;
    private ListFragment mListFragment;
    private InfoFragment mInfoFragment;
    
    int mTaskCounter = 1;
    int mNumTasks = 0;
    int mSelectedItem = -1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        mContext = this;     
        
        // create a database helper to get task data saved in our tasks database
        mDatabaseHelper = new TasksSQLiteOpenHelper(this);
      
        // check to see if we are going to show a split pane on the screen.  On pane for the task list
        // and one pane for the task description
        View fragmentContainer = this.findViewById(R.id.fragmentContainer);
        mDualPane = fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE;
        
        // we will always two panes (given the above is true) if we are in landscape mode.  But if we
        // are in portrait mode we only want to split the screen if it is a screen greater that
        // 960x720dp
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dpHeight = (int)(metrics.heightPixels / metrics.density);
        int dpWidth = (int)(metrics.widthPixels / metrics.density);
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ){
        	if ( dpHeight < 960 || dpWidth < 720 )
        		mDualPane = false;
        }
        		
		// Get the action bar
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		// create the fragments
        mInfoFragment = new InfoFragment();	
        FragmentManager fragmentManager = getFragmentManager();
        if ( mDualPane ){
        	mListFragment = (ListFragment) fragmentManager.findFragmentById(R.id.listFragmentDual);
        }
        else{
        	mListFragment = (ListFragment) fragmentManager.findFragmentById(R.id.listFragment);

        }
        if (mDualPane){
            FragmentTransaction ft = getFragmentManager().beginTransaction();            
            ft.add(R.id.fragmentContainer, mInfoFragment);
            ft.commit();
        }
        
        // Database query can be a time consuming task ..
        // so its safe to call database query in another thread
        // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new CustomCursorAdapter(MainActivity.this, mDatabaseHelper.getAllTasks(), 0);
                mListFragment.setListAdapter(customAdapter);
            }
        });        

        // Set OnClickListener so we know when the user wants to drill down for more info on a task
        mListFragment.getListView().setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		
				showInfo(position);
			}

	    });
	    
        // Set the OnLongItemClickListener so we know when the user wants to select an item to delete
	    mListFragment.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
				mSelectedItem = position;
				view.setSelected(true);
				return true;
			}	    
		});  
	}
	
	// Show either a fragment or activity with the selected task's info
    void showInfo(int index) {
    	
		Cursor cursor = (Cursor)customAdapter.getItem(index); 
		String infoString = "This is Task " + cursor.getString(0);
    	
        if (mDualPane) {
            mInfoFragment.setText(infoString);            
        }
        else {

            Intent intent = new Intent();
            intent.setClass(this, InfoActivity.class);
            intent.putExtra("info", infoString);
            startActivity(intent);
        }
    }	
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// add or delete task as selected from the ActionBar
		switch (item.getItemId()) {
		case R.id.addtask:
			mDatabaseHelper.addTask("Task : ");
            customAdapter.changeCursor(mDatabaseHelper.getAllTasks());
			break;
		case R.id.deletetask:
			// make sure we have a valid list selection!
			if ( mSelectedItem != -1){
				// after deleting the requested task, get a new cursor with the remaining tasks and
				// update the customAdapter.
				Cursor cursor = (Cursor)customAdapter.getItem(mSelectedItem); 
				String strID = cursor.getString(0);
				int nRows  = mDatabaseHelper.deleteTask(strID);
				if (nRows > 1){
					mSelectedItem = -1;
				}
				customAdapter.changeCursor(mDatabaseHelper.getAllTasks());
			}
			break;
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
