package org.radiognu.radiognu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.radiognu.radiognu.model.track;

import android.util.Log;

public class utilsradiognu {

	public static final String getResponseServiceAPI(String... uri){
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try
        {
        	// make a HTTP request
        	response = httpclient.execute(new HttpGet(uri[0]));
        	StatusLine statusLine = response.getStatusLine();
        	if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        		// request successful - read the response and close the connection
        		ByteArrayOutputStream out = new ByteArrayOutputStream();
        		response.getEntity().writeTo(out);
        		out.close();
        		responseString = out.toString();
        	}
        	else {
        		// request failed - close the connection
        		response.getEntity().getContent().close();
        		throw new IOException(statusLine.getReasonPhrase());
        	}
        }catch (Exception e) {
        	Log.d("Test", "Couldn't make a successful request!");
        	Log.d("Response", "Respuesta Entro a Excepcion: " + e);
        }
        Log.d("Response", "Respuesta " + responseString);
        return responseString;
	}
	public static final track updateTrack(String response) { 
		track current_track = new track();;
		try{
			JSONObject jsonResponse = new JSONObject(response);
			current_track.setArtist(jsonResponse.getString("artist"));
			current_track.setTitle(jsonResponse.getString("title"));
			current_track.setAlbum(jsonResponse.getString("album"));
			current_track.setCountry(jsonResponse.getString("country"));
			current_track.setVivo((jsonResponse.getString("isLive").compareTo("true") == 0)?true:false);
			current_track.setYear(jsonResponse.getString("year"));
			JSONObject jsonLicense = jsonResponse.getJSONObject("license");
			current_track.setLicense(jsonLicense.getString("shortname"));
			current_track.setStrImageBase64(jsonResponse.getString("cover"));
		}catch (Exception ex) { 
			Log.d("Debug", "Respuesta " + ex );
		}
		return current_track;
	}
	
}
