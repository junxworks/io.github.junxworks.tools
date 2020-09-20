package io.github.junxworks.tools.pojo.db;

import io.github.junxworks.tools.pojo.db.model.DatabaseElement;

public class SqlServerDatabase extends DataBseDefault {

	private static final String TABLE_COMMENTS_SQL = "SELECT a.NAME,CAST (isnull(e.[value], '') AS nvarchar (100)) AS REMARK FROM sys.tables a INNER JOIN sys.objects c ON a.object_id = c.object_id LEFT JOIN sys.extended_properties e ON e.major_id = c.object_id AND e.minor_id = 0 AND e.class = 1 where c.name=?";
	private static final String COLUMN_COMMENTS_SQL = "select a.NAME,cast(isnull(e.[value],'') as nvarchar(100)) as REMARK from sys.columns a inner join sys.objects c on a.object_id=c.object_id and c.type='u' left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name=?";

	public SqlServerDatabase(DatabaseElement de) {
		super(de);
	}
}
