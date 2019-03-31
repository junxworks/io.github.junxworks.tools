package io.github.junxworks.tools.pojo.db.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;

import io.github.junxworks.tools.pojo.db.DataBase;
import io.github.junxworks.tools.pojo.db.DatabaseFactory;
import io.github.junxworks.tools.pojo.db.model.Column;
import io.github.junxworks.tools.pojo.db.model.DatabaseElement;
import io.github.junxworks.tools.pojo.db.model.Table;

/**
 * Pojo类修改
 * 
 * @author levovo
 *
 */
public class BeanEditAction {
	public static boolean editBean(CompilationUnit unit) throws Exception {

		DatabaseElement de = BeanCreatAction.getDatabaseElement();
		if (StringUtil.isEmpty(de.getType()) || StringUtil.isEmpty(de.getUrl()) || StringUtil.isEmpty(de.getUsername())
				|| StringUtil.isEmpty(de.getPassword())) {
			throw new Exception("缺少数据库连接配置参数");
		}

		IJavaElement[] jes = unit.getChildren();
		for (IJavaElement j : jes) {
			if (j instanceof SourceType) {
				SourceType st = (SourceType) j;
				
				//st.getme
				
				// 根据类名查询数据库表
				DataBase db = DatabaseFactory.creatDataBase(de);
				ArrayList<String> inTabelName = new ArrayList<String>();
				inTabelName.add(st.getTypeQualifiedName().toLowerCase());
				List<Table> tables = db.getTables(de.getSchema(), inTabelName);
				if (tables == null || tables.size() < 1) {
					throw new Exception("未找到对应的数据库表");
				}
				// 获取所有数据库所有字段
				List<Column> baseColumns = tables.get(0).getBaseColumns();
				List<Column> pColumns = tables.get(0).getPrimaryKeys();
				for (Column column : pColumns) {
					baseColumns.add(column);
				}
//				ISourceRange sr = st.getAnnotation("").getNameRange();
//				//st.set
//				CompilationUnitEditor editor = new CompilationUnitEditor();
//				IDocument doc=editor.getDocumentProvider().getDocument();
				IField[] Fields = st.getFields();
				// 循环比对字段和属性书否匹配
				for (Column column : baseColumns) {
					Column tColumn = column;
					boolean flag = false;
					boolean isAdd = true;
					for (IField iField : Fields) {
						if (iField.getElementName().equals("serialVersionUID"))
							continue;
						IAnnotation ia = iField.getAnnotation("DBField");
						if(null == ia){
							continue;
						}
						// 如果匹配,校验length和dataType是否相同
						// 不相同则删除该属性
						if (iField.getElementName().equals(tColumn.getColumnName())) {
							isAdd = false;
							// 获取注解
							
							IMemberValuePair[] imps = ia.getMemberValuePairs();
							// 匹配注解,数据库里的length,type
							for (IMemberValuePair imp : imps) {
								if (flag)
									break;
								String val = String.valueOf(imp.getValue());
								switch (imp.getMemberName()) {
								case "length":
									if (!val.equals("" + tColumn.getSize())) {
										flag = true;
									}
									break;
								case "dataType":
									if (!val.equals(tColumn.getJdbcTypeName())) {
										flag = true;
									}
									break;
								}
							}
							if (flag) {
								iField.delete(true, null);
							}
							break;

						}
					}
					// 新增属性和对应方法,注解
					if (flag || isAdd) {
						// 添加属性
						String fieldName = getFieldStr(tColumn);
						st.createField(fieldName, null, true, null);
						// 添加属性get方法
						String getMethodStr = getGetMethodStr(tColumn);
						st.createMethod(getMethodStr, null, true, null);
						// 添加属性set方法
						String setMethodStr = getSetMethodStr(tColumn);
						st.createMethod(setMethodStr, null, true, null);
					}
					

				}
			}
		}
		return true;
	}

	/**
	 * 获取属性字符串
	 * 
	 * @param tColumn
	 * @return
	 */
	private static String getFieldStr(Column tColumn) {
		String key = "";
		if(tColumn.isPrimaryKey()){
			key += "@PrimaryKey\n";
		}
		return key + "@DBField(name=\"" + tColumn.getColumnName() + "\",desc=\"" + tColumn.getRemarks() + "\",length="
				+ tColumn.getSize() + ",dataType=\"" + tColumn.getJdbcTypeName() + "\")\nprivate " 
				+ tColumn.getJavaType() + " " + tColumn.getColumnName() + ";\n";
	}

	/**
	 * 获取属性get方法字符串
	 * 
	 * @param tColumn
	 * @return
	 */
	private static String getGetMethodStr(Column tColumn) {
		
		return "public " + tColumn.getJavaType() + " " + tColumn.getGetterMethodCamelName() 
				+ "() {\n	return this."
				+ tColumn.getColumnName() + ";\n}";
	}

	/**
	 * 获取属性set方法字符串
	 * 
	 * @param tColumn
	 * @return
	 */
	private static String getSetMethodStr(Column tColumn) {
		 String setStr = "public " + tColumn.getJavaType() + " " + tColumn.getSetterMethodCamelName() 
				+ "(" + tColumn.getJavaType() +" "+ tColumn.getColumnName() +") {\n	this."
				+ tColumn.getColumnName() + " = " + tColumn.getColumnName() + ";\n";
		 return setStr + "	setAttribute(\"" + tColumn.getColumnName() + "\"," + tColumn.getColumnName() +");\n}\n";
	}
}
