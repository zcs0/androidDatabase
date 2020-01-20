package com.data.db.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.z.format.json.FXJson;

import android.database.Cursor;

/**
 * @ClassName: SQLiteDBUtils.java
 * @author zcs
 * @version V1.0
 * @Date 2019年11月21日 下午12:33:54
 * @Modification 2019年11月21日 下午12:33:54
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class SQLiteDBUtils {
	private boolean isConnentSql=false;
	private static DBHelper dbHelper;
	private static long createTime = 0;
	public SQLiteDBUtils(String url,String user,String pass) {
		createTime = System.currentTimeMillis();
		try {
			System.out.println("------SQLiteDBUtils------");
			if(dbHelper==null||!dbHelper.isOpen() || !dbHelper.ping()) {
				dbHelper = new DBHelper(url, user, pass);
				isConnentSql = true;
				System.out.println("连接数据库");
			}else {
				isConnentSql = true;
			}
		} catch (Exception e) {
			dbHelper = null;
			System.out.println("连接数据库失败  "+e.toString());
//			e.printStackTrace();
		}
	}
	public long getCreateTime() {
		return createTime;
	}
	
	/**
	 * 数据库连接是否成功
	 * @return
	 */
	public boolean isConnentSql() {
		return isConnentSql && dbHelper !=null && dbHelper.isOpen() && dbHelper.ping();
	}

	/**
	 * 获取全部
	 * 
	 * @param table
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> getAll(String table) throws Exception {
		return querySQL(table, null, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param table
	 * @param selection     条件
	 * @param selectionArgs
	 * @return 限制条数
	 * @throws Exception 
	 */
	public List<FXJson> query(String table, String selection, String... selectionArgs) throws Exception {
		return querySQL(table, null, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * 
	 * @param table
	 * @param selection     条件
	 * @param selectionArgs 条件值
	 * @param orderBy       排序
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> query(String table, String selection, String[] selectionArgs, String orderBy) throws Exception {
		return querySQL(table, null, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * 
	 * @param table
	 * @param selection     条件
	 * @param selectionArgs 条件值
	 * @param orderBy       排序
	 * @param limit         限制条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> query(String table, String selection, String[] selectionArgs, String orderBy, String limit) throws Exception {
		return querySQL(table, null, selection, selectionArgs, null, null, orderBy, limit);
	}

	/**
	 * 
	 * @param table
	 * @param columns       列表
	 * @param selection     条件
	 * @param selectionArgs 条件值
	 * @param orderBy       排序
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> query(String table, String[] columns, String selection, String[] selectionArgs,
			String orderBy) throws Exception {
		return querySQL(table, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * 
	 * @param table
	 * @param columns       列表
	 * @param selection     条件
	 * @param selectionArgs 条件值
	 * @param orderBy       排序
	 * @param limit         限制条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy,
			String limit) throws Exception {
		return querySQL(table, columns, selection, selectionArgs, null, null, orderBy, limit);
	}

	/**
	 * 
	 * @param table
	 * @param orderBy 排序
	 * @param limit   限制条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> query(String table, String orderBy, String limit) throws Exception {
		return querySQL(table, null, null, null, null, null, orderBy, limit);
	}

	/**
	 * 
	 * @param table
	 * @param orderBy 排序
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> query(String table, String orderBy) throws Exception {
		return querySQL(table, null, null, null, null, null, orderBy, null);
	}

	/**
	 * 获取全部
	 * 
	 * @param table
	 * @param groupBy 组
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryGroupBy(String table, String groupBy) throws Exception {
		return querySQL(table, null, null, null, groupBy, null, null, null);
	}

	/**
	 * 获取全部
	 * 
	 * @param table
	 * @param orderBy 排序
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> getAllOrderBy(String table, String orderBy) throws Exception {
		return querySQL(table, null, null, null, null, null, orderBy, null);
	}

	/**
	 * 
	 * @param table
	 * @param orderBy 排序
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryOrderBy(String table, String orderBy) throws Exception {
		return querySQL(table, null, null, null, null, null, orderBy, null);
	}

	public List<FXJson> queryByFxJsonOrderBy(String table, FXJson json, String orderBy) throws Exception {
		return queryByFxJson(table, json, null, null, orderBy, null);
	}

	public List<FXJson> queryByFxJsonGroupByAndOrderBy(String table, FXJson json, String groupBy, String orderBy) throws Exception {
		return queryByFxJson(table, json, null, groupBy, orderBy, null);
	}

	public List<FXJson> queryFxjsonColumnsOrderBy(String table, FXJson json, String orderBy) throws Exception {
		return queryFxJsonByColumns(table, json, null, null, null, orderBy, null);
	}

	public List<FXJson> queryFxJsonByColumns(String table, FXJson json, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) throws Exception {
		if (json == null)
			return null;
		List<String> keys = json.getKeys();
		if (keys.size() > 0) {
			StringBuffer sb = new StringBuffer();
			String values[] = null;
			values = jsonParse(json, sb);
			String keyArr[] = new String[keys.size()];
			keys.toArray(keyArr);
			return querySQL(table, keyArr, sb.toString(), selectionArgs, groupBy, having, orderBy, limit);
		}
		return querySQL(table, null, null, null, null, null, null, null);

	}

	public List<FXJson> queryByColAndSelect(String table, String[] columns, String selection, String[] selectionArgs,
			String orderBy) throws Exception {
		return querySQL(table, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	public List<FXJson> queryByColAndSelect(String table, String[] columns, String selection, String[] selectionArgs) throws Exception {
		return querySQL(table, columns, selection, selectionArgs, null, null, null, null);
	}

	public List<FXJson> queryByFxJsonGroupBy(String table, FXJson json, String groupBy) throws Exception {
		return queryByFxJson(table, json, null, groupBy, null, null);
	}

	/**
	 * 查询表
	 * 
	 * @param table
	 * @param orderBy 排序
	 * @param i       条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryOrderBy(String table, String orderBy, int i) throws Exception {
		return querySQL(table, null, null, null, null, null, null, i + "");
	}

	/**
	 * 单条件查询
	 * 
	 * @param table
	 * @param orderBy
	 * @return
	 * @throws Exception 
	 */
	public FXJson queryOneOrderBy(String table, String orderBy) throws Exception {
		List<FXJson> query = querySQL(table, null, null, null, null, null, orderBy, null);
		if (query != null && query.size() > 0) {
			return query.get(0);
		} else {
			return null;
		}
	}

//	public FXJson queryRawOrderBy(String table, String orderBy) {
//		List<FXJson> query = query(table, null, null, null, null, null, orderBy, null);
//		if(query!=null&&query.size()>0) {
//			return query.get(0);
//		}else {
//			return null;
//		}
//	}
	/**
	 * 查询一条
	 * 
	 * @param table
	 * @param selection
	 * @param selectionArgs
	 * @return
	 * @throws Exception 
	 */
	public FXJson queryOne(String table, String selection, String... selectionArgs) throws Exception {
		List<FXJson> query = querySQL(table, null, selection, selectionArgs, null, null, null, null);
		if (query != null && query.size() > 0) {
			return query.get(0);
		} else {
			return null;
		}
	}
	
	public FXJson queryOne(String table,String[] columns, String selection, String... selectionArgs) throws Exception {
		List<FXJson> query = querySQL(table, columns, selection, selectionArgs, null, null, null, null);
		if (query != null && query.size() > 0) {
			return query.get(0);
		} else {
			return null;
		}
	}

//	public FXJson queryRaw(String table,String selection,String[] selectionArgs) {
//		List<FXJson> query = query(table, null, selection, selectionArgs, null, null, null, null);
//		if(query!=null&&query.size()>0) {
//			return query.get(0);
//		}else {
//			return null;
//		}
//	}
	/**
	 * 查询一条
	 * 
	 * @param table   表名
	 * @param orderBy 排序
	 * @param columns 列名
	 * @return
	 * @throws Exception 
	 */
	public FXJson queryOneOrderBy(String table, String orderBy, String[] columns) throws Exception {
		List<FXJson> query = querySQL(table, columns, null, null, null, null, orderBy, null);
		if (query != null && query.size() > 0) {
			return query.get(0);
		} else {
			return null;
		}
	}

//	public FXJson queryRawOrderBy(String table, String orderBy,String[] columns) {
//		List<FXJson> query = query(table, columns, null, null, null, null, orderBy, null);
//		if(query!=null&&query.size()>0) {
//			return query.get(0);
//		}else {
//			return null;
//		}
//	}
	/**
	 * {@link SQLiteDBUtils#queryOneByJson(String table, FXJson json, String orderBy)}
	 * 
	 * @param table
	 * @param json
	 * @return
	 * @throws Exception 
	 */
	public FXJson queryOneByJson(String table, FXJson json) throws Exception {
		return queryOneByJson(table, json, null);
	}

//	public FXJson queryRawByJson(String table, FXJson json) {
//		return queryRawByJson(table, json, null);
//	}
	/**
	 * 单条件查询
	 * 
	 * @param table   表名
	 * @param json    条件
	 * @param orderBy 排序
	 * @return
	 * @throws Exception 
	 */
	public FXJson queryOneByJson(String table, FXJson json, String orderBy) throws Exception {
		List<FXJson> query = queryByFxJson(table, json, null, null, orderBy, "1");
		if (query != null && query.size() > 0) {
			return query.get(0);
		} else {
			return null;
		}
	}
//	public FXJson queryRawByJson(String table, FXJson json, String orderBy) {
//		List<FXJson> query = queryByFxJson(table, json, null, null, orderBy,"1");
//		if(query!=null&&query.size()>0) {
//			return query.get(0);
//		}else {
//			return null;
//		}
//	}
//	public List<FXJson> queryRaw(String sql) {
//		return queryRaw(sql, null);
//	}
//	public List<FXJson> queryRaw(String sql,String[] columns) {
//		if(dbHelper==null || !dbHelper.isOpen())return null;
//		
//		Cursor query = null;
//		try {
//			query = sqlDB.rawQuery(sql, columns);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.e("sql: ", e.toString());
////			e.toString()
//			return null;
//		}
//		return queryByCursor(query);
//	}

	/**
	 * 
	 * @param table
	 * @param json    条件
	 * @param orderBy 排序
	 * @param i       返回的最大行数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryByJson(String table, FXJson json, String orderBy, int limit) throws Exception {
		return queryByFxJson(table, json, null, null, null, limit < 0 ? null : limit + "");
	}

	public List<FXJson> queryList(String table, FXJson json) throws Exception {
		return queryByFxJson(table, json, null, null, null, null);
	}

	public List<FXJson> queryList(String table, int limit) throws Exception {
		return querySQL(table, null, null, null, null, null, null, limit < 0 ? null : limit + "");
	}

	/**
	 * 
	 * @param table
	 * @param json  条数
	 * @param i     返回最大条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryByJson(String table, FXJson json, int limit) throws Exception {
		return queryByFxJson(table, json, null, null, null, limit < 0 ? null : limit + "");

	}

	public FXJson query(String table, FXJson json) throws Exception {
		List<FXJson> queryByFxJson = queryByFxJson(table, json, null, null, null, null);
		if (queryByFxJson == null)
			return null;
		return queryByFxJson.get(0);
//		sqlDB.rawQuery(sql, selectionArgs)
	}

//	public void execSQL(String sql) {
//		if(sqlDB!=null && sqlDB.isOpen()) {
//			sqlDB.execSQL(sql);
//		}
//	}
	
	public List<FXJson> queryByFxJson(String table, FXJson json,String limit) throws Exception {
		return queryByFxJson(table, json, null, null, null, null, limit);
	}
	/**
	 * 
	 * @param table 表名
	 * @param json 条件
	 * @param orderBy
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<FXJson> queryByFxJson(String table, FXJson json, String orderBy,String limit) throws Exception {
		return queryByFxJson(table, json, null, null, null, orderBy, limit);
	}
	public List<FXJson> queryByFxJson(String table,String[] columns, FXJson json, String orderBy,String limit) throws Exception {
		return queryByFxJson(table, json, columns, null, null, orderBy, limit);
	}
	public List<FXJson> queryLikeByFxJson(String table, FXJson json, String orderBy,String limit) throws Exception {
		return queryLikeByFxJson(table, json, null, null, null, orderBy, limit);
	}
	/**
	 * {@link SQLiteDBUtils#queryByFxJson(String table,FXJson json,String[] columns,String groupBy,String having,String orderBy,String limit)}
	 * 
	 * @param table
	 * @param json
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryByFxJson(String table, FXJson json, String groupBy, String having, String orderBy,
			String limit) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		String values[] = null;
//		values = jsonParse(json, sb);
		return queryByFxJson(table, json, null, groupBy, having, orderBy, limit);
	}

	/**
	 * 
	 * @param table   表名
	 * @param json    条件
	 * @param columns 字段
	 * @param groupBy 组
	 * @param having
	 * @param orderBy 排序
	 * @param limit   条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryByFxJson(String table, FXJson json, String[] columns, String groupBy, String having,
			String orderBy, String limit) throws Exception {
		StringBuffer sb = new StringBuffer();
		String values[] = null;
		values = jsonParse(json, sb);
		return querySQL(table, columns, sb.toString(), values, groupBy, having, orderBy, limit);
	}
	/**
	 * 
	 * @param table
	 * @param json 模糊搜索
	 * @param columns
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<FXJson> queryLikeByFxJson(String table, FXJson json, String[] columns, String groupBy, String having,
			String orderBy, String limit) throws Exception {
		StringBuffer sb = new StringBuffer();
		String values[] = null;
		values = jsonLikeOrParse(json, sb);
//		values = jsonParse(json, sb);
		return querySQL(table, columns, sb.toString(), values, groupBy, having, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryByFxJson(String table,FXJson json,String[] columns,String groupBy,String having,String orderBy,String limit)}
	 * @throws Exception 
	 */
	public List<FXJson> queryFxJsonAndColumns(String table, FXJson json, int limit, String... columns) throws Exception {
		return queryByFxJson(table, json, columns, null, null, null, limit < 0 ? null : limit + "");
	}

	/**
	 * {@link SQLiteDBUtils#queryByFxJson(String table,FXJson json,String[] columns,String groupBy,String having,String orderBy,String limit)}
	 * @throws Exception 
	 */
	public List<FXJson> queryFxJsonAndColumns(String table, FXJson json, String... columns) throws Exception {
		return queryByFxJson(table, json, columns, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryByFxJson(String table,FXJson json,String[] columns,String groupBy,String having,String orderBy,String limit)}
	 * @throws Exception 
	 */
	public List<FXJson> queryFxJsonOrderByAndColumns(String table, FXJson json, String orderBy, int limit,
			String... columns) throws Exception {
		return queryByFxJson(table, json, columns, null, null, orderBy, limit < 0 ? null : limit + "");
	}

	/**
	 * {@link SQLiteDBUtils#queryByFxJson(String table,FXJson json,String[] columns,String groupBy,String having,String orderBy,String limit)}
	 * @throws Exception 
	 */
	public List<FXJson> queryFxjsonOrderByAndColumns(String table, FXJson json, String orderBy, String... columns) throws Exception {
		return queryByFxJson(table, json, columns, null, null, orderBy, null);
	}

	public List<FXJson> queryColumns(String table, int limit, String... columns) throws Exception {
		return querySQL(table, columns, null, null, null, null, null, limit < 0 ? null : limit + "");
	}

	public List<FXJson> queryColumns(String table, String... columns) throws Exception {
		return querySQL(table, columns, null, null, null, null, null, null);
//		return queryColumns(table, columns, null, null, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String selection, String[] selectionArgs) throws Exception {
//		return query(table, columns, selection, selectionArgs, null, null, null, null);
		return queryColumns(table, columns, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String selection, String[] selectionArgs,
			String orderBy) throws Exception {
//		return query(table, columns, selection, selectionArgs, null, null, orderBy, null);
		return queryColumns(table, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String selection, String[] selectionArgs,
			String orderBy, int limit) throws Exception {
		return queryColumns(table, columns, selection, selectionArgs, null, null, orderBy, limit + "");
		// return query(table, columns, selection, selectionArgs, null, null, orderBy,
		// limit<0?null:limit+"");
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String orderBy, int limit) throws Exception {
		return queryColumns(table, columns, selection, selectionArgs, groupBy, null, orderBy, limit + "");
	}

	/**
	 * @param table         表名
	 * @param columns       字段列表
	 * @param selection     查询条件字段
	 * @param selectionArgs 查询条件
	 * @param groupBy       组
	 * @param orderBy       排序
	 * @param limit         条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String orderBy, String limit) throws Exception {
		return querySQL(table, columns, selection, selectionArgs, groupBy, null, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryLike(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 * @throws Exception 
	 */
	public List<FXJson> queryLike(String table, String[] selection, String[] selectionArgs) throws Exception {
		return queryLike(table, null, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryLike(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryLike(String table, String[] selection, String[] selectionArgs, String orderBy) throws Exception {
		return queryLike(table, null, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryLike(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryLike(String table, String[] selection, String[] selectionArgs, int limit) throws Exception {
		return queryLike(table, null, selection, selectionArgs, null, null, null, limit < 0 ? null : limit + "");
	}

	/**
	 * {@link SQLiteDBUtils#queryLike(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryLike(String table, String[] selection, String[] selectionArgs, String orderBy, int limit) throws Exception {
		return queryLike(table, null, selection, selectionArgs, null, null, orderBy, limit < 0 ? null : limit + "");
	}

	/**
	 * {@link SQLiteDBUtils#queryLike(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryLike(String table, String[] selection, String[] selectionArgs, String groupBy,
			String orderBy, int limit) throws Exception {
		return queryLike(table, null, selection, selectionArgs, groupBy, null, orderBy, limit < 0 ? null : limit + "");
	}

	/**
	 * {@link SQLiteDBUtils#queryLike(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryLike(String table, String[] selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, int limit) throws Exception {
		return queryLike(table, null, selection, selectionArgs, groupBy, having, orderBy,
				limit < 0 ? null : limit + "");
	}

	/**
	 * 模糊查询
	 * 
	 * @param table         表名
	 * @param columns       列名
	 * @param selection     条件字段
	 * @param selectionArgs 条件值
	 * @param groupBy       组
	 * @param having
	 * @param orderBy       排序
	 * @param limit         返回条数
	 * @return
	 * @throws Exception 
	 */
	public List<FXJson> queryLike(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) throws Exception {
		return querySQL(table, columns, getSelectionLike(selection), selectionArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> querySelection(String table, String[] selection, String[] selectionArgs) throws Exception {
		return queryColumns(table, null, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> querySelection(String table, String[] selection, String[] selectionArgs, String orderBy) throws Exception {
		return queryColumns(table, null, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs,
			String orderBy) throws Exception {
		return queryColumns(table, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs,
			String orderBy, String limit) throws Exception {
		return queryColumns(table, columns, selection, selectionArgs, null, null, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String orderBy, String limit) throws Exception {
		return queryColumns(table, columns, selection, selectionArgs, groupBy, null, orderBy, limit);
	}

	/**
	 * 默认条件为 where 为 or
	 * 
	 * @param table         表名
	 * @param columns       列名
	 * @param selection     条件表名
	 * @param selectionArgs 条件值
	 * @param groupBy       组
	 * @param having
	 * @param orderBy       排序
	 * @param limit         返回的条数
	 * @return
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) throws Exception {
		return querySQL(table, columns, getOrSelection(selection), selectionArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * 默认条件为 where 为 or
	 * 
	 * @param table         表名
	 * @param columns       列名
	 * @param selection     条件字段
	 * @param selectionArgs 条件值
	 * @param groupBy       组
	 * @param having
	 * @param orderBy       排序
	 * @param limit         返回的条数
	 * @return
	 */
	public List<FXJson> queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) throws Exception {
		return querySQL(table, columns, getAndSelection(selection), selectionArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> querySelectionAnd(String table, String[] selection, String[] selectionArgs) throws Exception {
		return queryColumnsAnd(table, null, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> querySelectionAnd(String table, String[] selection, String[] selectionArgs, String orderBy) throws Exception {
		return queryColumnsAnd(table, null, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs,
			String orderBy) throws Exception {
		return queryColumnsAnd(table, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs,
			String orderBy, String limit) throws Exception {
		return queryColumnsAnd(table, columns, selection, selectionArgs, null, null, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String orderBy, String limit) throws Exception {
		return queryColumnsAnd(table, columns, selection, selectionArgs, groupBy, null, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumnsAndOrderBy(String table, String orderBy, int limit, String... columns) throws Exception {
		return queryColumnsAnd(table, columns, null, null, null, null, orderBy, limit < 0 ? null : limit + "");
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public List<FXJson> queryColumnsAndOrderBy(String table, String orderBy, String... columns) throws Exception {
		return queryColumnsAnd(table, columns, null, null, null, null, orderBy, null);
	}

	/**
	 * 
	 * @param table         表名
	 * @param columns       列名
	 * @param selection     条件字段
	 * @param selectionArgs 条件值
	 * @param groupBy       组
	 * @param having
	 * @param orderBy       排序
	 * @param limit         返回的条数
	 * @return
	 */
	public List<FXJson> queryColumns(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) throws Exception {
		return querySQL(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOneAnd(String table, String[] columns) throws Exception {
		return queryColOneAnd(table, columns, null, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOneAnd(String table, String[] columns, String[] selection, String[] selectionArgs) throws Exception {
		return queryColOneAnd(table, columns, selection, selectionArgs, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOneAnd(String table, String[] columns, String[] selection, String[] selectionArgs,
			String orderBy) throws Exception {
		return queryColOneAnd(table, columns, selection, selectionArgs, null, null, orderBy);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOneAnd(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String orderBy) throws Exception {
		return queryColOneAnd(table, columns, selection, selectionArgs, groupBy, null, orderBy);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOne(String table, String[] columns) throws Exception {
		return queryColOne(table, columns, null, null, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOne(String table, String[] columns, String[] selection, String[] selectionArgs) throws Exception {
		return queryColOne(table, columns, selection, selectionArgs, null, null, null);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOne(String table, String[] columns, String[] selection, String[] selectionArgs,
			String orderBy) throws Exception {
		return queryColOne(table, columns, selection, selectionArgs, null, null, orderBy);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	public FXJson queryColOne(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String orderBy) throws Exception {
		return queryColOne(table, columns, selection, selectionArgs, groupBy, null, orderBy);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumns(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	private FXJson queryColOne(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) throws Exception {
		List<FXJson> queryColumns = queryColumns(table, columns, selection, selectionArgs, groupBy, having, orderBy,
				null);
		if (queryColumns == null || queryColumns.size() <= 0)
			return null;
		return queryColumns.get(0);
	}

	/**
	 * {@link SQLiteDBUtils#queryColumnsAnd(String table, String[] columns, String[] selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)}
	 */
	private FXJson queryColOneAnd(String table, String[] columns, String[] selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) throws Exception {
		List<FXJson> queryColumns = queryColumnsAnd(table, columns, selection, selectionArgs, groupBy, having, orderBy,
				"1");
		if (queryColumns == null || queryColumns.size() <= 0)
			return null;
		return queryColumns.get(0);
	}

	private String getSelectionLike(String[] columns) {
		if (columns == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (String string : columns) {
			sb.append(string + " like '%?%' ");
			sb.append(" or ");
		}
		String result = sb.toString();
		return result.substring(0, result.length() - 4);
	}

	private String getAndSelection(String[] columns) {
		if (columns == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (String string : columns) {
			sb.append(string + " = ? ");
			sb.append("AND ");
		}
		String result = sb.toString();
		return result.substring(0, result.length() - 4);
	}

	private String getOrSelection(String[] columns) {
		if (columns == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (String string : columns) {
			sb.append(string + " = ? ");
			sb.append("or ");
		}
		String result = sb.toString();
		return result.substring(0, result.length() - 4);
	}

	private String[] jsonLikeAndParse(FXJson json, StringBuffer sb) {
		String[] values = null;
		List<String> keys = json.getKeys();
		if (keys != null && keys.size() > 0) {
			values = new String[keys.size()];
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				if (sb != null) {
					sb.append(key + " like '%?%' ");
				}
				if (i < keys.size() - 1) {
					sb.append("or ");
				}
				values[i] = json.getStr(key);
			}
		}
		return values;
	}
	private String[] jsonLikeOrParse(FXJson json, StringBuffer sb) {
		String[] values = null;
		List<String> keys = json.getKeys();
		if (keys != null && keys.size() > 0) {
			values = new String[keys.size()];
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				if (sb != null) {
					sb.append(key + " like ? ");
				}
				if (i < keys.size() - 1) {
					sb.append("or ");
				}
				values[i] = json.getStr(key);
				values[i] = "%"+values[i] +"%";
				
			}
		}
		return values;
	}

	/**
	 * 
	 * @param json
	 * @param sb   key
	 * @return value 返回值的数组
	 */
	private String[] jsonParse(FXJson json, StringBuffer sb) {
		if (json == null)
			return null;
		if (sb == null)
			sb = new StringBuffer();
		String[] values = null;
//		Iterator<String> iterator = json.iterator();
		List<String> keys = json.getKeys();
		List<String> newKeys = new ArrayList<String>();
		List<String> valueArr = new ArrayList<String>();
		for (int i = keys.size()-1; i >=0 ; i--) {
			String key = keys.get(i);
			String value = json.getStr(key);
			if(value==null || value.length()<=0)continue;
			newKeys.add(key);
			sb.append(key + " = ? ");
			if(i>0 && newKeys.size()>0) {
				sb.append("AND ");
			}
			valueArr.add(value);
		}
		if(newKeys.size()>0) {
			values = new String[valueArr.size()];
			values = valueArr.toArray(values);
		}else {
			values = null;
		}
		return values;
	}

	private List<String> getJsonKey(FXJson json) {
		List<String> keys = json.getKeys();
		return keys;
	}

	/**
	 * Android 原始的查询方式 Query the given table, returning a {@link Cursor} over the
	 * result set.
	 *
	 * @param table         The table name to compile the query against.
	 * @param columns       A list of which columns to return. Passing null will
	 *                      return all columns, which is discouraged to prevent
	 *                      reading data from storage that isn't going to be used.
	 * @param selection     A filter declaring which rows to return, formatted as an
	 *                      SQL WHERE clause (excluding the WHERE itself). Passing
	 *                      null will return all rows for the given table.
	 * @param selectionArgs You may include ?s in selection, which will be replaced
	 *                      by the values from selectionArgs, in order that they
	 *                      appear in the selection. The values will be bound as
	 *                      Strings.
	 * @param groupBy       A filter declaring how to group rows, formatted as an
	 *                      SQL GROUP BY clause (excluding the GROUP BY itself).
	 *                      Passing null will cause the rows to not be grouped.
	 * @param having        A filter declare which row groups to include in the
	 *                      cursor, if row grouping is being used, formatted as an
	 *                      SQL HAVING clause (excluding the HAVING itself). Passing
	 *                      null will cause all row groups to be included, and is
	 *                      required when row grouping is not being used.
	 * @param orderBy       How to order the rows, formatted as an SQL ORDER BY
	 *                      clause (excluding the ORDER BY itself). Passing null
	 *                      will use the default sort order, which may be unordered.
	 * @param limit         Limits the number of rows returned by the query,
	 *                      formatted as LIMIT clause. Passing null denotes no LIMIT
	 *                      clause.
	 * @return A {@link Cursor} object, which is positioned before the first entry.
	 *         Note that {@link Cursor}s are not synchronized, see the documentation
	 *         for more details.
	 * @throws Exception 
	 * @see Cursor
	 */
	public List<FXJson> querySQL(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) throws Exception {
		if (dbHelper == null || !dbHelper.isOpen() || table == null)
			return null;
		StringBuffer sb = new StringBuffer();
		String select = "*";
		if (columns != null && columns.length > 0) {
			select = getSelectSql(columns);
		}
		sb.append("select ").append(select).append(" ");
//		boolean isSelect = false;
		sb.append("from ").append(table).append(" ");
		if (selection != null && selection.trim().length()>0 /* && selectionArgs != null && selectionArgs.length > 0 */) {
//			isSelect = true;
			sb.append("where ").append(selection).append(" ");
		}
		if (groupBy != null) {
			sb.append("GROUP BY").append(groupBy).append(" ");
		}

		if (having != null) {
			sb.append("HAVING ").append(having).append(" ");
		}
		if (orderBy != null) {
			sb.append("order by ").append(orderBy).append(" ");
		}
		if (limit != null) {
			sb.append("limit ").append(limit);
		}
		String sql = sb.toString();
		System.out.println("sql = "+sql);
		System.out.println("columns = "+columns);
		System.out.println("selectionArgs = "+selectionArgs);
		System.out.println("groupBy = "+groupBy);
		System.out.println("orderBy = "+orderBy);
		System.out.println("limit = "+limit);
		return querySQL(sql, selectionArgs);
//		try {
//			if (selectionArgs != null) {
//				executeQuery = dbHelper.executeQuery(sql, selectionArgs);
//			} else {
//				executeQuery = dbHelper.executeQuery(sql);
//			}
//			return queryByCursor(executeQuery);
//		} catch (Exception e) {
//			System.err.println(e.toString());
//			// TODO Auto-generated catch block
////			e.printStackTrace();
//		}
//		return null;
//		for (FXJson fxJson : fxList) {
//			System.out.println(fxJson.toString());
//		}
//		return null;
	}
	
	public List<FXJson> querySQL(String sql,Object...objects) throws Exception{
		ResultSet executeQuery;
		if (objects != null) {
			executeQuery = dbHelper.executeQuery(sql, objects);
		} else {
			
			executeQuery = dbHelper.executeQuery(sql);
		}
		return queryByCursor(executeQuery);
	}
	
	public int executSql(String sql) {
		if(sql==null)return -1;
		try {
			return dbHelper.executeSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	

	/**
	 * 只显示的字段名
	 * 
	 * @param columns
	 * @return
	 */
	private String getSelectSql(String[] columns) {
		if (columns == null || columns.length <= 0)
			return null;
		StringBuffer sb = new StringBuffer();
		for (String string : columns) {
			sb.append(string).append(",");
		}
		String sql = sb.toString();
		sql = sql.substring(0, sql.length() - 1);
		return sql;
	}

	public List<FXJson> queryByCursor(ResultSet query) throws SQLException {
		if (query == null)
			return null;
//		String[] columnNames = query.getColumnNames();

		if (query == null) {
			return null;
		}
		ResultSetMetaData metaData = query.getMetaData();
		if (metaData == null) {
			return null;
		}
		List<FXJson> list = new ArrayList<>();
		int columnCount = metaData.getColumnCount();
		while (query.next()) {
			FXJson child = new FXJson();
			for (int i = 1; i <= columnCount; i++) {
				String name = metaData.getColumnName(i);
				String value = query.getString(name);
//				System.out.println(name + "  "+value);
				child.put(name, value);
			}
			list.add(child);
		}

//		int count = query.getCount();
////		query.moveToFirst();
//		while(query.moveToNext()) {
//			FXJson child = new FXJson();
//			for (String key : columnNames) {
//				int columnIndex = query.getColumnIndex(key);
//				String value = query.getString(columnIndex);
//				child.put(key, value);
//			}
//			list.add(child);
//		}
//		query.close();
//		if(list.size()<=0)return null;
		return list;
	}

//	/**
//	 * 
//	 * @param table 表名
//	 * @param json 要更新的值
//	 * @param whereJson 条件
//	 * @return
//	 */
//	public int update(String table,FXJson json,FXJson whereJson) {
//		ContentValues contentValues = jsonToCV(json);
//		StringBuffer sb = new StringBuffer();
////		String values[] = null;
////		values = jsonParse(json, null, values);
////		if(values==null)return -1;
//		String[] whereClause = jsonParse(whereJson, sb);
//		return sqlDB.update(table, contentValues, sb.toString(), whereClause);
//	}
//	public int update(String table,FXJson json,String[] whereJson,String[] whereArg) {
//		StringBuffer sb = new StringBuffer();
//		return sqlDB.update(table, contentValues, sb.toString(), whereArg);
//	}
//	/**
//	 * 
//	 * @param table
//	 * @param json
//	 * @param whereClause
//	 * @param whereArgs
//	 * @return
//	 */
//	public int update(String table,FXJson json,String whereClause,String[] whereArgs) {
////		StringBuffer sb = new StringBuffer();
////		String values[] = null;
////		values = jsonParse(json, sb, values);
////		if(values==null)return -1;
//		ContentValues contentValues = jsonToCV(json);
//		if(contentValues==null)return -1;
//		return update(table, contentValues, whereClause, whereArgs);
//				
////		return sqlDB.update(table, contentValues, whereClause, whereArgs);
//	}
//	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
//		return sqlDB.update(table, values, whereClause, whereArgs);
//	}
//	/**
//	 * 替换/新增一条
//	 * @param table
//	 * @param json
//	 * @param nullColumnHack 替换条件，字段名
//	 * @return
//	 */
//	public long replace(String table,FXJson json,String nullColumnHack) {
//		ContentValues contentValues = jsonToCV(json);
//		if(contentValues==null)return -1;
//		return sqlDB.replace(table, nullColumnHack, contentValues);
//	}
//	public long insert(String table,List<FXJson> json) {
//		if(json==null || sqlDB==null || !sqlDB.isOpen())return -1;
//		long count = 0;
//		for (FXJson fxJson : json) {
//			count += insert(table, fxJson);
//		}
//		return count;
//	}
//	/**
//	 * 
//	 * @param table
//	 * @param json
//	 * @param type 1:增加，2：删除，3：改
//	 * @return
//	 * @throws Exception 
//	 */
//	private long update(String table,FXJson json,int type) throws Exception {
//		if(dbHelper==null || !dbHelper.isOpen() || table==null)return -1;
//		StringBuffer sb = new StringBuffer();
//		List<String> keys = json.getKeys();
//		StringBuffer keySql = new StringBuffer();
//		StringBuffer valueSql = new StringBuffer();
//		String[] values = new String[keys.size()];
//		for (int i = 0; i < values.length; i++) {
//			String key = keys.get(i);
//			keySql.append(key).append(",");
//			values[i] = json.getStr(key);
//		}
////		for (String string : keys) {
////			keySql.append(string).append(",");
////			valueSql.append("'"+json.getStr(string)+"'").append(",");
////			values
////		}
//		String keyStr = keySql.toString().substring(0, keySql.length()-1);
//		String valueStr = valueSql.toString().substring(0, valueSql.length()-1);
//		
//		update(table, keySql.toString(), values, null, type);
//		
////		if(type==1) {//插入一条
////			//INSERT INTO 表名称 VALUES (值1, 值2,....)
////			//INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
////			 sb.append("instert into ").append(table).append(" ");
////			 sb.append("(").append(keyStr).append(")");
////			 sb.append("values (").append(valueStr).append(")");
////			 String sql = sb.toString();
////			 return dbHelper.executeUpdate(sql);
////		}else if(type == 2) {//删除
////			//DELETE FROM area_table WHERE district = 'www'
////		}else if(type == 3) {//改
////			//UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
////			//UPDATE Person SET FirstName = 'Fred' WHERE LastName = 'Wilson' 
////		}
//		return -1;
//	}
	public int delete(String table, FXJson json) throws Exception {
		if (dbHelper == null || !dbHelper.isOpen() || table == null)
			return -1;
//		StringBuffer sb = new StringBuffer();
		List<String> keys = json.getKeys();
		StringBuffer keySql = new StringBuffer();
//		StringBuffer valueSql = new StringBuffer();
		String[] values = new String[keys.size()];
		for (int i = 0; i < values.length; i++) {
			String key = keys.get(i);
			keySql.append(key).append("=? and ");
			values[i] = json.getStr(key);
		}
		String keyStr = keySql.toString().substring(0, keySql.length() - 4);
//		String valueStr = valueSql.toString().substring(0, valueSql.length() - 1);
		// DELETE FROM area_table WHERE district = 'www'
//		update(table, null, values, values, type);
		return delete(table, keyStr, values);
//		return delete(table, keyStr, values, 2);
//		return -1;
	}
	
	public int delete(String table, String where,String[] whereArgs) throws Exception {
		return update(table, where, whereArgs, null, 2);
	}

	/**
	 * 更新表
	 * 
	 * @param table
	 * @param json
	 * @param where
	 * @param whereArgs
	 * @return
	 * @throws Exception 
	 */
	public int update(String table, FXJson json, String where, String[] whereArgs) throws Exception {
		// 更新
		if (dbHelper == null || !dbHelper.isOpen() || table == null || json == null)
			return -1;
		List<String> keys = json.getKeys();
		String[] values = new String[keys.size()];
		String[] keysArr = new String[keys.size()];
		for (int i = 0; i < values.length; i++) {
			keysArr[i] = keys.get(i);
			values[i] = json.getStr(keysArr[i]);
		}
		return update(table, keysArr, values, where, whereArgs);
//		return -1;
	}

	/**
	 * 更新表
	 * 
	 * @param table     表名
	 * @param select    字段名
	 * @param selectArg 要被修改字段名的值
	 * @param where     条件
	 * @param whereArgs 条件值
	 * @return
	 * @throws Exception 
	 */
	public int update(String table, String[] select, String[] selectArg, String where, String[] whereArgs) throws Exception {
		if (table == null || select == null || select.length <= 0 || selectArg == null || selectArg.length <= 0)
			return -1;
		// UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
		// UPDATE Person SET FirstName = 'Fred' WHERE LastName = 'Wilson'
//		update(table, sql, whereArgs, where, 3);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < select.length && i < selectArg.length; i++) {
			sb.append(select[i] + " = '" + selectArg[i] + "'").append(",");
		}
		String keyStr = sb.toString().substring(0, sb.length() - 1) + " ";
		return update(table, keyStr, whereArgs, where, 3);
//		return -1;
	}

	public int insert(String table, List<FXJson> json) throws Exception {
		int count = 0;
		for (FXJson fxJson : json) {
			int insert = insert(table, fxJson);
			if (insert > 0) {
				count++;
			}
		}
		return count;

	}

	public int insert(String table, FXJson json) throws Exception {
		if (dbHelper == null || !dbHelper.isOpen() || table == null || json == null)
			return -1;
		List<String> keys = json.getKeys();
		List<String> keyArr = new ArrayList<String>();//json.getKeys();
		for (String key : keys) {
			String value = json.getStr(key);
			if(value==null || value.trim().length()<=0) {
				continue;
			}
			keyArr.add(key);
		}
		String[] values = new String[keyArr.size()];
//		String[] keysArr = new String[keys.size()];
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < values.length; i++) {
			String key = keyArr.get(i);
			String value = json.getStr(key);
			values[i] = value;
			sb.append(key).append(",");
			
		}
		String keyStr = sb.toString().substring(0, sb.length() - 1);
		
//		keyStr = "(" + keyStr + ")";
//		where = "(" + where + ")";
//		keyStr = keyStr + " values " + where;

		// INSERT INTO 表名称 VALUES (值1, 值2,....)
		// INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)

		return insert(table, keyStr, values);

//		return -1;
	}
	/**
	 * 
	 * @param table
	 * @param keyArr (key,key2,key3)
	 * @param values value
	 * @return
	 * @throws Exception
	 */
	public int insert(String table, String keyArr, String... values) throws Exception {
		String sele = "("+keyArr+")";
		StringBuffer whereSb = new StringBuffer();
		for (String string : values) {
			whereSb.append("?").append(",");
		}
		String where = whereSb.toString().substring(0, whereSb.length() - 1);
		sele = sele +" values "+ "("+where+")";
		return update(table, sele, values, null, 1);
	}

	/**
	 * 
	 * @param table  表名
	 * @param select 如果是修改 set
	 * @param values 值
	 * @param where  修改时的where
	 * @param type 操作类型
	 * @return
	 * @throws Exception 
	 */
	private int update(String table, String select, String[] values, String where, int type) throws Exception {
		if (dbHelper == null || !dbHelper.isOpen() || table == null)
			return -1;
		if (type > 3 || type < 1)
			return -1;
		StringBuffer sb = new StringBuffer();
		switch (type) {
		case 1:// 插入
				// INSERT INTO 表名称 VALUES (值1, 值2,....)
				// INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
			sb.append("insert into ").append(table).append(" ").append(select).append(" ");
			break;
		case 2:// 删除
				// DELETE FROM area_table WHERE district = 'www'
			sb.append("delete from ").append(table).append(" where ").append(select).append(" ");
			break;
		case 3:// 改，测试过
				// UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
				// UPDATE Person SET FirstName = 'Fred' WHERE LastName = 'Wilson'
			if (where != null) {
				sb.append("update ").append(table).append(" set ").append(select).append(" where ").append(where);
			} else {
				sb.append("update ").append(table).append(" set ").append(select);
			}
			break;

		default:
			break;
		}
		String sql = sb.toString();
		return update(sql, values);
//		return -1;
	}

	public int update(String sql, String[] values) throws Exception {
		System.out.println("update sql = "+sql);
		if (values != null && values.length > 0) {
			return dbHelper.executeUpdate(sql, values);
		} else {
			return dbHelper.executeUpdate(sql);
		}
	}
	public FXJson queryCount(String table,String select, String[] args) throws Exception {
		//SELECT COUNT(*) AS count from device_list WHERE user_id = 10
		String sql = "SELECT COUNT(*) AS count from "+ table;
		if(select!=null) {
			sql = sql + " where "+ select;
		}
		return queryCount(sql, args);
	}
	public FXJson queryCount(String table,FXJson json) throws Exception {
		String sql = "SELECT COUNT(*) AS count from "+ table;
		String values[] = null;
		if(json != null) {
			StringBuffer sb = new StringBuffer();
			values = jsonParse(json, sb);
			sql = "SELECT COUNT(*) AS count from "+ table + " where "+ sb.toString();
		}
		return queryCount(sql, values);
		
//		return querySQL(table, columns, sb.toString(), values, null, null, null, limit);
		
		//SELECT COUNT(*) AS count from device_list WHERE user_id = 10
//		String sql = "SELECT COUNT(*) AS count from "+ table + " where "+ select;
//		return queryCount(sql, args);
	}
	/**
	 * 统计数量
	 * @param sql
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public FXJson queryCount(String sql, String[] args) throws Exception {
		if (args != null && args.length > 0) {
			ResultSet executeQuery = dbHelper.executeQuery(sql, args);
			List<FXJson> queryByCursor = queryByCursor(executeQuery);
			if(queryByCursor!=null && queryByCursor.size()>0) {
				return queryByCursor.get(0);
			}
		} else {
			ResultSet executeQuery = dbHelper.executeQuery(sql);
			List<FXJson> queryByCursor = queryByCursor(executeQuery);
			if(queryByCursor!=null && queryByCursor.size()>0) {
				return queryByCursor.get(0);
			}
		}
		return null;
	}
	
	
	
	/**
	 * 关闭数据库
	 */
	public void close(){
		if(dbHelper!=null) {
			dbHelper.close();
			dbHelper = null;
		}
	}

//	/**
//	 * 删除表
//	 * @param table
//	 */
//	public void deleteTable(String table) {
//		if(table==null || sqlDB==null || !sqlDB.isOpen())return;
//		sqlDB.execSQL("drop table "+table);
//	}
//	/**
//	 * 清空表数据
//	 * @param table
//	 */
//	public void deleteAll(String table) {
//		//db.execSQL("drop table tab_name"); 删除表
//		//db.execSQL("delete from tab_name"); 清空表
//		if(table==null || sqlDB==null || !sqlDB.isOpen())return ;
//		sqlDB.execSQL("delete from "+table);
//	}
//	public int delete(String table,FXJson json) {
//		if(json==null || sqlDB==null || !sqlDB.isOpen())return -1;
//		StringBuffer sb = new StringBuffer();
//		String[] jsonParse = jsonParse(json, sb);
//		return sqlDB.delete(table, sb.toString(), jsonParse);
//	}
//	public ContentValues jsonToCV(FXJson json) {
//		if(json == null) return null;
//		List<String> jsonKey = getJsonKey(json);//所有的key
//		if(jsonKey==null||jsonKey.size()<=0)return null;
//		ContentValues contentValues = jsonToCV(json,jsonKey);
//		return contentValues;
//	}
//	private ContentValues jsonToCV(FXJson json, List<String> keys) {
//		ContentValues contentValues = new ContentValues();
//		for (String key : keys) {
//			if(key == null) continue;
//			String value = json.getStr(key);
//			//如果没有值，=后可能会出错
////			if(value != null && (value.trim().length()>0)) {
//				contentValues.put(key, value);
////			}
//		}
//		return contentValues;
//	}
//	/**
//	 * 
//	 * @param json
//	 * @param keys key
//	 * @return
//	 * @deprecated
//	 */
//	private ContentValues jsonToCV(FXJson json, String[] keys) {
//		ContentValues contentValues = new ContentValues();
//		for (String key : keys) {
//			String value = json.getStr(key);
//			contentValues.put(key, value);
//		}
//		return contentValues;
//	}
//	public void close() {
//		if(sqlDB!=null) {
//			sqlDB.close();
//			sqlDB = null;
//		}
//	}
//	public void beginTransaction() {
//		if(sqlDB==null)return;
//		sqlDB.beginTransaction();
//	}
//	public void setTransactionSuccessful() {
//		if(sqlDB==null)return;
//		sqlDB.setTransactionSuccessful();
//	}
//	public void endTransaction() {
//		if(sqlDB==null)return;
//		sqlDB.endTransaction();
//	}
//	public boolean isOpen() {
//		return sqlDB==null?false:sqlDB.isOpen();
//	}
	public static void writeByteFile(File file, byte[] btyes) throws IOException {
		if (file == null)
			return;
		if (file.isFile()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream localFileOutputStream = new FileOutputStream(file);
		localFileOutputStream.write(btyes);
		localFileOutputStream.flush();
		localFileOutputStream.close();// 保存加密后的文件
	}

//	private void copyDatabase(Context context,File saveFile,String name,boolean isEncrypt) {
//		if(isEncrypt) {//如是有加密
//			try {
//				InputStream stream = context.getAssets().open(name);
//				byte[] bytes = FileUtils.getByteByStream(stream);
//				byte[] fileDecrypt = FileUtils.fileDecrypt(bytes);
//				FileUtils.writeByteFile(saveFile, fileDecrypt);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}else {
//			try {
//				InputStream stream = context.getAssets().open(name);
//				FileUtils.writeFile(saveFile, stream);
//				if(stream!=null)
//					stream.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//        
//    }
//	/**
//     * 判断assets文件夹下的文件是否存在
//     *
//     * @return false 不存在    true 存在
//     */
//    private static boolean isFileExists(String filename,Context cxt) {
//    	AssetManager assetManager = cxt.getResources().getAssets();
//        try {
//            String[] names = assetManager.list("");
//            for (int i = 0; i < names.length; i++) {
//                if (names[i].equals(filename.trim())) {
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
	/**
	 * 把文件转成byte[]
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] getFileToByte(String filePath) throws IOException {
		InputStream inputStream = new FileInputStream(filePath);
		return getByteByStream(inputStream);
	}

	/**
	 * 把文件转成byte[]
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteByStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream dexByteArrayOutputStream = new ByteArrayOutputStream();
		byte[] arrayOfByte = new byte[1024];
		while (true) {
			int read = inputStream.read(arrayOfByte);
//             int i = localZipInputStream.read(arrayOfByte);
			if (read == -1)
				break;
			dexByteArrayOutputStream.write(arrayOfByte, 0, read);
		}
		inputStream.close();
		dexByteArrayOutputStream.close();
		return dexByteArrayOutputStream.toByteArray();
	}

}
