package org.radiognu.radiognu.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class UpdateService extends Service {
	private static final String TAG = UpdateService.class.getSimpleName();
	static final int DELAY = 60000;
	// 60000 milisegundos
	private boolean runFlag = false;
	private Updater updater;
	//private MainApplication application;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
		updater = new Updater();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		runFlag = false;
		//application.setServiceRunningFlag(false);
		updater.interrupt();
		updater = null;
		//Log.d(TimelineActivity.TAG_GENERAL, "Pasando por UpdateService.onDestroy()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand");
		runFlag = true;
		//application.setServiceRunningFlag(true);
		//Log.d(TimelineActivity.TAG_GENERAL, "Pasando por UpdateService.onStartCommand() antes del start al Thread");
		updater.start();
		//Log.d(TimelineActivity.TAG_GENERAL, "Pasando por UpdateService.onStartCommand() despues del start al Thread");
		return START_STICKY;
	}

	public class Updater extends Thread { 
		
		public Updater() { 
			
			super("UpdaterService-UpdateThread");
		}
		@Override
		public void run() {
			UpdateService updateService  = UpdateService.this;
			while (updateService.runFlag) { 
				Log.d(TAG, "UpdaterThread running");
				try { 
					
					//Log.d(TimelineActivity.TAG_GENERAL, "Pasando por el Thread Updater despues de Sleep");
				}
				catch(Exception ex){
					updateService.runFlag = false;
					//application.setServiceRunningFlag(false);
					
				}
			}
		}
	}
}
