package org.radiognu.radiognu;

import org.radiognu.radiognu.db.DBHelper;
import org.radiognu.radiognu.fragments.MainFragment;
import org.radiognu.radiognu.fragments.PlayerFragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	 private MenuItem menuItem;
	 private static DBHelper dbhelper;
	 private static String imgRadioGNU;
	 private static Context context;
	 private static int estatusPlayStreaming = 0; /* Stop */
	 private static boolean play = false;
	 public static boolean isPlay() {
		return play;
	}
	public static void setPlay(boolean play) {
		MainActivity.play = play;
	}
	public static boolean isEstatusServicio() {
		return estatusServicio;
	}
	public static void setEstatusServicio(boolean estatusServicio) {
		MainActivity.estatusServicio = estatusServicio;
	}
	private static boolean estatusServicio = false;
	
 	public static int getEstatusPlayStreaming() {
		return estatusPlayStreaming;
	}
	public static void setEstatusPlayStreaming(int i) {
		MainActivity.estatusPlayStreaming = i;
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    /*
	     * ACTIONBAR
	     */
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(getResources().getString(R.string.subtitle));

		/*      
         * SECCION PARA DIBUJAR EL MAIN_FRAGMENT DENTRO DE LA MAIN_ACTIVITY
         */
        Fragment f = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
               .replace(R.id.main_content, f)
               .commit();
        
        /*
         * CODIGO PARA EL BOTON REFRESH
         */
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
	        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
     
        dbhelper = new DBHelper(this);
        imgRadioGNU = getResources().getString(R.string.IMG_RADIOGNU);
        context = this;
	}
 	public static Context getContext() { return context; }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu_load:
	      menuItem = item;
	      menuItem.setActionView(R.layout.progressbar);
	      menuItem.expandActionView();
	      refreshTask task = new refreshTask();
	      task.execute("test");
	      break;
	    default:
	      break;
	    }
	    return true;
	  }

	  private class refreshTask extends AsyncTask<String, Void, String> {

	    @Override
	    protected String doInBackground(String... params) {
	      // Simulate something long running
	      try {
	    	  PlayerFragment.staticWebView.reload();
	        Thread.sleep(2000);
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      }
	      return null;
	    }

	    @SuppressLint("NewApi")
		@Override
	    protected void onPostExecute(String result) {
	      menuItem.collapseActionView();
	      menuItem.setActionView(null);
	    }
	  };
	  @Override
		public void onBackPressed() {
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(getResources().getString(R.string.message_title_exit_app))
	        .setMessage(getResources().getString(R.string.message_exit_app))
	        .setPositiveButton(getResources().getString(R.string.botonYes_message_exit_app), new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	Intent intent = new Intent(getBaseContext(), serviceplayerstreaming.class);
		        getBaseContext().stopService(intent);
		        PlayerFragment.staticWebView.destroy();
	            finish();    
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
		
		}
	  public static DBHelper getDBHelper() { 
		  return dbhelper;
	  }
	  public static String getImgRadioGNU() { 
		  return imgRadioGNU;
	  }
}