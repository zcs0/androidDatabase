package com.database;

import com.base.BaseActivity;
import com.z.view.utils.ZActivity;
import com.z.view.utils.ZException;

import android.os.Bundle;

@ZActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreateView(Bundle arg0) {
		try {
			regeditActivity();
		} catch (ZException e) {
			e.printStackTrace();
		}
	}
	
	
	

}
