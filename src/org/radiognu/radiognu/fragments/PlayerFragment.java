package org.radiognu.radiognu.fragments;


import org.radiognu.radiognu.MainActivity;
import org.radiognu.radiognu.R;
import org.radiognu.radiognu.serviceplayerstreaming;
import org.radiognu.radiognu.db.DBHelper;
import org.radiognu.radiognu.utils.WebAppInterface;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerFragment extends Fragment implements OnSeekBarChangeListener {
	public static View viewStatic;
	public static WebView staticWebView;
	private View view;
	private MediaPlayer mediaPlayer;
	private WebView myWebView;
	private SeekBar sb;
    private AudioManager am;
    private int Volume=0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragmentplayer, null);
		viewStatic = view;
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction("NOTIFY_SERVICE");
		filter.addAction("FINISH_APP");
		getActivity().registerReceiver(receiver, filter);
		
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mediaPlayer = new MediaPlayer();
		final ImageView imageControlRadio = (ImageView) view.findViewById(org.radiognu.radiognu.R.id.imageControlRadio);
		//final TextView textMessageRadio = (TextView) view.findViewById(org.radiognu.radiognu.R.id.textMessageRadio); 
	    
		/*Obtener el estado del mediaplayer */
		int estadomp = MainActivity.getEstatusPlayStreaming();
		
		
		if (estadomp == 1) { 
			imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_pause));
		} 
		MainActivity.setEstatusServicio(false);
		
	    imageControlRadio.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) {
					
				/*
				 * Determinar si el servicio org.radiognu.radiognu.serviceplayerstreaming esta en ejecuci�n
				 */
				ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
			    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			        if (serviceplayerstreaming.class.getName().equals(service.service.getClassName())) {
			        	MainActivity.setEstatusServicio(true);
			        }
			    }
						
				if (MainActivity.isPlay() == false) { 
					imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_pause));
					//textMessageRadio.setText(getResources().getString(R.string.MessageRadioPlay));
					if (!MainActivity.isEstatusServicio()) {
						/*
						 * Iniciando el servicio de streaming
						 */
						Intent intent = new Intent(getActivity().getBaseContext(), serviceplayerstreaming.class);
				        getActivity().getBaseContext().startService(intent);
				        	
						//intialStage = false;
						}
		            else {
		            	sendBroadCast("resume");	
		            }
		            
					MainActivity.setPlay(true);
				}
				else {
					imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_play));
					//textMessageRadio.setText(getResources().getString(R.string.MessageRadio));
					sendBroadCast("stop");
					MainActivity.setPlay(false);
				}
				
				
			}
	    	
	    });
	    
	    /*
	     * CODIGO PARA EL SOCKET
	     */
	    myWebView = (WebView) view.findViewById(R.id.webView);
		 
        // Enable JavaScript
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBlockNetworkLoads(false);
        
                
     // Bind a new interface between your JavaScript and Android code
        myWebView.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
        
     // Load HTML page
        myWebView.loadUrl("file:///android_asset/index.html");
        staticWebView = myWebView;
        
        /*
		 * codigo para inicializar el control de volumen
		 */
		sb = (SeekBar) view.findViewById(R.id.sbVolume);
		am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        
        sb.setMax(maxVolume);
        sb.setProgress(curVolume);
        
        sb.setOnSeekBarChangeListener(this);
        
        if (DBHelper.isExistsTrackInfo()) { 
        	SQLiteDatabase db = MainActivity.getDBHelper().getReadableDatabase();
        	Cursor cursor = db.rawQuery("SELECT * from track",null);
        	cursor.moveToFirst();
        	TextView textTitle = (TextView) view.findViewById(R.id.textTitulo);
        	textTitle.setText(cursor.getString(0));
        	TextView textArtist = (TextView) view.findViewById(R.id.textArtista);
        	textArtist.setText(cursor.getString(1));
        	TextView textAlbum = (TextView) view.findViewById(R.id.textAlbum);
        	textAlbum.setText(cursor.getString(2));
        	TextView textCountry = (TextView) view.findViewById(R.id.textCountry);
        	textCountry.setText(cursor.getString(3));
        	TextView textYearLicence = (TextView) view.findViewById(R.id.textyearlicence);
        	textYearLicence.setText(cursor.getString(4));
        	
        	ImageView imgCover = (ImageView) view.findViewById(R.id.imageTrack);
        	
        	try {
        		String imageDataBytes = cursor.getString(5).substring(cursor.getString(5).indexOf(",")+1); 		    		
    			byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
    			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
    			imgCover.setImageBitmap(decodedByte);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	
        }
	}

	private void sendBroadCast(String state) {   
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("NOTIFY_AUDIO");         
        broadcastIntent.putExtra("state", state);
        getActivity().sendBroadcast(broadcastIntent);
	}
	
  	@Override
	public void onPause() {
	    super.onPause();
	    if (mediaPlayer != null) {
	        mediaPlayer.reset();
	        mediaPlayer.release();
	        mediaPlayer = null;
	    }
	}
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
		      String action = intent.getAction();

		
		      if(action.equals("NOTIFY_SERVICE")){
				ImageView imageControlRadio = (ImageView) view.findViewById(R.id.imageControlRadio);
		        imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.play32));
		      }
   
		   }
		};

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		am.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
		Volume = arg1;   
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		Toast.makeText(getActivity().getApplicationContext(), "Volume: " + Integer.toString(Volume), Toast.LENGTH_SHORT).show();

		
	}
}