package io.github.junxworks.tools;

import java.io.Serializable;
import java.util.List;

public class ServiceModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String packageName;
	
	private String className;
	
	private String TranscationLevel;
	
	private String Transcation;
	
	private String pool;
	
	private String readTool;
	
	private String writeTool;
	
	private String author;
	
	private List<Compoment> compList;
	
	
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


	public String getTranscationLevel() {
		return TranscationLevel;
	}


	public void setTranscationLevel(String transcationLevel) {
		TranscationLevel = transcationLevel;
	}


	public String getTranscation() {
		return Transcation;
	}


	public void setTranscation(String transcation) {
		Transcation = transcation;
	}


	public String getPool() {
		return pool;
	}


	public void setPool(String pool) {
		this.pool = pool;
	}


	public String getReadTool() {
		return readTool;
	}


	public void setReadTool(String readTool) {
		this.readTool = readTool;
	}


	public String getWriteTool() {
		return writeTool;
	}


	public void setWriteTool(String writeTool) {
		this.writeTool = writeTool;
	}


	public List<Compoment> getCompList() {
		return compList;
	}


	public void setCompList(List<Compoment> compList) {
		this.compList = compList;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public class Compoment implements Serializable{
		
		private static final long serialVersionUID = 1L;

		private String compName;
		
		private String compClass;
		
		private String className;
		
		private String packageName;

		public String getCompName() {
			return compName;
		}

		public void setCompName(String compName) {
			this.compName = compName;
		}

		public String getCompClass() {
			return compClass;
		}

		public void setCompClass(String compClass) {
			this.compClass = compClass;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		
		
	}
}
