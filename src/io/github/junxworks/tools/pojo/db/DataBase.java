package io.github.junxworks.tools.pojo.db;


import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import io.github.junxworks.tools.pojo.db.model.Table;

/**
 * 数据库操作类
 * 
 * @author levovo
 *
 */
public interface DataBase {
	/**
	 * 查询所有数据库表
	 * 
	 * @return
	 */
	public List<Table> queryAllTableNames(String schema, String tableName);

	/**
	 * 根据表名查询表详细信息
	 * 
	 * @param names
	 * 
	 * @return 表名
	 */
	public List<Table> getTables(String schema, List<String> tableNames);

	/**
	 * 关闭结果集
	 */
	public void closeResultSet(ResultSet rs);

	public void closeStatement(Statement statement);
}
