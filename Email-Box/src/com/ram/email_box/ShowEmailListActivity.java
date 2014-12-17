package com.ram.email_box;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.mail.BodyPart;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import com.ram.email_box.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SearchView;

public class ShowEmailListActivity extends Activity {

	private Mail mMail;
	private ArrayList<Message> messages;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_email_list);


		listView = (ListView) findViewById(R.id.mail_list);

		mMail = (Mail) getIntent().getParcelableExtra("EXTRA_MAIL_INSTANCE");

		TextView user = (TextView) findViewById(R.id.user_name);
		if (user != null)
			user.setText(mMail.getPasswordAuthentication().getUserName());

		messages = new ArrayList<Message>();
		Message[] arrayMsg;

		arrayMsg = mMail.getMessages(50);
		for (int i = 50; i > 0; i--)
			messages.add(arrayMsg[i]);

		listView.setAdapter(new MailItemAdapter(this,
				R.layout.activity_show_email_list, R.id.mail_list, messages));

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showSingleEmail(getApplicationContext(), messages.get(position));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_email_list, menu);
		return true;
	}

	private void showSingleEmail(Context context, Message msg) {
		try {

			Intent intent = new Intent(context, ShowSingleEmailActivity.class);

			// Get message_from
			Address[] fromAdresses = msg.getFrom();
			StringBuilder fromBuilder = new StringBuilder();
			String from;

			for (int i = 0; i < fromAdresses.length; i++) {
				fromBuilder.append(fromAdresses[i] + " ");
			}
			from = fromBuilder.toString();

			intent.putExtra("EXTRA_FROM", from);

			// Get message_subject
			String subject = msg.getSubject();
			intent.putExtra("EXTRA_SUBJECT", subject);

			// Get message_body
			Part part = msg;

			if (part.isMimeType("text/plain")) {
				intent.putExtra("EXTRA_BODY", (String) part.getContent());
			} else {
				Multipart multipart = (Multipart) msg.getContent();

				// Greater index bodyparts refer to attachments
				// TODO attachment Handling 
				BodyPart bodyPart = multipart.getBodyPart(0);

				BufferedReader r = new BufferedReader(new InputStreamReader(
						bodyPart.getInputStream()));
				StringBuilder body = new StringBuilder(bodyPart
						.getInputStream().available());
				String line;
				while ((line = r.readLine()) != null) {
					body.append(line + '\n');
				}

				intent.putExtra("EXTRA_BODY", body.toString());
			}
			
			intent.putExtra("EXTRA_MAIL_INSTANCE", mMail);
			
			String id = msg.getHeader("Message-ID")[0];
				
			intent.putExtra("EXTRA_MESSAGE_ID", id);

			startActivity(intent);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adapter used to populate listItems with Message data
	 */
	public class MailItemAdapter extends ArrayAdapter<Message> {

		private final ArrayList<Message> messages;

		public MailItemAdapter(Context context, int layoutResourceId,
				int textViewResourceId, ArrayList<Message> values) {
			
			super(context, layoutResourceId, textViewResourceId, values);
			this.messages = values;
		}

		@Override
		//TODO Add attachment handling
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.inbox_item, null);
			}

			Message msg = messages.get(position);
			try {
				if (msg != null) {
					
					//From
					TextView from = (TextView) v.findViewById(R.id.mail_from);
					
					//Subject
					TextView subject = (TextView) v
							.findViewById(R.id.mail_subject);
					
					//Time
					TextView time = (TextView) v.findViewById(R.id.mail_time);

					if (from != null)
						from.setText(msg.getFrom()[0].toString());

					if (subject != null)
						subject.setText(msg.getSubject());

					if (time != null) {
						time.setText(msg.getReceivedDate().getHours() + ":"
								+ msg.getReceivedDate().getMinutes());
					}

				}
			} catch (MessagingException e) {
				// TODO Handle correctly
				e.printStackTrace();
			}

			return v;

		}
	}

}
