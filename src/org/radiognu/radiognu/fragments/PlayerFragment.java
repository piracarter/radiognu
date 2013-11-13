package org.radiognu.radiognu.fragments;

import java.io.IOException;

import org.radiognu.radiognu.R;
import org.radiognu.radiognu.utils.RequestTaskAPI;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerFragment extends Fragment {
	private View view;
	private boolean play = false;
	private boolean intialStage = true;
	private MediaPlayer mediaPlayer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragmentplayer, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new RequestTaskAPI(view).execute("http://radiognu.org/api/");
		mediaPlayer = new MediaPlayer();
		final ImageView imageControlRadio = (ImageView) view.findViewById(org.radiognu.radiognu.R.id.imageControlRadio);
		final TextView textMessageRadio = (TextView) view.findViewById(org.radiognu.radiognu.R.id.textMessageRadio); 
	    
	    imageControlRadio.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (play == false) { 
					imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.stop32));
					textMessageRadio.setText(getResources().getString(R.string.MessageRadioPlay));
					if (intialStage) {
		                new Player()
		                        .execute(getResources().getString(R.string.URL_AUDIO));
						}
		            else {
		                if (!mediaPlayer.isPlaying()) {
		                    mediaPlayer.start();
		                }
		            }
					play = true;
				}
				else {
					imageControlRadio.setImageDrawable(getResources().getDrawable(R.drawable.play32));
					textMessageRadio.setText(getResources().getString(R.string.MessageRadio));
					
					if (mediaPlayer.isPlaying()) {
		                mediaPlayer.pause();          
						}
					play = false;
				}
			}
	    	
	    });
	}
	
	class Player extends AsyncTask<String, Void, Boolean> {
	    private ProgressDialog progress;

	    @Override
	    protected Boolean doInBackground(String... params) {
	        Boolean prepared;
	        try {
	        	Log.d("Debug", "Pasando " + params[0]);
	            mediaPlayer.setDataSource(params[0]);

	            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                    intialStage = true;
	                    play=false;
	                    mediaPlayer.stop();
	                    mediaPlayer.reset();
	                }
	            });
	            mediaPlayer.prepare();
	            prepared = true;
	        } catch (IllegalArgumentException e) {
	            Log.d("IllegarArgument", e.getMessage());
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
	        super.onPostExecute(result);
	        if (progress.isShowing()) {
	            progress.cancel();
	        }
	        Log.d("Prepared", "//" + result);
	        mediaPlayer.start();

	        intialStage = false;
	    }

	    public Player() {
	        progress = new ProgressDialog(getActivity());
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        this.progress.setMessage("Buffering...");
	        this.progress.show();

	    }
	}
	@Override
	public void onPause() {
	    // TODO Auto-generated method stub
	    super.onPause();
	    if (mediaPlayer != null) {
	        mediaPlayer.reset();
	        mediaPlayer.release();
	        mediaPlayer = null;
	    }
	}
	
}
