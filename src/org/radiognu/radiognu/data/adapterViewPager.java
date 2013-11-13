package org.radiognu.radiognu.data;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.radiognu.radiognu.fragments.*;

public class adapterViewPager extends FragmentStatePagerAdapter {
    private Fragment[] fragments;
    
	public adapterViewPager(FragmentManager fm) {
		super(fm);
		fragments = new Fragment[] {
                new PlayerFragment()
		};

	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments[arg0];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

}
