package com.ram.email_box;

import com.ram.email_box.LoginActivity;
import com.ram.email_box.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
        	switch (item.getItemId())
    		{
    		case R.id.gmail:
    			Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
    			startActivity(intent);
    			return true;
    		case R.id.item1:
    			Intent intent1 = new Intent(getApplicationContext() , LoginActivity.class);
    			startActivity(intent1);
    			return true;
    		case R.id.item2:
    			Intent intent2 = new Intent(getApplicationContext() , LoginActivity.class);
    			startActivity(intent2);
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    			}
        }


  
}
