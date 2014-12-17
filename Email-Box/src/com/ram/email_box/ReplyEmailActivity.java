package com.ram.email_box;

import javax.mail.MessagingException;

import com.ram.email_box.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ReplyEmailActivity extends Activity {

	
	private Mail mMail;
	private String replyAddress;
	private String replySubject;
	private String replyID;

	
	private EditText mToView;
	private EditText mSubjectView;
	private EditText mBodyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply_email);
		
		Intent intent = getIntent();
		
		mMail = (Mail) intent.getParcelableExtra("EXTRA_MAIL_INSTANCE");
		
		mToView = (EditText) findViewById(R.id.to);
		mSubjectView = (EditText) findViewById(R.id.subject);
		mBodyView = (EditText) findViewById(R.id.body);
		
		replyAddress = intent.getStringExtra("EXTRA_REPLY_TO");
		mToView.setText(replyAddress);
			
		replySubject = intent.getStringExtra("EXTRA_REPLY_SUBJECT");
		mSubjectView.setText("Re: "+ replySubject);
			
		replyID = intent.getStringExtra("EXTRA_REPLY_ID");
			

		findViewById(R.id.send_email).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						sendEmail();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reply_email, menu);
		return true;
	}

	
private void sendEmail() {
		
		// Store View values at the time of the send attempt.
	
		String[] to = new String[1];
		to[0] = replyAddress;

		String subject = replySubject;
		String ID = replyID; 
				
		String body = mBodyView.getText().toString();
		
		// Fill email data
		mMail.setFrom(mMail.getPasswordAuthentication().getUserName());
		mMail.setTo(to);
		mMail.setSubject(subject);

		mMail.setBody(body);
		
		mMail.setID(ID);
		
		try {
			mMail.send(true);
			//TODO Flash SENT message
			finish();
		} catch (MessagingException e) {
			// TODO Handle Correctly, possibly RETRY
			e.printStackTrace();
			System.exit(1);
		}
	}
}
