package org.radiognu.radiognu;

import org.radiognu.radiognu.fragments.MainFragment;
import org.radiognu.radiognu.fragments.PlayerFragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	  private MenuItem menuItem;

	
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
	}
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
			Log.d("Depurando","onBackPressed");
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing Activity")
	        .setMessage("Are you sure you want to close this activity?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
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
}