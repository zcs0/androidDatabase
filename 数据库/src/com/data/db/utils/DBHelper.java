package com.data.db.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executor;

import com.mysql.cj.jdbc.ConnectionImpl;
/**
 * @ClassName:     DBHelper.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2019年11月20日 上午10:39:15
 * @Modification   2019年11月20日 上午10:39:15 
 * @Description:   TODO(用一句话描述该文件做什么) 
 */
public class DBHelper implements Executor {
    /**
     * 数据库URL
     */
    private static String URL = "jdbc:mysql://192.168.52.128:3306/movie_db?useUnicode=true&characterEncoding=utf-8&useSSL=true";
    /**
     * 登录用户名
     */
    private static String USER = "root";
    /**
     * 登录密码
     */
    private static String PASSWORD = "!Z1234578Ca#";
    
    private static Connection connection = null;
    private static Statement statement = null;

    private static DBHelper helper = null;
    private static boolean isSuccess = false;

    static {
        try {
            Class<?> forName = Class.forName("com.mysql.jdbc.Driver");
            isSuccess = true;
            System.out.println(forName);
        } catch (ClassNotFoundException e) {
        	isSuccess = false;
            e.printStackTrace();
        }
    }

    public DBHelper(String url,String user,String pass) throws Exception {
//    	URL = "jdbc:mysql://192.168.52.128:3306/movie_db?useUnicode=true&characterEncoding=utf-8&useSSL=true";
    	//Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/XX","root","XXXX")
//        int loginTimeout = DriverManager.getLoginTimeout();
//        int networkTimeout = connection.getNetworkTimeout();
    	DriverManager.setLoginTimeout(30);
        connection = DriverManager.getConnection(url, user, pass);
//    	connection.setNetworkTimeout(this , 1*60*1000);//1分钟
        statement = connection.createStatement();
    }
    private DBHelper() throws Exception {
//		URL = "jdbc:mysql://192.168.52.128:3306/movie_db?useUnicode=true&characterEncoding=utf-8&useSSL=true";
//		URL = "jdbc:mysql://192.168.52.128:3306/movie_db?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
//		URL = "jdbc:mysql://localhost:3306/movie_db?useUnicode=true&characterEncoding=utf8";
//    	URL = "jdbc:mysql://192.168.52.128:3306/movie_db?useUnicode=true&characterEncoding=utf-8&useSSL=true";
    	//Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/XX","root","XXXX")
    	DriverManager.setLoginTimeout(5);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
//        connection.setNetworkTimeout(this , 5*1000);
        statement = connection.createStatement();
    }

    /**
     * 返回单例模式的数据库辅助对象
     * 
     * @return
     * @throws Exception 
     */
    public static DBHelper getDbHelper() throws Exception {
    	if(!isSuccess) {
    		throw new RuntimeException("conntent error,check username or pass or url");
    	}
        if (helper == null || connection == null || connection.isClosed())
            helper = new DBHelper();
        return helper;
    }
    public static DBHelper getDbHelper(String url,String user,String pass) throws Exception {
    	if(!isSuccess) {
    		throw new RuntimeException("conntent error,check username or pass or url");
    	}
    	return new DBHelper(url, user, pass);
    }

    /**
     * 执行查询
     * @param sql 要执行的SQL语句
     * @return  查询的结果集对象
     * @throws Exception
     */
    public ResultSet executeQuery(String sql) throws Exception {
        if (statement != null) {
        	
            return statement.executeQuery(sql);
        }

        throw new Exception("数据库未正常连接");
    }
    
    /**
     * 返回受影响的条数
     * @param sql
     * @return
     * @throws SQLException
     */
    public int executeSql(String sql) throws SQLException {
    	boolean execut=  statement.execute(sql);
    	return statement.getUpdateCount();
    }
    public boolean executeSql(String sql,String[] arg) throws SQLException {
    	return statement.execute(sql,arg);
    }

    /**
     * 执行查询
     * @param sql  要执行的带参数的SQL语句
     * @param args  SQL语句中的参数值
     * @return  查询的结果集对象
     * @throws Exception
     */
    public ResultSet executeQuery(String sql, Object...args) throws Exception {
        if (connection == null || connection.isClosed()) {
            DBHelper.close();
            throw new Exception("数据库未正常连接");
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        int index = 1;
        for (Object arg : args) {
            ps.setObject(index, arg);
            index++;
        }
        
        return ps.executeQuery();
    }
    
    /**
     * 执行更新(增删改)
     * @param sql  要执行的SQL语句
     * @return  受影响的记录条数
     * @throws Exception
     */
    public int executeUpdate(String sql) throws Exception {
        if (statement != null) {
            return statement.executeUpdate(sql);
        }
        throw new Exception("数据库未正常连接");
    }
    
    /**
     * 执行更新
     * @param sql  要执行的SQL语句
     * @param args  SQL语句中的参数
     * @return  受影响的记录条数
     * @throws Exception
     */
    public int executeUpdate(String sql, Object...args) throws Exception {
        if (connection == null || connection.isClosed()) {
            DBHelper.close();
            throw new Exception("数据库未正常连接");
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        int index = 1;
        for (Object arg : args) {
            ps.setObject(index, arg);
            index++;
        }
        return ps.executeUpdate();
    }
    
    /**
     * 获取预编译的语句对象
     * @param sql  预编译的语句
     * @return  预编译的语句对象
     * @throws Exception
     */
    public PreparedStatement prepareStatement(String sql) throws Exception {
        return connection.prepareStatement(sql);
    }
    
    /**
     * 关闭对象，同时将关闭连接
     */
    public static void close() {
        try {
            if (statement != null)
                statement.close();
            if (connection != null) 
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper = null;
        }
    }

    public boolean ping() {
    	if(connection!=null) {
    		com.mysql.cj.jdbc.ConnectionImpl impl = (ConnectionImpl) connection;
//    		impl.checkClosed();
    		try {
				impl.ping();
				return true;
			} catch (SQLException e) {
//				e.printStackTrace();
				return false;
			}
    	}
    	return false;
    }
    
	public boolean isOpen() {
		if(statement!=null && connection!=null) {
//			statement.setQueryTimeout(seconds);
			try {
				return !statement.isClosed() && !connection.isClosed();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	@Override
	public void execute(Runnable command) {
//		System.out.println("连接网络超时");
//		close();
		
	}
}
