package org.radiognu.radiognu.fragments;


import org.radiognu.radiognu.R;
import org.radiognu.radiognu.serviceplayerstreaming;
import org.radiognu.radiognu.utils.WebAppInterface;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerFragment extends Fragment {
	public static View viewStatic;
	public static WebView staticWebView;
	private View view;
	private boolean play = false;
	private MediaPlayer mediaPlayer;
	private WebView myWebView;
	private boolean estatusServicio;
	
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
		final TextView textMessageRadio = (TextView) view.findViewById(org.radiognu.radiognu.R.id.textMessageRadio); 
	    
	    imageControlRadio.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) {
			
				/*
				 * Determinar si el servicio org.radiognu.radiognu.serviceplayerstreaming esta en ejecuci�n
				 */
				ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
			    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			        if (serviceplayerstreaming.class.getName().equals(service.service.getClassName())) {
			        	Log.d("Depurando","servicio de mediaplayer en running");
			        	estatusServicio = true;
			        }
			    	Log.d("Depurando", service.service.getClassName());
			    }
						
				if (play == false) { 
					imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.stop32));
					textMessageRadio.setText(getResources().getString(R.string.MessageRadioPlay));
					if (!estatusServicio) {
						/*
						 * Iniciando el servicio de streaming
						 */
						Intent intent = new Intent(getActivity().getBaseContext(), serviceplayerstreaming.class);
				        getActivity().getBaseContext().startService(intent);
				        	
						Log.d("Depurando","Servicio iniciado");
						//intialStage = false;
						}
		            else {
		            	sendBroadCast("resume");	
		            	Log.d("Depurando", "MediaPlayer continua");
		            }
		            
					play = true;
				}
				else {
					imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.play32));
					textMessageRadio.setText(getResources().getString(R.string.MessageRadio));
					sendBroadCast("stop");
					Log.d("Depurando","MediaPlayer detenido");
					play = false;
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
	}

	private void sendBroadCast(String state) {   
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("NOTIFY_AUDIO");         
        broadcastIntent.putExtra("state", state);
        getActivity().sendBroadcast(broadcastIntent);
        Log.d("Depurando","Fin del metodo sendBroadCast()");
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
	          Log.d("Depurando ","***************************************");
	          Log.d("Depurando ","***************************************");
	          Log.d("Depurando ","***************************************");
	          Log.d("Depurando action", action);
		
		      if(action.equals("NOTIFY_SERVICE")){
		        Log.d("Depurando","Actualizar la UI");
				ImageView imageControlRadio = (ImageView) view.findViewById(R.id.imageControlRadio);
		        imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.play32));
		        TextView textMessageRadio = (TextView) view.findViewById(R.id.textMessageRadio);
				textMessageRadio.setText(getResources().getString(R.string.MessageRadio));
				play = false;
		      }
   
		   }
		};
}