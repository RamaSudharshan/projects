package com.ram.email_box;

import com.ram.email_box.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ShowSingleEmailActivity extends Activity {

	private Mail mMail;
	private String from;
	private String subject;
	private String messageID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_single_email);

		Intent intent = getIntent();
		
		mMail = (Mail) getIntent().getParcelableExtra("EXTRA_MAIL_INSTANCE");
		from = intent.getStringExtra("EXTRA_FROM");
		subject = intent.getStringExtra("EXTRA_SUBJECT");
		messageID = intent.getStringExtra("EXTRA_MESSAGE_ID");
		
		
		TextView fromView = (TextView) findViewById(R.id.message_from);
		if (fromView != null){
			fromView.setText(from);
		}
		
		TextView subjectView = (TextView) findViewById(R.id.message_subject);
		if (subjectView != null){
			subjectView.setText(subject);
		}
		
		TextView bodyView = (TextView) findViewById(R.id.message_body);
		if (bodyView != null){
			bodyView.setText(intent.getStringExtra("EXTRA_BODY"));
		}
		
		findViewById(R.id.reply_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						reply();
					}
				});
		
	}
	
	private void reply(){
		
		Intent intent = new Intent(getApplicationContext() , ReplyEmailActivity.class);

		intent.putExtra("EXTRA_MAIL_INSTANCE", mMail);
		intent.putExtra("EXTRA_REPLY_TO", from);
		intent.putExtra("EXTRA_REPLY_SUBJECT", subject);
		intent.putExtra("EXTRA_REPLY_ID", messageID);
		
		startActivity(intent);
		
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_single_email, menu);
		return true;
	}

}
