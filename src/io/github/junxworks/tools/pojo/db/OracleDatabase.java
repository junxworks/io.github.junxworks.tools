package io.github.junxworks.tools.pojo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.junxworks.tools.pojo.db.model.DatabaseElement;
import io.github.junxworks.tools.pojo.db.model.Table;
import io.github.junxworks.tools.pojo.db.utils.StringUtil;

public class OracleDatabase extends DataBseDefault {

	// private static final String TABLE_COMMENTS_SQL = "select * from
	// user_tab_comments where TABLE_NAME=?";

	// private static final String COLUMN_COMMENTS_SQL = "select * from
	// user_col_comments where TABLE_NAME=?";

	public OracleDatabase(DatabaseElement de) {
		super(de);
	}

	@Override
	public List<Table> getTables(String schema, List<String> tableNames) {
		List<Table> tables = super.getTables(schema, tableNames);
		return tables;
	}

	@Override
	public List<Table> queryAllTableNames(String schema, String tableName) {
		String sql = "SELECT T.TABLE_NAME, " + "(SELECT COMMENTS FROM user_tab_comments V"
				+ " WHERE V.TABLE_NAME = T.TABLE_NAME) AS COMMENTS " + " FROM ALL_TABLES T   WHERE T.OWNER= ? ";
		if (!StringUtil.isEmpty(tableName)) {
			sql += " AND T.TABLE_NAME LIKE '" + tableName + "%'";
		}
		List<Table> ts = new ArrayList<Table>();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConn();
			psmt = con.prepareStatement(sql);
			psmt.setString(1, schema);
			rs = psmt.executeQuery();
			while (rs.next()) {
				Table table = new Table();
				table.setTableName(rs.getString("TABLE_NAME"));
				table.setTableComment(rs.getString("COMMENTS"));
				ts.add(table);
			}
		} catch (Exception e) {

		} finally {
			closeResultSet(rs);
			closeStatement(psmt);
			closeConn(con);
		}
		return ts;
	}
}
