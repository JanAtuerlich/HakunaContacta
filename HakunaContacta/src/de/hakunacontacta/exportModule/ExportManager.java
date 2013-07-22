package de.hakunacontacta.exportModule;

import java.io.Serializable;
import java.util.ArrayList;

import de.hakunacontacta.shared.ExportField;
import de.hakunacontacta.shared.ExportOption;
import de.hakunacontacta.shared.ExportTypeEnum;

/**
 * ExportManager verwaltet ExportFields und ExportOptions
 */
public class ExportManager implements IExportManager, Serializable {

	private ExportTypeEnum exportType;
	private ArrayList<ExportField> exportFieldsCSV = new ArrayList<ExportField>();
	private ArrayList<ExportField> exportFieldsXML = new ArrayList<ExportField>();
	private ArrayList<ExportField> exportFieldsvCard = new ArrayList<ExportField>();
	private ArrayList<ExportField> exportFieldsCSVWord = new ArrayList<ExportField>();

	private ArrayList<ExportField> currentFields = exportFieldsCSV;

	public ExportManager() {
		super();
		this.addExportField("Vorname", exportFieldsCSVWord);
		this.addExportField("Nachname", exportFieldsCSVWord);
		this.addExportField("Adresse", exportFieldsCSVWord);

		this.addExportField("Vorname", exportFieldsvCard);
		this.addExportField("Nachname", exportFieldsvCard);
		this.addExportField("Zweitname", exportFieldsvCard);
		this.addExportField("Preffix", exportFieldsvCard);
		this.addExportField("Suffix", exportFieldsvCard);
		this.addExportField("Adresse", exportFieldsvCard);
		this.addExportField("Telefon", exportFieldsvCard);
		this.addExportField("Handy", exportFieldsvCard);
		this.addExportField("E-Mail privat", exportFieldsvCard);
		this.addExportField("E-Mail gesch�ftlich", exportFieldsvCard);
		this.addExportField("Kommentar", exportFieldsvCard);
		this.addExportField("Homepage", exportFieldsvCard);

		this.addExportField("Vorname", exportFieldsXML);
		this.addExportField("Nachname", exportFieldsXML);
		this.addExportField("Zweitname", exportFieldsXML);
		this.addExportField("Preffix", exportFieldsXML);
		this.addExportField("Suffix", exportFieldsXML);
		this.addExportField("Adresse", exportFieldsXML);
		this.addExportField("Telefon", exportFieldsXML);
		this.addExportField("Handy", exportFieldsXML);
		this.addExportField("E-Mail privat", exportFieldsXML);
		this.addExportField("E-Mail gesch�ftlich", exportFieldsXML);
		this.addExportField("Kommentar", exportFieldsXML);
		this.addExportField("Homepage", exportFieldsvCard);

	}

	/**
	 * Die Methode setExportFormat(...) setzt den Bearbeitungszeiger des ExportManagers auf die entsprechende Collection
	 * abh�ngign vom dem �bergebenen Enum Wert.
	 * 
	 * @param ExportTypeEnum gibt die Zielcollection an
	 * @return void
	 */
	public void setExportFormat(ExportTypeEnum type) {
		if (type == ExportTypeEnum.CSV) {
			currentFields = exportFieldsCSV;
			this.exportType = ExportTypeEnum.CSV;
		} else if (type == ExportTypeEnum.XML) {
			currentFields = exportFieldsXML;
			this.exportType = ExportTypeEnum.XML;
		} else if (type == ExportTypeEnum.vCard) {
			currentFields = exportFieldsvCard;
			this.exportType = ExportTypeEnum.vCard;
		} else if (type == ExportTypeEnum.CSVWord) {
			currentFields = exportFieldsCSVWord;
			this.exportType = ExportTypeEnum.CSVWord;
		}

	}

	public ExportTypeEnum getExportFormat() {
		return exportType;
	}

	public void addExportField(String name) {
		ExportField exportField = new ExportField();
		exportField.setName(name);
		currentFields.add(exportField);
	}

	//
	public void addExportField(String name, ArrayList<ExportField> exportFields) {
		ExportField exportField = new ExportField();
		exportField.setName(name);
		exportFields.add(exportField);
	}

	/**
	 * Die Methode setExportFormat(...) setzt den Bearbeitungszeiger des ExportManagers auf die entsprechende Collection
	 * abh�ngign vom dem �bergebenen Enum Wert und gibt diese zur�ck
	 * 
	 * @param ExportTypeEnum gibt die Zielcollection an
	 * @return Liefert die aktuell in Bearbeitung befindliche ArrayList zur�ck
	 */
	public ArrayList<ExportField> changeExportFormat(ExportTypeEnum type) {
		if (type == ExportTypeEnum.CSV) {
			currentFields = exportFieldsCSV;
			this.exportType = ExportTypeEnum.CSV;
		} else if (type == ExportTypeEnum.XML) {
			currentFields = exportFieldsXML;
			this.exportType = ExportTypeEnum.XML;
		} else if (type == ExportTypeEnum.vCard) {
			currentFields = exportFieldsvCard;
			this.exportType = ExportTypeEnum.vCard;
		} else if (type == ExportTypeEnum.CSVWord) {
			currentFields = exportFieldsCSVWord;
			this.exportType = ExportTypeEnum.CSVWord;
		}
		return currentFields;
	}

	public void addExportOption(String displayName, String exportField, String sourceFieldName, String sourceType, int priority) {
		for (ExportField exportFieldx : this.currentFields) {
			if (exportFieldx.getName() == exportField) {
				exportFieldx.addExportOption(sourceType, sourceFieldName, displayName, priority);
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

	@Override
	public void setExportField(ArrayList<ExportField> exportFields) {

		currentFields.clear();

		for (ExportField exportField : exportFields) {
			currentFields.add(exportField);
			for (ExportOption exportOption : exportField.getExportOptions()) {
			}
		}
	}
	
	

	public ArrayList<ExportField> getChosenExportFields() {
		return currentFields;
	}

	public ExportTypeEnum getExportType() {
		return exportType;
	}

}
