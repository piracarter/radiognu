package org.radiognu.radiognu;
import java.io.IOException;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class serviceplayerstreaming  extends Service  {
	private MediaPlayer mediaPlayer;
	@Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Servicio en Ejecucion", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
        unregisterReceiver(receiver);
        Toast.makeText(this, getResources().getString(R.string.exit_service), Toast.LENGTH_SHORT).show();
    }

	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = new MediaPlayer();
		
		Player player = new Player();
		player.execute(getResources().getString(R.string.URL_AUDIO));
        
		IntentFilter filter = new IntentFilter();
		filter.addAction("NOTIFY_AUDIO");
		filter.addAction("FINISH_APP");
		registerReceiver(receiver, filter);
		
	}
	class Player extends AsyncTask<String, Void, Boolean> {
	    private ProgressDialog progressDialog;

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(MainActivity.getContext());
			progressDialog.setMessage(getResources().getString(R.string.message_conn2));
			progressDialog.setCancelable(false);
			progressDialog.show();
	        //this.progress.setMessage("Buffering...");
	        //this.progress.show();

	    }
	    
	    @Override
	    protected Boolean doInBackground(String... params) {
	        Boolean prepared;
	        try {
	            mediaPlayer.setDataSource(params[0]);

	            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                    mediaPlayer.stop();
	                    mediaPlayer.reset();
	                }
	            });
	            mediaPlayer.prepare();
	            prepared = true;
	            MainActivity.setEstatusPlayStreaming(1); /* Se esta reproduciendo el streaming */
	        } catch (IllegalArgumentException e) {
	            prepared = false;
	            e.printStackTrace();
	        } catch (SecurityException e) {
	            prepared = false;
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            prepared = false;
	            e.printStackTrace();
	        } catch (IOException e) {
	        	prepared = false;
	            e.printStackTrace();
	        }
	        return prepared;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        progressDialog.dismiss();
	        if (result) {
	        	mediaPlayer.start();
	        }
	        else
	        {
	        	mediaPlayer.reset();
	        	
	        	Intent broadcastIntent = new Intent();
	            broadcastIntent.setAction("NOTIFY_SERVICE");         
	            broadcastIntent.putExtra("state", "update ui");
	            sendBroadcast(broadcastIntent);
	        	
	        	stopService();
	        }
	    }

	    public Player() {
	        //progress = new ProgressDialog(getActivity());
	    }

	    
	}
	public class LocalBinder extends Binder {
        public serviceplayerstreaming getService() {
            return serviceplayerstreaming.this;
        }
    }
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
		      String action = intent.getAction();
	   		
		      if(action.equals("NOTIFY_AUDIO")){
		        /*
		         * En caso de enviar stop
		         */
		        
		        if (intent.getStringExtra("state").equals("stop"))  { 
		        	if (mediaPlayer.isPlaying()) {
		        		mediaPlayer.pause();     
		         
		                MainActivity.setEstatusPlayStreaming(0);					}
		        }
		        /*
		         * En caso de enviar resume
		         */
		        if (intent.getStringExtra("state").equals("resume"))  { 
		        	if (!mediaPlayer.isPlaying()) {
		        		mediaPlayer.start();     
		              
		                MainActivity.setEstatusPlayStreaming(1);					

					}
		        }
		        
		      }
      
		   }
		};
		public void stopService() { 
			this.stopSelf();
			MainActivity.setEstatusPlayStreaming(0);
			MainActivity.setPlay(false);
		}
		
}
