package com.example.notifications;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {
	
	String content = "I love my INDIA";

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void sendNotifications(View view){
   
    	
    	switch (view.getId()){
    	
    	case R.id.buttonBasic:
    		sendBasicNotification();
    	break;
    	
    	case R.id.buttonBigPicture:
    	break;
    	
    	case R.id.buttonInbox:
    	break;
    }
    }
    	
    private void sendBasicNotification() {
    		
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    	builder.setAutoCancel(true);
    	builder.setContentTitle("Basic Notifications");
    	builder.setContentText(content);
    	builder.setSmallIcon(R.drawable.ic_launcher);
    	MediaPlayer mySong = MediaPlayer.create(MainActivity.this, R.raw.frooty);
    	mySong.start();
    	
    	Notification notification = builder.build();
    	NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
    	manager.notify(8, notification);
  }
    
    
}
