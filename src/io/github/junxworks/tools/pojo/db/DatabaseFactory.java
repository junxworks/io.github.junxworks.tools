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
		if(de.getType().toLowerCase().contains("oracle")){
            return new OracleDatabase(de);
        } else if(de.getType().toLowerCase().contains("sql server")){
            return new SqlServerDatabase(de);
        } else if(de.getType().toLowerCase().contains("mysql")){
            return new MySqlDatabase(de);
        } 
		return null;
	}

}
