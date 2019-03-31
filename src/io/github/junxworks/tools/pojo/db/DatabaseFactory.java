package io.github.junxworks.tools.pojo.db;

import java.sql.Connection;

import io.github.junxworks.tools.pojo.db.model.DatabaseElement;
import io.github.junxworks.tools.pojo.db.utils.DbUtil;

public class DatabaseFactory {
	/**
	 * 创建数据库操作对象
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static DataBase creatDataBase(DatabaseElement de ) throws Exception {
		Connection con = DbUtil.getConnection(de.getType(), de.getUrl(), de.getUsername(), de.getPassword());
		if(de.getType().toLowerCase().contains("oracle")){
            return new OracleDatabase(con);
        } else if(de.getType().toLowerCase().contains("sql server")){
            return new SqlServerDatabase(con);
        } else if(de.getType().toLowerCase().contains("mysql")){
            return new MySqlDatabase(con);
        } 
		return null;
	}

}
