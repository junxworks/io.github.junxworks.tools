package io.github.junxworks.tools;

import java.io.Serializable;

public class TableModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer rowIndex;
	
	private String tableName;
	
	private String description;
	
	public TableModel(Integer rowIndex,String tableName,String description){
		this.rowIndex = rowIndex;
		this.tableName = tableName;
		this.description = description;
	}
	
	public Integer getRowIndex() {
		return rowIndex;
	}
	
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}



	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
}
