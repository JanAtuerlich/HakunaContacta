package de.hakunacontacta.exportModule;

import java.util.ArrayList;

/**
 * ExportManager verwaltet ExportFields und ExportOptions
 * @author AlHafi
 * @category exportauswahlModul
 * @version 0.1beta
 */

public class ExportManager implements IExportManager {
	
	private static ExportManager instance;
	//singleton pattern
	public static ExportManager getExportManager() {
		if (null == instance) {
			instance = new ExportManager();
		}
		return instance;

	}


	public enum exportTypeEnum {
		CSV, XML, vCard, CSVWord
	}; // Steht in Klassendiagramm noch als String

	private String exportType;
	private ArrayList<ExportField> exportFieldsCSV = new ArrayList<ExportField>();
	private ArrayList<ExportField> exportFieldsXML = new ArrayList<ExportField>();
	private ArrayList<ExportField> exportFieldsvCard = new ArrayList<ExportField>();
	private ArrayList<ExportField> exportFieldsCSVWord = new ArrayList<ExportField>();

	
	private ArrayList<ExportField> currentFields = exportFieldsCSV; //aktueller Bearbeitungszeiger

	
	/* (non-Javadoc)
	 * @see exportAuswahlModul.IExportManager#setExportFormat(exportAuswahlModul.ExportManager.exportTypeEnum)
	 */
	public void setExportFormat(exportTypeEnum type) {
		if (type == exportTypeEnum.CSV) {
			currentFields = exportFieldsCSV;
			this.exportType = "CSV";
		} else if (type == exportTypeEnum.XML) {
			currentFields = exportFieldsXML;
			this.exportType = "XML";
		} else if (type == exportTypeEnum.vCard) {
			currentFields = exportFieldsvCard;
			this.exportType = "vCard";
		} else if (type == exportTypeEnum.CSVWord) {
			currentFields = exportFieldsCSVWord;
			this.exportType = "CSVWord";
		}

	}


	public String getExportFormat() {
		return exportType;
	}

	public void addExportField(String name) {
		ExportField exportField = new ExportField(name);
		currentFields.add(exportField);
	}
	
	public void addExportField(String name, ArrayList<ExportField> exportFields) {
		ExportField exportField = new ExportField(name);
		exportFields.add(exportField);
	}

	public ArrayList<ExportField> changeExportFormat(exportTypeEnum type) {
		if (type == exportTypeEnum.CSV) {
			currentFields = exportFieldsCSV;
			this.exportType = "CSV";
		} else if (type == exportTypeEnum.XML) {
			currentFields = exportFieldsXML;
			this.exportType = "XML";
		} else if (type == exportTypeEnum.vCard) {
			currentFields = exportFieldsvCard;
			this.exportType = "Costum";
		} else if (type == exportTypeEnum.CSVWord) {
			currentFields = exportFieldsCSVWord;
			this.exportType = "CSVWord";
		}
		return currentFields;
	}

	public void addExportOption(String exportField, String sourceFieldName, String sourceType, int priority) {
		for (ExportField exportFieldx : this.currentFields) {
			if (exportFieldx.getName() == exportField) {
				exportFieldx.addExportOption(sourceType, sourceFieldName, priority);
			}
		}
	}

	public void removeExportOption(String exportField, String sourceType, String sourceField) {
		for (ExportField exportFieldx : this.currentFields) {
			if (exportFieldx.getName() == exportField) {
				exportFieldx.removeExportOption(sourceType, sourceField);
			}
		}
	}


	public void removeExportField(String exportField) {
		for (ExportField exportFieldx : this.currentFields) {
			if (exportFieldx.getName() == exportField) {
				this.currentFields.remove(exportFieldx);
			}
		}
	}

	public ArrayList<ExportField> getChosenExportFields() {
		return currentFields;
	}

	public ExportManager() {
		super();
		this.addExportField("Vorname", exportFieldsCSVWord);
		this.addExportField("Nachname", exportFieldsCSVWord);
		this.addExportField("Adresse", exportFieldsCSVWord);
		
		this.addExportField("Vorname", exportFieldsvCard);
		this.addExportField("Nachname", exportFieldsvCard);
		this.addExportField("Adresse", exportFieldsvCard);
		this.addExportField("Telefon", exportFieldsvCard);
		this.addExportField("Handy", exportFieldsvCard);
		this.addExportField("E-Mail privat", exportFieldsvCard);
		this.addExportField("E-Mail geschäftlich", exportFieldsvCard);
		this.addExportField("Kommentar", exportFieldsvCard);
				
		// hier sollte VCard die entsprechenden Standardfelder zugewiesen bekommen
	}

	public String toString() {
		String string = "CurrentFields \n";
		for (ExportField exportFieldx : this.currentFields) {
			string += "\t" + exportFieldx.getName() + " \n";
		}

		string += "CostumFields \n";
		for (ExportField exportFieldx : this.exportFieldsvCard) {
			string += "\t" + exportFieldx.getName() + " \n";
		}

		string += "CSVFields \n";
		for (ExportField exportFieldx : this.exportFieldsCSV) {
			string += "\t" + exportFieldx.getName() + " \n";
		}
		return string;
	}
	



}


