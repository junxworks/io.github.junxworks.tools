package io.github.junxworks.tools.pojo.db.utils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.pojo.db.DataBase;
import io.github.junxworks.tools.pojo.db.DatabaseFactory;
import io.github.junxworks.tools.pojo.db.model.DatabaseElement;
import io.github.junxworks.tools.pojo.db.model.Table;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Pojo类生成
 * 
 * @author levovo
 *
 */
public class BeanCreatAction {
	/**
	 * 生成Pojo类
	 * 
	 * @param path
	 *            目标路径
	 * @param packageName
	 *            包名
	 * @param tableNames
	 *            表名
	 * @return
	 * @throws Exception
	 */
	public static boolean creatBean(String path, String packageName, List<String> tableNames) throws Exception {
		if (StringUtil.isEmpty(path)) {
			throw new Exception("目标路径不能为空");
		}
		if (StringUtil.isEmpty(packageName)) {
			throw new Exception("包名不能为空");
		}
		if (null == tableNames || tableNames.size() < 1) {
			throw new Exception("表名不能为空");
		}

		DatabaseElement de = getDatabaseElement();
		if (StringUtil.isEmpty(de.getType()) || StringUtil.isEmpty(de.getUrl()) || StringUtil.isEmpty(de.getUsername())
				|| StringUtil.isEmpty(de.getPassword())) {
			throw new Exception("缺少数据库连接配置参数");
		}
		DataBase db = DatabaseFactory.creatDataBase(de);
		List<Table> tables = db.getTables(de.getSchema(), tableNames);
		Configuration cfg = Configuration.getDefaultConfiguration();
		cfg.setTemplateLoader(new TemplateLoader() {

			public void closeTemplateSource(Object templateSource) throws IOException {
				((InputStream) templateSource).close();
			}

			public Object findTemplateSource(String name) throws IOException {
				return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
			}

			public long getLastModified(Object templateSource) {
				return 0;
			}

			public Reader getReader(Object templateSource, String encoding) throws IOException {
				return new InputStreamReader((InputStream) templateSource, encoding);
			}
		});
		cfg.setEncoding(Locale.CHINA, "UTF-8");
		Template template = cfg.getTemplate("/io/github/junxworks/tools/pojo/db/model/junx_entity.ftl");
		for (Table table : tables) {
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("package", packageName);
			root.put("table", table);
			Writer out = new OutputStreamWriter(new FileOutputStream(path + "/" + table.getClassName() + ".java"),
					"UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		}
		return true;
	}

	/**
	 * 获取所有表名
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<Table> getAllTableName(String tableName) throws Exception {
		DatabaseElement de = getDatabaseElement();
		if (StringUtil.isEmpty(de.getType()) || StringUtil.isEmpty(de.getUrl()) || StringUtil.isEmpty(de.getUsername())
				|| StringUtil.isEmpty(de.getPassword())) {
			throw new Exception("缺少数据库连接配置参数");
		}
		DataBase db = DatabaseFactory.creatDataBase(de);
		List<Table> tName = db.queryAllTableNames(de.getSchema(), tableName);
		return tName;
	}

	/**
	 * 获取数据库配置
	 * 
	 * @return
	 */
	public static DatabaseElement getDatabaseElement() {
		IPreferenceStore store = JunxworksPlugin.getDefault().getPreferenceStore();
		DatabaseElement de = new DatabaseElement();
		de.setType(store.getString("dbType"));
		de.setUrl(store.getString("url"));
		de.setUsername(store.getString("user"));
		de.setPassword(store.getString("password"));
		de.setSchema(store.getString("schema"));
//		DatabaseElement de = new DatabaseElement();
//		de.setType("mysql");
//		de.setUrl("jdbc:mysql://10.111.125.137:3306/test");
//		de.setUsername("root");
//		de.setPassword("123456");
//		de.setSchema("mysql");
		
		//"mysql", "jdbc:mysql://10.111.125.137:3306/mysql", "root", "123456"
		return de;
	}

//	private static void inputstreamtofile(InputStream ins, File file) {
//		try {
//			OutputStream os = new FileOutputStream(file);
//			int bytesRead = 0;
//			byte[] buffer = new byte[8192];
//			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//				os.write(buffer, 0, bytesRead);
//			}
//			os.close();
//			ins.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
