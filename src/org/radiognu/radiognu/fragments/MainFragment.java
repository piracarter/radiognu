package org.radiognu.radiognu.fragments;

import org.radiognu.radiognu.R;
import org.radiognu.radiognu.data.adapterViewPager;
import org.radiognu.radiognu.utils.utilsradiognu;
import org.radiognu.radiognu.utils.RequestTaskAPI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment 
	extends Fragment 
	implements ViewPager.OnPageChangeListener {

	private adapterViewPager adapter;
    private ViewPager view_pager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View view = inflater.inflate(R.layout.view_pager, null);
    	view_pager = (ViewPager) view.findViewById(R.id.pager);
    	return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       	super.onActivityCreated(savedInstanceState);

       	/* SECCION PARA EL VIEW PAGER */

        adapter = new adapterViewPager(getActivity().getSupportFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.setOnPageChangeListener(this);
        
    }
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
