package io.github.junxworks.tools.pojo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import io.github.junxworks.tools.pojo.db.model.DatabaseElement;
import io.github.junxworks.tools.pojo.db.model.Table;
import io.github.junxworks.tools.pojo.db.utils.StringUtil;

public class MySqlDatabase extends DataBseDefault {

	public MySqlDatabase(DatabaseElement de) {
		super(de);
	}

	@Override
	public List<Table> getTables(String schema, List<String> tableNames) {

		List<Table> tables = super.getTables(schema, tableNames);
		return tables;
	}

	@Override
	public List<Table> queryAllTableNames(String schema, String tableName) {
		String sql = "select table_name,table_comment from information_schema.tables where table_schema=? and table_type='base table'";
		if (!StringUtil.isEmpty(tableName)) {
			sql += " and table_name LIKE '" + tableName + "%'";
		}
		
		List<Table> res = new ArrayList<Table>();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConn();
			psmt = con.prepareStatement(sql);
			psmt.setObject(1, schema);
			rs = psmt.executeQuery();
			while (rs.next()) {
				Table table = new Table();
				table.setTableName(rs.getString("table_name"));
				table.setTableComment(rs.getString("table_comment"));
				res.add(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(psmt);
			closeConn(con);
		}
		return res;
	}

}
