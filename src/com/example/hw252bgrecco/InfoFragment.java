package com.example.hw252bgrecco;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {

	   String mTaskIdString = "";
	   TextView mTv;
	   
	   @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

 
	        // Inflate the layout for this fragment and set the text for task info
	        View v = inflater.inflate(R.layout.info_layout, container, false);
	        mTv = (TextView)v.findViewById(R.id.infoTextView);
	        if (mTv != null){
	        	mTv.setText(mTaskIdString);
	        }
	        return v;
	    }
	    
	    
	    @Override
		public void onResume() {
	        // make sure we have a valid text view instance and set the task info string	    	
			if (mTv == null){
				View view = this.getView();
		        if (view != null){	
		        	mTv = (TextView)view.findViewById(R.id.infoTextView);
		        }
			}	        	
	    	mTv.setText(mTaskIdString);
			super.onResume();
		}


		public void setText(String taskIdString) {        
	        // make sure we have a valid text view instance and set the task info string
			if (mTv == null){
				View view = this.getView();
		        if (view != null){	
		        	mTv = (TextView)view.findViewById(R.id.infoTextView);
		        }
			}
			mTaskIdString = taskIdString;
	    	mTv.setText(mTaskIdString);			
	    }


}
