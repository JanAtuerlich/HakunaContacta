package de.hakunacontacta.shared;

import java.io.Serializable;


/**
 * ExportOption
 * @author AlHafi
 * @category exportauswahlModul
 * @version 0.1beta
 */
public class ExportOption implements Comparable<ExportOption>, Serializable {

	private String sourceType;
	private String sourceField;
	private String displayName;
	private int priority;
	//

//	public ExportOption(String sourceType, String sourceField, int priority) {
//		this.sourceType = sourceType;
//		this.sourceField = sourceField;
//		this.priority = priority;
//	}
	
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getSourceField() {
		return sourceField;
	}

	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}

	@Override
	public int compareTo(ExportOption o) {
		return this.getPriority() - o.getPriority();
	}
	
	

}
