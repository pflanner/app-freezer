package com.example.appfreezer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	static final int NUM_ITEMS = 2;

    MyAdapter mAdapter;

    ViewPager mPager;
    
    static List<RunningAppProcessInfo> procInfos;
    static List<String> procNames;
    static List<String> apkNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
        
        // Get a list of names of all running processes, and store them in procNames
        ActivityManager actvityManager = (ActivityManager)this.getSystemService( ACTIVITY_SERVICE );
        procInfos = actvityManager.getRunningAppProcesses();
        procNames = new ArrayList<String>();
        for (RunningAppProcessInfo info : procInfos) {
            procNames.add(info.processName);
        } 
        
        // Get a list of names of all installed processes, and store them in apkNames
        final PackageManager pm = getPackageManager();
      //get a list of installed apps.
      List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
      apkNames = new ArrayList<String>();
      StringBuffer sb = new StringBuffer();
      for (ApplicationInfo packageInfo : packages) {
    	  // reset the string buffer
    	  sb.delete(0, sb.length());
          sb.append("Installed package : ");
          sb.append(packageInfo.packageName);
          sb.append('\n');
          sb.append("Source dir : ");
          sb.append(packageInfo.sourceDir);
          
          // add the info to apkNames
          apkNames.add(sb.toString());
      }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		
	}
	
	public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
	
	// INNER CLASSES
	
	public class MyAdapter extends FragmentPagerAdapter {
	    public MyAdapter(FragmentManager fm) {
	        super(fm);
	    }

	    @Override
	    public int getCount() {
	        return NUM_ITEMS;
	    }

	    @Override
	    public Fragment getItem(int position) {
	    	ListFragment fragment = new MyFragment();
	    	Bundle args = new Bundle();
	        // Our object is just an integer :-P
	        args.putInt(MyFragment.ARG_OBJECT, position + 1);
	        fragment.setArguments(args);

	        ListAdapter la;
	        if (position == 0) {
	        	la = new ArrayAdapter<String>(MainActivity.this,
	        			R.layout.fragment_collection_object, R.id.text1, procNames);
	        } else {
	        	la = new ArrayAdapter<String>(MainActivity.this,
	        			R.layout.fragment_collection_object, R.id.text1, apkNames);
	        }
	        fragment.setListAdapter(la);
	        return fragment;
	    }
	}

	// Instances of this class are fragments representing a single
	// object in our collection.
	public static class MyFragment extends ListFragment {
	    public static final String ARG_OBJECT = "object";

	    
	}

}


