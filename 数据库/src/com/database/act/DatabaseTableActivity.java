package com.database.act;

import java.util.List;

import com.base.BaseActivity;
import com.data.db.utils.SQLiteDBUtils;
import com.database.R;
import com.z.format.json.FXJson;
import com.z.view.utils.ViewById;
import com.z.view.utils.ZActivity;
import com.z.view.utils.ZException;

import android.os.Bundle;
import android.widget.ListView;

/**
 * @ClassName:     DatabaseTableActivity.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2020年1月20日 上午10:51:47
 * @Modification   2020年1月20日 上午10:51:47 
 * @Description:   数据库的基本信息
 */
@ZActivity(R.layout.activity_database_table_layout)
public class DatabaseTableActivity extends BaseActivity{
	
	@ViewById(R.id.lv_view)
	ListView lv_view;
	
	@Override
	protected void onCreateView(Bundle arg0) {
		try {
			regeditActivity();
		} catch (ZException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "jdbc:mysql://192.168.52.128:3306/movie_db?useUnicode=true&characterEncoding=utf-8&useSSL=true";
		String user = "root";
		String pass = "!Z1234578Ca#";
		
		SQLiteDBUtils utils = new SQLiteDBUtils(url, user, pass);
		
		try {
			List<FXJson> querySQL = utils.querySQL("show databases",null);
			System.out.println(querySQL.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
