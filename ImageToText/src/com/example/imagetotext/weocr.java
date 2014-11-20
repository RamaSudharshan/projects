package com.example.imagetotext;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

public class weocr {
    
	String response;
    weocr() throws UnsupportedEncodingException, ParseException, ClientProtocolException, IOException
    {
       String url="http://appsv.ocrgrid.org/cgi-bin/weocr/submit_tesseract.cgi";
       response="";
       HttpClient client = new DefaultHttpClient();
       client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
       HttpPost post=new HttpPost(url);
       MultipartEntity entity = new MultipartEntity (HttpMultipartMode.BROWSER_COMPATIBLE);


       
       //test start
       try {
    	   	Bitmap bm = BitmapFactory.decodeFile("/sdcard/DCIM/test.jpg");
    	   	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	   	bm.compress(CompressFormat.JPEG, 75, bos);
    	   	byte[] data = bos.toByteArray();
    	   	ByteArrayBody bab = new ByteArrayBody(data, "testbin.jpg");
    	   	        
       
       //test end
	 	  entity.addPart( "userfile", bab);

        // For usual String parameters
        entity.addPart( "outputencoding", new StringBody(data, "utf-8"));
        entity.addPart( "outputformat", new StringBody(data, "txt"));
        post.setEntity( entity );
        HttpResponse response = client.execute(post);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		String sResponse;
		StringBuilder s = new StringBuilder();

		while ((sResponse = reader.readLine()) != null) {
			s = s.append(sResponse);
		}
	
		
		this.response=new String(s.toString());

		System.out.println("Response: " + s);
        
       // Here we go!
       String response1 = EntityUtils.toString( client.execute( post ).getEntity(), "UTF-8" );
       client.getConnectionManager().shutdown();
       System.out.println(response1);
       Toast toast=Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG);
        toast.show();
	


       }

       
       catch (Exception e) {
           Log.e(e.getClass().getName(), e.getMessage());
       }
       
     }
	private Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}