package com.example.hw252bgrecco;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.info_layout);

        // Turn off animations
        getWindow().setWindowAnimations(android.R.style.Animation);
        
        // Enable the up affordance
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Bundle b = getIntent().getExtras();
        String taskIdString = b.getString("info");
        
        TextView tv = (TextView)this.findViewById(R.id.infoTextView);
        if (tv != null){
        	tv.setText(taskIdString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        // Handle the up affordance
        if (item.getItemId() == android.R.id.home)
            finish();
        
        return super.onOptionsItemSelected(item);
    }

}
