package com.ram.email_box;

import com.ram.email_box.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends Activity {

	private Mail mMail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		TextView t = (TextView) findViewById(R.id.user_name);

		Intent intent = getIntent();

		t.setText(intent.getStringExtra("EXTRA_EMAIL"));
		mMail = (Mail) intent.getParcelableExtra("EXTRA_MAIL_INSTANCE");

				
		findViewById(R.id.new_mail_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						newEmail();
					}
				});
		
	
		findViewById(R.id.show_emails_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						showEmails();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}
	
	 @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
     	switch (item.getItemId())
 		{
 		case R.id.showmails:
 			showEmails();
 			return true;
 		case R.id.newmail:
 			newEmail();
 			return true;
 		
 		default:
 			return super.onOptionsItemSelected(item);
 			}
     }
	/**
	 * Sets up data for newEmailActivity and starts it	
	 */
	private void newEmail(){
		
		Intent intent = new Intent(getApplicationContext() , NewEmailActivity.class);

		intent.putExtra("EXTRA_MAIL_INSTANCE", mMail);

		startActivity(intent);
	
	}
	
	/**
	 * Sets up data for showEmailActivity and starts it	
	 */
	private void showEmails(){
		
		Intent intent = new Intent(getApplicationContext() , ShowEmailListActivity.class);

		intent.putExtra("EXTRA_MAIL_INSTANCE", mMail);

		startActivity(intent);
		
	}


}
