package com.example.hw252bgrecco;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
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
        mDatabaseHelper = new TasksSQLiteOpenHelper(this);
       
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dips = metrics.densityDpi;
        int pixels = (int)(dips * metrics.density);
        
        View fragmentContainer = this.findViewById(R.id.fragmentContainer);
        mDualPane = fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE;
        
        
		// Get the action bar
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		
        FragmentManager fragmentManager = getFragmentManager();
        mListFragment = (ListFragment) fragmentManager.findFragmentById(R.id.listFragment);
        mInfoFragment = new InfoFragment();
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

        mListFragment.getListView().setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		
				showInfo(position);
			}

	    });
	    
	    mListFragment.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
				mSelectedItem = position;
				view.setSelected(true);
				return true;
			}	    
		});  
	    

 
 
		
	}
	
	   /** Show either a fragment or activity in the selected color */
    void showInfo(int index) {
    	
		Cursor cursor = (Cursor)customAdapter.getItem(index); 
		String infoString = "This is Task " + cursor.getString(0);
    	
        if (mDualPane) {
            
            // This was the old way, when not using Tabs
            FragmentTransaction ft = getFragmentManager().beginTransaction();            
            ft.replace(R.id.fragmentContainer, mInfoFragment);
            ft.commit();
            getFragmentManager().executePendingTransactions();    
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

		switch (item.getItemId()) {
		case R.id.addtask:
			mDatabaseHelper.addTask("Task : ");
            customAdapter.changeCursor(mDatabaseHelper.getAllTasks());
			break;
		case R.id.deletetask:
			if ( mSelectedItem != -1){
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
