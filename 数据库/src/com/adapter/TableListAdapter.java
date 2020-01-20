package com.adapter;
/**
 * @ClassName:     BaseAdapter.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2020年1月20日 上午10:59:49
 * @Modification   2020年1月20日 上午10:59:49 
 * @Description:   TODO(用一句话描述该文件做什么) 
 */

import com.base.BaseAdapter;
import com.database.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TableListAdapter extends BaseAdapter{

	@Override
	public View createView(int arg0, View arg1, ViewGroup arg2) {
		View view = View.inflate(arg2.getContext(), R.layout.datapter_table_list_layout, null);
		return view;
	}

	@Override
	public void initView(int arg0, View arg1, ViewGroup arg2) {
		TextView tvName = findViewById(R.id.tv_name);
		
		
	}

}
