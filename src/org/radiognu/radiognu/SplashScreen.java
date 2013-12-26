package org.radiognu.radiognu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
	private static Context context;

	public static Context getContext() { return context; }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       	super.onCreate(savedInstanceState);
       	setContentView(R.layout.activity_splash);
       	
       	context = this;
       	/*
       	 * Validar conexion a red
       	 * Si no hay conexion a red terminar la aplicacion 
       	 */
       	boolean flag_network_online = false;
       	Boolean flag_api_available = false;
        flag_network_online = hasNetworkConnection();
        
       	if (flag_network_online == false) {
       		Toast.makeText(context, getResources().getString(R.string.splashscreen_message_no_network), Toast.LENGTH_LONG).show();;
       		new Handler().postDelayed(new Runnable() {
       			@Override
       			public void run() {
       				finish();
       			}
       			}, SPLASH_TIME_OUT);
       		}
       	
       	TaskIsOnline task = new TaskIsOnline();
       	task.execute();
       	
       	try {
			flag_api_available = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	Log.d("Depurando","result " + flag_api_available);
 
       	if (flag_api_available == false) {
       		Toast.makeText(context, getResources().getString(R.string.splashscreen_message_no_api), Toast.LENGTH_LONG).show();;
       		finish();
       	} 
       	else { 
       		
       		final ProgressDialog pdiag = new ProgressDialog(this);
       		pdiag.setMessage(getResources().getString(R.string.splashscreen_message_progressdialog));
       		pdiag.setCancelable(false);
       		pdiag.show();
       		new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {
               	pdiag.dismiss();
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
       		}
    }
    
    class TaskIsOnline extends AsyncTask<Void, Void, Boolean> {
    	ProgressDialog progressDialog;
    	    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		progressDialog = new ProgressDialog(SplashScreen.getContext());
    		progressDialog.setMessage(getResources().getString(R.string.splashscreen_message_checknetwork));
    		progressDialog.show();
    		
    	}
		@Override
		protected Boolean doInBackground(Void... params) {
		
			HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = null;
	        Boolean result = false;
	        try
	        {
	        	// make a HTTP request
	        	response = httpclient.execute(new HttpGet(getResources().getString(R.string.PUBLIC_API)));
	       
	        	StatusLine statusLine = response.getStatusLine();
	        	if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
	        		ByteArrayOutputStream out = new ByteArrayOutputStream();
	        		response.getEntity().writeTo(out);
	        		out.close();
	        		responseString = out.toString();
	        		result= true;
	        	}
	        	else {
	        		result = false;
	        		response.getEntity().getContent().close();
	        		throw new IOException(statusLine.getReasonPhrase());
	        	}
	        }catch (Exception e) {
	        	result=false;
	        }
	        return result;
		}
    	@Override
    	protected void onPostExecute(Boolean result) {
    		super.onPostExecute(result);
    		progressDialog.dismiss();
    		
    	}
    }
    private boolean hasNetworkConnection() { 
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo netInfo = cm.getActiveNetworkInfo();
    	if (netInfo != null && netInfo.isConnected()) {
    		return true;
    	}
    	return false;
    }
}
