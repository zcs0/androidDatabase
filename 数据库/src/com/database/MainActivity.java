package com.database;

import com.base.BaseActivity;
import com.database.act.DatabaseTableActivity;
import com.z.view.utils.ZActivity;
import com.z.view.utils.ZException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

@ZActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreateView(Bundle arg0) {
		try {
			regeditActivity();
		} catch (ZException e) {
			e.printStackTrace();
		}
		
		findView(R.id.btn_open).setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_open:
			Intent inten = new Intent(this, DatabaseTableActivity.class);
			startActivity(inten);
			break;

		default:
			break;
		}
	}
	
	
	

}
