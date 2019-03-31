package io.github.junxworks.tools;

import java.io.Serializable;
import java.util.List;

public class EntityModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String packageName;
	
	private String className;
	
	private String entityDb;
	
	private String author;
	
	private String date;
	
	private boolean isImportTime;
	
	private boolean isImportDate;
	
	private boolean isimportBigsem;
	
	private List<Column> columnList;
	
	
	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public String getEntityDb() {
		return entityDb;
	}


	public void setEntityDb(String entityDb) {
		this.entityDb = entityDb;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public boolean isImportTime() {
		return isImportTime;
	}


	public void setImportTime(boolean isImportTime) {
		this.isImportTime = isImportTime;
	}


	public boolean isImportDate() {
		return isImportDate;
	}


	public void setImportDate(boolean isImportDate) {
		this.isImportDate = isImportDate;
	}


	public boolean isIsimportBigsem() {
		return isimportBigsem;
	}


	public void setIsimportBigsem(boolean isimportBigsem) {
		this.isimportBigsem = isimportBigsem;
	}


	public List<Column> getColumnList() {
		return columnList;
	}


	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public class Column implements Serializable{
		
		private static final long serialVersionUID = 1L;

		private String properName;
		
		private String columnName;
		
		private String columnType;
		
		private String isPrimaryKey;
		
		private String isUnique;
		
		private String isNullable;
		
		private String isGenare;
		
		private String length;
		
		private String decimalDigits;
		
		private String method;
		
		private String desc;

		public String getProperName() {
			return properName;
		}

		public void setProperName(String properName) {
			this.properName = properName;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getColumnType() {
			return columnType;
		}

		public void setColumnType(String columnType) {
			this.columnType = columnType;
		}

		public String getIsPrimaryKey() {
			return isPrimaryKey;
		}

		public void setIsPrimaryKey(String isPrimaryKey) {
			this.isPrimaryKey = isPrimaryKey;
		}

		public String getIsUnique() {
			return isUnique;
		}

		public void setIsUnique(String isUnique) {
			this.isUnique = isUnique;
		}

		public String getIsNullable() {
			return isNullable;
		}

		public void setIsNullable(String isNullable) {
			this.isNullable = isNullable;
		}

		public String getIsGenare() {
			return isGenare;
		}

		public void setIsGenare(String isGenare) {
			this.isGenare = isGenare;
		}

		public String getLength() {
			return length;
		}

		public void setLength(String length) {
			this.length = length;
		}

		public String getDecimalDigits() {
			return decimalDigits;
		}

		public void setDecimalDigits(String decimalDigits) {
			this.decimalDigits = decimalDigits;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		
	}
}
