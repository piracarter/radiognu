package org.radiognu.radiognu.utils;

import org.radiognu.radiognu.MainActivity;
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
	//private ProgressDialog progressDialog;
    
    public RequestTaskAPI(View view){
    	this.view = view;
    }
    @Override
    protected void onPreExecute() {
       	super.onPreExecute();
       	//progressDialog = new ProgressDialog(view.getContext());
		//progressDialog.setTitle(view.getResources().getString(R.string.message_conn));
		//progressDialog.show();
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
    	//progressDialog.dismiss();
    	current_track = utilsradiognu.updateTrack(response);
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
			e.printStackTrace();
		}
    	/* 
    	 * GUARDANDO EN LA BASE DE DATOS
    	 */
    	MainActivity.getDBHelper().saveTrackInfo(current_track.getTitle(), current_track.getArtist(), current_track.getAlbum(),
				current_track.getCountry(), current_track.getYear() + "|" + current_track.getLicense(), strBase64);
		
    }
	
	
	
}	
	

