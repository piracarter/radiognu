package org.radiognu.radiognu.utils;

import org.radiognu.radiognu.R;
import org.radiognu.radiognu.model.track;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RequestTaskAPI extends AsyncTask<String, Void, String> {
    
    private track current_track;
    private View view;

    public RequestTaskAPI(View view){
    	this.view = view;
    }
    @Override
    protected String doInBackground(String... uri)
    {
    	return utilsradiognu.getResponseServiceAPI(uri);
    }

    @Override
    protected void onPostExecute(String response)
    {
    	super.onPostExecute(response);
    	current_track = utilsradiognu.updateTrack(response);
    	Log.d("Debug", "Pasando por onPostExecute " + current_track.getTitle());
    	TextView textTitulo = (TextView) view.findViewById(R.id.textTitulo);
    	TextView textArtista = (TextView) view.findViewById(R.id.textArtista);
    	TextView textAlbum = (TextView) view.findViewById(R.id.textAlbum);
    	TextView textCountry = (TextView) view.findViewById(R.id.textCountry);
    	TextView textyearlicence = (TextView) view.findViewById(R.id.textyearlicence);
    	ImageView imgCover = (ImageView) view.findViewById(R.id.imageTrack);
    	
    	textTitulo.setText(current_track.getTitle());
    	textArtista.setText(current_track.getArtist());
    	textAlbum.setText(current_track.getAlbum());
    	textCountry.setText(current_track.getCountry());
    	textyearlicence.setText(current_track.getYear() + "|"  + current_track.getLicense());
    	
    	String strBase64 = current_track.getStrImageBase64();
    	
    	try {
    		String imageDataBytes = strBase64.substring(strBase64.indexOf(",")+1); 		    		
			byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
			imgCover.setImageBitmap(decodedByte);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Debug", "Pasando por onPostExecute, Excepcion haciendo la conversion de base64 " + e);
			e.printStackTrace();
		}
    	
    }
	
	
	
}	
	

