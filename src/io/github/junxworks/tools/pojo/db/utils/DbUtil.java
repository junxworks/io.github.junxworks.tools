package io.github.junxworks.tools.pojo.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	/**
	 * 判断数据库链接是否链接成功
	 * 
	 * @param type
	 * @param url
	 * @param userNama
	 * @param password
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean isConnection(String type, String url, String userName, String password) throws Exception {
		Connection con = getConnection(type, url, userName, password);
		if (null != con) {
			con.close();
			return true;
		}
		return false;

	}

	/**
	 * 创建数据库链接
	 * 
	 * @param type
	 * @param url
	 * @param userNama
	 * @param password
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection(String type, String url, String userNama, String password) throws Exception {
		Connection con = null;
		String className = "";
		if (type.toLowerCase().contains("oracle")) {
			className = "oracle.jdbc.driver.OracleDriver";
		} else if (type.toLowerCase().contains("sql server")) {
			className = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		} else if (type.toLowerCase().contains("mysql")) {
			className = "com.mysql.cj.jdbc.Driver";
		} else {
			throw new Exception("暂不支持数据库类型:" + type);
		}
		try {
			Class.forName(className);
			con = DriverManager.getConnection(url, userNama, password);
		} catch (ClassNotFoundException cException) {
			throw new Exception("缺少" + type + "数据库驱动类");
		} catch (SQLException sException) {
			throw new Exception("创建数据库连接失败");
		}

		return con;
	}
	
}
