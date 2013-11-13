package org.radiognu.radiognu;

import org.radiognu.radiognu.fragments.MainFragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity {
	
	
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

	}

}