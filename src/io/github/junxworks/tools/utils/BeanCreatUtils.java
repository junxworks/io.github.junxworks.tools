package io.github.junxworks.tools.utils;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.actions.CreateMetadataAction;
import io.github.junxworks.tools.pojo.db.DataBase;
import io.github.junxworks.tools.pojo.db.DatabaseFactory;
import io.github.junxworks.tools.pojo.db.model.DatabaseElement;
import io.github.junxworks.tools.pojo.db.model.Table;
import io.github.junxworks.tools.pojo.db.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Pojo类生成
 * 
 * @author levovo
 *
 */
public class BeanCreatUtils {

	public static boolean creatBean(String path, String fileName,final String configId, String packageName,String tableName) throws Exception {
		if (StringUtil.isEmpty(path)) {
			throw new Exception("目标路径不能为空");
		}
		if (StringUtil.isEmpty(packageName)) {
			throw new Exception("包名不能为空");
		}
		if (null == tableName) {
			throw new Exception("表名不能为空");
		}

		DatabaseElement de = getDatabaseElement();
		if (StringUtil.isEmpty(de.getType()) || StringUtil.isEmpty(de.getUrl()) || StringUtil.isEmpty(de.getUsername()) || StringUtil.isEmpty(de.getPassword())) {
			throw new Exception("缺少数据库连接配置参数");
		}
		DataBase db = DatabaseFactory.creatDataBase(de);
		List<Table> tables = db.getTables(de.getSchema(), Arrays.asList(tableName));
		Configuration cfg = Configuration.getDefaultConfiguration();
		cfg.setTemplateLoader(new TemplateLoader() {

			public void closeTemplateSource(Object templateSource) throws IOException {
				((InputStream) templateSource).close();
			}

			public Object findTemplateSource(String name) throws IOException {
				String template = JunxworksPlugin.getDefault().getPreferenceStore().getString(configId);
				return new ByteArrayInputStream(template.getBytes());
				//				return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
			}

			public long getLastModified(Object templateSource) {
				return 0;
			}

			public Reader getReader(Object templateSource, String encoding) throws IOException {
				return new InputStreamReader((InputStream) templateSource, encoding);
			}
		});
		cfg.setEncoding(Locale.CHINA, "UTF-8");
		Template template = cfg.getTemplate("");
		for (Table table : tables) {
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("package", packageName);
			root.put("table", table);
			Writer out = new OutputStreamWriter(new FileOutputStream(path + File.separator + fileName), "UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		}
		return true;
	}

	/**
	 * 生成Pojo类
	 * 
	 * @param path        目标路径
	 * @param packageName 包名
	 * @param tableNames  表名
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
		if (StringUtil.isEmpty(de.getType()) || StringUtil.isEmpty(de.getUrl()) || StringUtil.isEmpty(de.getUsername()) || StringUtil.isEmpty(de.getPassword())) {
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
				String template = JunxworksPlugin.getDefault().getPreferenceStore().getString(CreateMetadataAction.METADATA_CONFIG_ID);
				return new ByteArrayInputStream(template.getBytes());
				//				return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
			}

			public long getLastModified(Object templateSource) {
				return 0;
			}

			public Reader getReader(Object templateSource, String encoding) throws IOException {
				return new InputStreamReader((InputStream) templateSource, encoding);
			}
		});
		cfg.setEncoding(Locale.CHINA, "UTF-8");
		Template template = cfg.getTemplate("");
		for (Table table : tables) {
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("package", packageName);
			root.put("table", table);
			Writer out = new OutputStreamWriter(new FileOutputStream(path + "/" + table.getClassName() + ".java"), "UTF-8");
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
		if (StringUtil.isEmpty(de.getType()) || StringUtil.isEmpty(de.getUrl()) || StringUtil.isEmpty(de.getUsername()) || StringUtil.isEmpty(de.getPassword())) {
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
		return de;
	}

}
