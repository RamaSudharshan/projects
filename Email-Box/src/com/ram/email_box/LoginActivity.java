package com.ram.email_box;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.ram.email_box.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.prototype.eclient.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	// TODO onResume

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// BASIC Check for a valid email address.
		if (TextUtils.isEmpty(mEmail) || !mEmail.contains("@")) {

			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}
		try {
			// TODO see why validation not working
			new InternetAddress(mEmail);
		} catch (AddressException e) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel && focusView != null) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// perform the user login attempt.
			mAuthTask = new UserLoginTask(getApplicationContext());
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private Context previousContext;
		private Mail mailInstance;

		public UserLoginTask(Context previous) {
			super();
			previousContext = previous;

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			mailInstance = new Mail(mEmail, mPassword);

			// Verify user-password with server
			if (mailInstance.loginIsValid())
				return true;

			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if (success) {

				// Load Home for logged user
				Intent intent = new Intent(previousContext, HomeActivity.class);
				EditText emailText = (EditText) findViewById(R.id.email);
				EditText passwordText = (EditText) findViewById(R.id.password);

				String email = emailText.getText().toString();
				String password = passwordText.getText().toString();

				intent.putExtra("EXTRA_EMAIL", email);
				intent.putExtra("EXTRA_PASS", password);

				intent.putExtra("EXTRA_MAIL_INSTANCE", this.mailInstance);

				startActivity(intent);

			} else {

				// Return to user login
				mEmailView
						.setError(getString(R.string.error_email_password_match));
				View focusView = mEmailView;

				mPasswordView
						.setError(getString(R.string.error_email_password_match));
				focusView = mPasswordView;

				focusView.requestFocus();
			}

		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}

}
