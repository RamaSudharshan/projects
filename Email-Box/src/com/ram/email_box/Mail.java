package com.ram.email_box;

import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.os.Parcel;
import android.os.Parcelable;

import com.sun.mail.imap.IMAPFolder;

/*
 * JavaMail wrapper class
 * 
 * Source: http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_%28no_Intents%29_in_Android
 * 
 * Added functionality: 
 * 		imap protocol support
 * 		getMessages()
 * 		loginIsValid()
 * 		getStore()
 * 		reply as conversation
 * 		implements Parcelable
 * 		
 */
public class Mail extends javax.mail.Authenticator implements Parcelable {
	private String _user;
	private String _pass;

	private String[] _to;
	private String _from;

	private String _port;
	private String _sport;

	private String _smtpHost;
	private String _imapHost;

	private String _subject;
	private String _body;

	private boolean _auth;

	private boolean _debuggable;

	private Multipart _multipart;
	
	
	
	
	
	private String _ID;

	public Mail() {

		_smtpHost = "smtp.gmail.com"; // default smtp server
		_imapHost = "imap.gmail.com"; // default imap server

		_port = "465"; // default smtp port
		_sport = "465"; // default socketfactory port

		_user = ""; // username
		_pass = ""; // password
		_from = ""; // email sent from
		_subject = ""; // email subject
		_body = ""; // email body
		
		_ID = "";

		_debuggable = false; // debug mode on or off - default off
		_auth = true; // smtp authentication - default on

		_multipart = new MimeMultipart();

		// There is something wrong with MailCap, javamail can not find a
		// handler for the multipart/mixed part, so this bit needs to be added.
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
	}

	public Mail(String user, String pass) {
		this();
		_user = user;
		_pass = pass;
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(_user, _pass);
	}

	private Properties _setProperties() {
		Properties props = new Properties();

		props.put("mail.trasport.protocol", "smtp");
		props.put("mail.smtp.host", _smtpHost);

		props.put("mail.store.protocol", "imap");
		props.put("mail.imaps.host", _imapHost);

		if (_debuggable) {
			props.put("mail.debug", "true");
		}
		if (_auth) {
			props.put("mail.smtp.auth", "true");
		}

		props.put("mail.smtp.port", _port);
		props.put("mail.smtp.socketFactory.port", _sport);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		return props;
	}

	public boolean send(boolean isReply) throws AddressException, MessagingException {

		Properties props = _setProperties();

		if (!_user.equals("") 	 && !_pass.equals("") &&
			_to.length > 0 	  	 && !_from.equals("") &&
			!_subject.equals("") &&	!_body.equals("")) {

			Session session = Session.getInstance(props, this);

			// Create and fill new email 
			
			MimeMessage msg = new MimeMessage(session);
			
			//From, reply address by default
			msg.setFrom(new InternetAddress(_user));
			
			msg.setReplyTo(new InternetAddress[] { 
					new InternetAddress(_user)});
			
			//Subject
			msg.setSubject(_subject);
			
			//Date
			msg.setSentDate(new Date());

			//To
			InternetAddress[] addressTo = new InternetAddress[_to.length];
			for (int i = 0; i < _to.length; i++) {
				addressTo[i] = new InternetAddress(_to[i]);
			}
			
						
			if (isReply){
				
				msg.setHeader("Message-Id", _ID);

				msg = (MimeMessage) msg.reply(false);
				//From, reply address by default
				msg.setFrom(new InternetAddress(_user));
				
				msg.setReplyTo(new InternetAddress[] { 
						new InternetAddress(_user)});
				
			} else{
				msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
			}
		 
			//Body
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(_body);
			_multipart.addBodyPart(messageBodyPart);

			msg.setContent(_multipart);
			// send email
			Transport.send(msg);

			
			return true;
		}

		return false;
		
	}

	public void addAttachment(String filename) throws Exception {

		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filename);

		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename);

		_multipart.addBodyPart(messageBodyPart);
	}

	
	/**
	 * Gets the N newest messages from default folder Inbox. 
	 * 
	 * @param messagesSinceNewest Number of messages to retrieve
	 * @return Fetched messages
	 */
	// TODO Add selection of folder type
	//TODO Add POP3 support
	public Message[] getMessages(int messagesSinceNewest){

		IMAPFolder folder = null;
		Message[] messages = null;
		
		try {
			//Starts connection
			Store store = this.getStore();
			store.connect();
			
			folder = (IMAPFolder) store.getFolder("INBOX"); //default folder 
			folder.open(Folder.READ_ONLY);
			
			int newest = folder.getMessageCount();

			messages = folder.getMessages( (newest - messagesSinceNewest) , newest);	
						
		} catch (MessagingException e) {
			//TODO Handle correctly

			e.printStackTrace();
			System.exit(2);
		}
		
		return messages;
	}

	/**
	 * Checks against email server whether or not user-password correspond each other
	 * 
	 * @return true if server validated user-password 
	 */
	public boolean loginIsValid(){
		
		Store store = null;
		
		try {
			store = this.getStore();
			store.connect();
			
			// No Exception thrown, Successful login.
			// Close service connection until actual use
			store.close();
			
		} catch (AuthenticationFailedException e) {
			return false;
			
		} catch (MessagingException e) {
			//TODO Handle correctly
			e.printStackTrace();
			System.exit(1);
		}

		return true;
	}
	
	// TODO POP3 support
	private Store getStore() throws NoSuchProviderException {
		Properties props = this._setProperties();

		Session session = Session.getInstance(props, this);
		return session.getStore("imaps");
	}

	
	// the getters and setters
	public void setBody(String _body) {
		this._body = _body;
	}
	
	public void setID(String _id) {
		this._ID = _id;
	}

	public void setTo(String[] toArr) {
		this._to = toArr;
	}

	public void setFrom(String string) {
		this._from = string;
	}

	public void setSubject(String string) {
		this._subject = string;
	}

	// Parcelable implementation methods
	public Mail(Parcel in) {
		this();
		readFromParcel(in);
	}

	public static final Parcelable.Creator<Mail> CREATOR = new Parcelable.Creator<Mail>() {
		public Mail createFromParcel(Parcel in) {
			return new Mail(in);
		}

		public Mail[] newArray(int size) {
			return new Mail[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(_user);
		dest.writeString(_pass);

		dest.writeString(_port);
		dest.writeString(_sport);
		dest.writeString(_smtpHost);
		dest.writeString(_imapHost);
		

		boolean[] bools = { _auth, _debuggable };
		dest.writeBooleanArray(bools);

	}

	public void readFromParcel(Parcel in) {

		_user = in.readString();
		_pass = in.readString();

		_port = in.readString();
		_sport = in.readString();
		_smtpHost = in.readString();
		_imapHost = in.readString();
		
		boolean[] bools = new boolean[2];
		in.readBooleanArray(bools);

		_auth = bools[0];
		_debuggable = bools[1];
	
	}

}
